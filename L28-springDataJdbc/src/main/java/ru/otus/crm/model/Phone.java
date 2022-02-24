package ru.otus.crm.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Builder(toBuilder = true)
@Table("phones")
public class Phone implements Cloneable {

    @Id
    @Getter
    private Long id;

    @NonNull
    @Column("number")
    @Getter
    @Setter
    private String number;

    @Getter
    @Setter
    @Column("client_id")
    private Long clientId;

    public Phone(String number, Long clientId) {
        this.id = null;
        this.number = number;
        this.clientId = clientId;
    }

    @PersistenceConstructor
    public Phone(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public Phone() {
    }

    @Override
    public Phone clone() {
        return Phone.builder().id(id).number(number).clientId(clientId).build();
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
