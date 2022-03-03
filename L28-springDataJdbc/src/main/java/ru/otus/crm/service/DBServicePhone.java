package ru.otus.crm.service;

import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DBServicePhone {

    List<Phone> savePhones(List<Phone> phoneSet);

    List<Phone> findAll();

    Optional<Phone> getPhone(long id);
}
