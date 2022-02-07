package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import java.util.List;
import java.util.Optional;


public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);
    private final MyCache<String, Manager> myCache = new MyCache<>();

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        var listener = new HwListener<String, Manager>() {
            @Override
            public void notify(String key, Manager value, String action) {
                if (action.equals(MyCache.actionGet)) {
                    log.info("key:{}, value:{}, action: {}", key, value, action);
                }
            }
        };
        myCache.addListener(listener);

        Optional<Manager> managerFromCache = Optional.ofNullable(myCache.get(Long.toString(no)));
        myCache.removeListener(listener);

        if (managerFromCache.isPresent()) {
            return managerFromCache;
        }

        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            if (managerOptional.isPresent()) {
                var listener_put = new HwListener<String, Manager>() {
                    @Override
                    public void notify(String key, Manager value, String action) {
                        if (action.equals(MyCache.actionPut)) {
                            log.info("key:{}, value:{}, action: {}", key, value, action);
                        }
                    }
                };
                myCache.addListener(listener_put);
                myCache.put(Long.toString(no), managerOptional.get());
                myCache.removeListener(listener_put);
            }
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
       });
    }
}
