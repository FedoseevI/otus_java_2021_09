package ru.otus.crm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Table("client")
public class Client implements Cloneable {

    @Id
    private Long id;

    @NonNull
    private String name;


    @Getter
    @Setter
    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    @Getter
    @Setter
    private Set<Phone> phones;


    @PersistenceConstructor
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address + '\'' +
                ", phones=" + phones + '\'' +
                '}';
    }

    public String getAllPhonesString() {
        return phones
                .stream()
                .map(Phone::getNumber)
                .collect(Collectors.joining(","));
    }

    public String getAddressString() {
        return address.getStreet();
    }
}
