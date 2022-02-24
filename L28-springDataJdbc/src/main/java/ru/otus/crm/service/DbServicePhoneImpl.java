package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.Phone;
import ru.otus.crm.repository.PhoneRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.*;

@Service
public class DbServicePhoneImpl implements DBServicePhone {
    private static final Logger log = LoggerFactory.getLogger(DbServicePhoneImpl.class);

    private final TransactionManager transactionManager;
    private final PhoneRepository phoneRepository;

    public DbServicePhoneImpl(TransactionManager transactionManager, PhoneRepository phoneRepository) {
        this.transactionManager = transactionManager;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public List<Phone> savePhones(List<Phone> phoneList) {
        return transactionManager.doInTransaction(() -> {
            List<Phone> savedPhones = new ArrayList<>();
            phoneList.forEach(phone -> {
                if (!phone.getNumber().equals("")) savedPhones.add((phoneRepository.save(phone)));
            });
            log.info("saved phones: {}", savedPhones);
            return savedPhones;
        });
    }

    @Override
    public List<Phone> findAll() {
        List<Phone> phoneList = new ArrayList<>();
        phoneRepository.findAll().forEach(phoneList::add);
        return phoneList;
    }

    @Override
    public Optional<Phone> getPhone(long id) {
        var phoneOptional = phoneRepository.findById(id);
        log.info("phones: {}", phoneOptional);
        return phoneOptional;
    }

}
