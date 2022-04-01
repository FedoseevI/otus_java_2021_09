package ru.otus.crm.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.crm.model.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageResultSetExtractorClass implements ResultSetExtractor {
    @Override
    public List<Message> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var messageList = new ArrayList<Message>();
        while (rs.next()) {
            Message message = new Message();
            var user_from = rs.getString("user_from");
            var user_to = rs.getString("user_to");
            message.setUserFrom(Integer.parseInt(user_from));
            message.setUserTo(Integer.parseInt(user_to));
            message.setMessageSendTime(rs.getObject("message_send_time", java.time.LocalDateTime.class));
            message.setMessageText(rs.getString("message_text"));
            message.setUsernameFrom(rs.getString("username_from"));
            message.setUsernameTo(rs.getString("username_to"));
            messageList.add(message);
        }
        return messageList;
    }
}
