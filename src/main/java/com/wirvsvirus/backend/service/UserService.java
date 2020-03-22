package com.wirvsvirus.backend.service;

import com.wirvsvirus.backend.exception.UserNotFoundException;
import com.wirvsvirus.backend.model.User;
import com.wirvsvirus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> listUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User createUser(User user) {
        user.setUserId(null);
        return userRepository.save(user);
    }

    public User getUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("The user having the 'userId': '" + userId + "' does not exist");
        }
        return userOptional.get();
    }

    public User updateUser(Long userId, User user) {
        User existingUser = getUser(userId);

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setSocialId(user.getSocialId());

        return userRepository.save(existingUser);
    }

    public void verifyUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("The user having the 'userId': '" + userId + "' does not exist");
        }
    }

}
