package ru.otus.crm.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "address")
public class Address implements Cloneable {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name="identifier_address", sequenceName="address_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="identifier_address")
    @Getter
    private Long id;

    @Column(name = "street")
    @Getter @Setter
    private String street;

    @OneToOne(mappedBy = "address")
    @Getter @Setter
    private Client client;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address(String street,Client client) {
        this.street=street;
        this.client=client;
    }

    public Address() {
    }

    @Override
    public Address clone() {
        return Address.builder().id(id).street(street).client(client).build();
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }

}
