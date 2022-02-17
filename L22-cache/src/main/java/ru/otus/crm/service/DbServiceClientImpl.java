package ru.otus.crm.service;

import lombok.extern.log4j.Log4j2;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.Client;
import ru.otus.core.sessionmanager.TransactionRunner;

import java.util.List;
import java.util.Optional;

@Log4j2
public class DbServiceClientImpl implements DBServiceClient {
    private final MyCache<String, Client> myCache = new MyCache<>();
    private final HwListener<String, Client> listener = new HwListener<String, Client>() {
        @Override
        public void notify(String key, Client value, String action) {
            log.info("key:{}, value:{}, action: {}", key, value, action);
        }
    };

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        // добавляем listener
        myCache.addListener(listener);
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                myCache.put(Long.toString(clientId), client);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            myCache.put(Long.toString(client.getId()), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        // пробуем получить данные из кэша
        Optional<Client> clientFromCache = Optional.ofNullable(myCache.get(Long.toString(id)));
        if (clientFromCache.isPresent()) {
            return clientFromCache;
        }
        // получаем данные из БД если не удалось получить из кэша
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            if (clientOptional.isPresent()) myCache.put(Long.toString(id), clientOptional.get());

            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
