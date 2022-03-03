package ru.otus.crm.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@Builder(toBuilder = true)
@Table("address")
public class Address implements Cloneable {

    @Id
    @Getter
    private Long id;

    @Getter
    @Setter
    private String street;

    @Column("client_id")
    @Getter
    @Setter
    private Long clientId;

    public Address(String street, Long clientId) {
        this.id = null;
        this.clientId = clientId;
        this.street = street;
    }

    @PersistenceConstructor
    public Address(Long id, String street, Long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }


    @Override
    public Address clone() {
        return Address.builder().id(id).street(street).clientId(clientId).build();
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }

}
