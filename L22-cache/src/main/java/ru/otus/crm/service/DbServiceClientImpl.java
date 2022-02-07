package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.Client;
import ru.otus.core.sessionmanager.TransactionRunner;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private final MyCache<String, Client> myCache = new MyCache<>();

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                if (action.equals(MyCache.actionGet)) {
                    log.info("key:{}, value:{}, action: {}", key, value, action);
                }
            }
        };
        myCache.addListener(listener);

        Optional<Client> clientFromCache = Optional.ofNullable(myCache.get(Long.toString(id)));
        myCache.removeListener(listener);

        if (clientFromCache.isPresent()) {
            return clientFromCache;
        }


        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            if (clientOptional.isPresent()) {
                var listener_put = new HwListener<String, Client>() {
                    @Override
                    public void notify(String key, Client value, String action) {
                        if (action.equals(MyCache.actionPut)) {
                            log.info("key:{}, value:{}, action: {}", key, value, action);
                        }
                    }
                };
                myCache.addListener(listener_put);
                myCache.put(Long.toString(id), clientOptional.get());
                myCache.removeListener(listener_put);
            }
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
