package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.formInput.ClientInput;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceAddress;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DBServicePhone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.List.*;

@Controller
public class ClientController {

    private final String osData;
    private final String applicationYmlMessage;
    private final DBServiceClient dbServiceClient;
    private final DBServiceAddress dbServiceAddress;
    private final DBServicePhone dbServicePhone;

    public ClientController(@Value("${app.client-list-page.msg:Тут может находиться ваша реклама}")
                                    String applicationYmlMessage,
                            @Value("OS: #{T(System).getProperty(\"os.name\")}, " +
                                    "JDK: #{T(System).getProperty(\"java.runtime.version\")}")
                                    String osData,
                            DBServiceClient dbServiceClient, DBServiceAddress dbServiceAddress, DBServicePhone dbServicePhone) {
        this.applicationYmlMessage = applicationYmlMessage;
        this.osData = osData;
        this.dbServiceClient = dbServiceClient;
        this.dbServiceAddress = dbServiceAddress;
        this.dbServicePhone = dbServicePhone;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        List<Client> clients = dbServiceClient.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        var emptyClient = new ClientInput();
        model.addAttribute("client", emptyClient);
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute ClientInput client) {
        var savedClient = dbServiceClient.save(client);
        return new RedirectView("/", true);
    }

}
