package ru.otus.crm.service;

import ru.otus.crm.formInput.ClientInput;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    Client save(ClientInput client);

    Optional<Client> getClient(long id);

    List<Client> findAll();

    List<Client> findByName(String name);
}
