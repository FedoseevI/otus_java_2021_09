package ru.otus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.Message;
import ru.otus.crm.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }


    public List<Message> findMessagesByUsersName(String user, String userName) {
        return messageRepository.findMessagesByUsersName(user, userName);
    }
}
