package ru.otus.crm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.User;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Integer> {
    @Override
    List<User> findAll();

    @Query(value = "select * from users where username <> :username")
    List<User> findAllExceptUser(@Param("username") String username);

    User findByUsername(String username);
}
