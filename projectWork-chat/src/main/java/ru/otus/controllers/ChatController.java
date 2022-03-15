package ru.otus.controllers;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.crm.model.Message;
import ru.otus.crm.model.User;
import ru.otus.service.ExtendedUserData;
import ru.otus.service.MessageService;
import ru.otus.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Controller
public class ChatController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss", Locale.US);
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public ChatController(SimpMessageSendingOperations messagingTemplate, UserService userService, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.messageService = messageService;
    }

    @SneakyThrows
    @MessageMapping("/sendMessage.{user}.{username}")
    @SendTo("/topic/response.{user}.{username}")
    public Message sendMessage(@DestinationVariable String user, @DestinationVariable String username, @Payload String jsonMessage) {
        JSONObject jsonObject = new JSONObject(jsonMessage);
        Message message = new Message();
        User userFrom = userService.findUserByUsername(jsonObject.getString("userFrom"));
        User userTo = userService.findUserByUsername(jsonObject.getString("userTo"));
        message.setUserFrom(userFrom.getId());
        message.setUserTo(userTo.getId());
        message.setUsernameFrom(userFrom.getUsername());
        message.setUsernameTo(userTo.getUsername());
        message.setMessageText(jsonObject.getString("text"));
        message.setMessageSendTime(LocalDateTime.parse(jsonObject.getString("sendingTime"), formatter));
        messageService.saveMessage(message);
        this.messagingTemplate.convertAndSend("/topic/messageFor." + userTo.getUsername(), message);
        return message;
    }

    @MessageMapping("/chat.{user}.{username}")
    public void showChat(@DestinationVariable String user, @DestinationVariable String username) {
        var userMessages = messageService.findMessagesByUsersName(user, username);
        userMessages.forEach(m -> this.messagingTemplate.convertAndSend("/topic/response." + username + "." + user, m));
    }

    @GetMapping("/chat")
    public String showChat(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user = ((ExtendedUserData) principal).getUsername();
        var users = userService.findAllExceptUser(user);
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return "chat";
    }
}
