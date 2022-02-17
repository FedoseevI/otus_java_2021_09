package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";
    private static final String TEMPLATE_ATTR_ERROR = "error";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;


    public ClientServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<String> error = new ArrayList<>();
        error.add(req.getParameter("error"));
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, dbServiceClient.findAll());
        paramsMap.put(TEMPLATE_ATTR_ERROR, error);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

}
