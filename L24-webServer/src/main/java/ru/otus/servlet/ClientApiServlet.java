package ru.otus.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class ClientApiServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;

    public ClientApiServlet(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var clients = dbServiceClient.findAll();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        log.info(clients);
        out.print(clients.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String error = "";
        try {
            resp.setContentType("application/text");
            resp.setCharacterEncoding("UTF-8");

            String clientname = req.getParameter("name");
            String clientaddress = req.getParameter("address");
            String clientphone = req.getParameter("phone");

            if (clientname.isEmpty()) {
                error = "client name is null";
            } else {
                var phone = new Phone();
                phone.setNumber(clientphone);
                var address = new Address();
                address.setStreet(clientaddress);
                var client = new Client(null, clientname, address, List.of(phone));
                phone.setClient(client);
                address.setClient(client);
                dbServiceClient.saveClient(client);
            }
        } catch (Exception e) {
            error = "error adding client";
        }

        resp.sendRedirect("/client?error=" + error);

    }


}
