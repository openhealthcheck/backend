package com.wirvsvirus.backend.repository;

import com.wirvsvirus.backend.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
