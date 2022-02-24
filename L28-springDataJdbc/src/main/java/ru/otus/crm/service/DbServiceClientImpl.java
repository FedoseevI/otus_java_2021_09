package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.formInput.ClientInput;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    public DbServiceClientImpl(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(ClientInput clientInput) {
        return transactionManager.doInTransaction(() -> {
            var clientFromInput = clientInput.toClient();
            var phones = clientFromInput.getPhones();
            var address = clientFromInput.getAddress();
            var savedClient = clientRepository.save(new Client(null, clientFromInput.getName(), new Address(), Set.of(new Phone())));
            var phonesWithClientId = phones.stream().map(e -> new Phone(null, e.getNumber(), savedClient.getId())).collect(Collectors.toSet());
            savedClient.setPhones(phonesWithClientId);
            address.setClientId(savedClient.getId());
            savedClient.setAddress(address);
            Client resultSave = null;
            if ((long) phones.size() > 0 || address != null) {
                resultSave = clientRepository.save(savedClient);
            }
            log.info("saved client: {}", savedClient);
            return resultSave;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<>(clientRepository.findAll());
        log.info("clientList:{}", clientList);
        return clientList;
    }

    @Override
    public List<Client> findByName(String name) {
        var clientList = new ArrayList<>(clientRepository.findByName(name));
        log.info("clientList:{}", clientList);
        return clientList;
    }
}
