package ru.otus.crm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.Message;


import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    @Query(value = "with msgs as\n" +
            "(select m.*,(select us.username from users us where us.id=user_from) as username_from,\n" +
            "(select us.username from users us where us.id=user_to) as username_to\n" +
            "from messages m)\n" +
            "select * from msgs where (username_from=:user and username_to=:username) or (username_from=:username and username_to=:user)",
            resultSetExtractorClass = MessageResultSetExtractorClass.class)
    List<Message> findMessagesByUsersName(@Param("user") String user, @Param("username") String username);

}
