package ru.otus.crm.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @Getter
    @Setter
    private Address address;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "client")
    @Getter
    @Setter
    private List<Phone> phones = new ArrayList<>();

    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.address.setClient(this);
        for (Phone phone : phones) phone.setClient(this);
        this.phones = phones;
    }


    @Override
    public Client clone() {
        var clientClone = new Client(this.id, this.name);
        if (address != null) {
            clientClone.setAddress(this.address.clone());
            clientClone.getAddress().setClient(clientClone);
        };

        if (this.phones != null) {
            var phoneList =
                    this.phones.stream().map(Phone::clone).map(phones -> {
                        phones.setClient(clientClone);
                        return phones;
                    }).collect(Collectors.toList());
            clientClone.setPhones(phoneList);
        }

        return clientClone;
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
}
