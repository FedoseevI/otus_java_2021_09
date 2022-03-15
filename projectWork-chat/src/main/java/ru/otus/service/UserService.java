package ru.otus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.User;
import ru.otus.crm.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> findAllExceptUser(String username) {
        return userRepository.findAllExceptUser(username);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
