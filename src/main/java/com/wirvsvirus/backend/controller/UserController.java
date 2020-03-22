package com.wirvsvirus.backend.controller;

import com.wirvsvirus.backend.model.User;
import com.wirvsvirus.backend.model.Users;
import com.wirvsvirus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Users> getUsers() {
        Users users = new Users();
        users.setUsers(userService.listUsers());
        users.add(linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
        users.add(linkTo(methodOn(UserController.class).postUser(new User())).withRel("register_user"));
        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Users> postUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        HttpHeaders responseHeaders = new HttpHeaders();
        String link = linkTo(methodOn(UserController.class).getUser(newUser.getUserId())).toString();
        responseHeaders.set("location", link);

        return new ResponseEntity(responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
        User user = userService.getUser(userId);
        user.add(linkTo(methodOn(UserController.class).getUser(user.getUserId())).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).getUsers()).withRel("all"));
        user.add(linkTo(methodOn(UserController.class).updateUser(user.getUserId(), new User())).withRel("update_user"));
        user.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaires(user.getUserId())).withRel("questionnaires"));
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId,
                                           @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        updatedUser.add(linkTo(methodOn(UserController.class).getUser(updatedUser.getUserId())).withSelfRel());
        updatedUser.add(linkTo(methodOn(UserController.class).getUsers()).withRel("all"));
        updatedUser.add(linkTo(methodOn(UserController.class).updateUser(updatedUser.getUserId(), new User())).withRel("update_user"));
        updatedUser.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaires(user.getUserId())).withRel("questionnaires"));
        return ResponseEntity.ok(updatedUser);
    }
}
