package ru.otus.crm.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table("messages")
@Getter
@Setter
public class Message {

    @Id
    private Integer id;

    @Column("user_from")
    private Integer userFrom;

    @Column("user_to")
    private Integer userTo;

    @Column("message_text")
    private String messageText;

    @Column("message_send_time")
    private LocalDateTime messageSendTime;

    @Transient
    private String usernameFrom;

    @Transient
    private String usernameTo;

    @PersistenceConstructor
    public Message(Integer id, Integer userFrom, Integer userTo, String messageText, LocalDateTime messageSendTime) {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.messageText = messageText;
        this.messageSendTime = messageSendTime;
    }

    public Message() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(userFrom, message.userFrom) && Objects.equals(userTo, message.userTo) && Objects.equals(messageText, message.messageText) && Objects.equals(messageSendTime, message.messageSendTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userFrom, userTo, messageText, messageSendTime);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                ", text='" + messageText + '\'' +
                ", sendingTime=" + messageSendTime +
                '}';
    }
}


