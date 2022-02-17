package ru.otus.crm.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "phones")
public class Phone implements Cloneable {

    @Id
    @SequenceGenerator(name = "identifier_phone", sequenceName = "phones_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "identifier_phone")
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "number")
    @Getter
    @Setter
    private String number;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    @Getter
    @Setter
    private Client client;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone() {
    }

    @Override
    public Phone clone() {
        return Phone.builder().id(id).number(number).client(client).build();
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
