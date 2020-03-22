package com.wirvsvirus.backend.model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class Users extends RepresentationModel<Users> {
    private List<User> users;
}
