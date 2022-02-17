package ru.otus.cachehw;

import lombok.extern.log4j.Log4j2;
import org.flywaydb.core.Flyway;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;
import java.util.ArrayList;

@Log4j2
public class HWCacheDemo {

    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();
        log.info("Lesson Integer cache");
        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 1);

        log.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);


        log.info("database cache");

// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

// Работа с клиентом
        EntityClassMetaData entityClassMetaDataClient = new EntityClassMetaDataImpl(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);

        var clientIdList = new ArrayList<Long>();
        for (int i = 0; i < 1000; i++) {
            var newClient = new Client("client_" + i);
            var savedClient = dbServiceClient.saveClient(newClient);
            var clientSelected = dbServiceClient.getClient(savedClient.getId()).orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient.getId()));
            clientIdList.add(savedClient.getId());
            log.info("clientSecondSelected:{}", clientSelected);
        }

        for (Long id : clientIdList) dbServiceClient.getClient(id);

        EntityClassMetaData entityClassMetaDataManager = new EntityClassMetaDataImpl(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);

        var managerNoList = new ArrayList<Long>();
        for (int i = 0; i < 1000; i++) {
            var newManager = new Manager("manager_" + i);
            var savedManager = dbServiceManager.saveManager(newManager);
            var managerSelected = dbServiceManager.getManager(savedManager.getNo()).
                    orElseThrow(() -> new RuntimeException("Manager not found, id:" + savedManager.getNo()));
            managerNoList.add(savedManager.getNo());
            log.info("managerSelected:{}", managerSelected);
        }

        for (Long no : managerNoList) dbServiceManager.getManager(no);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
