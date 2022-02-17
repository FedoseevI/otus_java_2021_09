package ru.otus.crm.service;

import lombok.extern.log4j.Log4j2;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Manager;

import java.util.List;
import java.util.Optional;

@Log4j2
public class DbServiceManagerImpl implements DBServiceManager {
    private final MyCache<String, Manager> myCache = new MyCache<>();
    private final HwListener<String, Manager> listener = new HwListener<String, Manager>() {
        @Override
        public void notify(String key, Manager value, String action) {
            log.info("key:{}, value:{}, action: {}", key, value, action);
        }
    };

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        // добавляем listener
        myCache.addListener(listener);
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                myCache.put(Long.toString(managerNo), manager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            myCache.put(Long.toString(manager.getNo()), manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        // пробуем получить данные из кэша
        Optional<Manager> managerFromCache = Optional.ofNullable(myCache.get(Long.toString(no)));
        if (managerFromCache.isPresent()) {
            return managerFromCache;
        }
        // если не удалось получить данные из кэша идем за данными в БД
        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            if (managerOptional.isPresent()) myCache.put(Long.toString(no), managerOptional.get());
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
