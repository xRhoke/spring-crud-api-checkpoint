package com.example.springcrudapicheckpoint.Repository;

import com.example.springcrudapicheckpoint.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Map;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findByEmail(String email);
}
