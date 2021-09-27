package com.example.springcrudapicheckpoint.Controller;

import com.example.springcrudapicheckpoint.Model.User;
import com.example.springcrudapicheckpoint.Repository.UserRepository;
import com.example.springcrudapicheckpoint.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    @JsonView(Views.UserView.class)
    public Iterable<User> getAllUsers(){
        return this.repository.findAll();
    }

    @PostMapping("")
    @JsonView(Views.UserView.class)
    public User createUser(@RequestBody User user){
        return this.repository.save(user);
    }

    @GetMapping("/{id}")
    @JsonView(Views.UserView.class)
    public User getUserById(@PathVariable long id){
        if (this.repository.findById(id).isPresent()){
            return this.repository.findById(id).get();
        }
        else return null;
    }

    @PatchMapping("/{id}")
    @JsonView(Views.UserView.class)
    public User updateUser(@PathVariable long id, @RequestBody User user){
        if (this.repository.findById(id).isPresent()){
            if(user.getEmail() != null) {
                this.repository.findById(id).get().setEmail(user.getEmail());
                //System.out.println("Set new email to " + this.repository.findById(id).get().getEmail());
            }
            if(user.getPassword() != null) {
                this.repository.findById(id).get().setPassword(user.getPassword());
                //System.out.println("Set new password to " + this.repository.findById(id).get().getPassword());
            }
            return this.repository.save(this.repository.findById(id).get());
        }
        else return null;
    }

    @DeleteMapping("/{id}")
    public Map<String, Long> deleteUser(@PathVariable long id){
        this.repository.deleteById(id);
        HashMap<String, Long> result = new HashMap<>();
        result.put("count", this.repository.count());

        return result;
    }

    @PostMapping("/authenticate")
    @JsonView(Views.UserView.class)
    public Map<String, Object> authenticateUser(@RequestBody User user){
        User currentUser;
        HashMap<String, Object> result = new HashMap<>();
        if (this.repository.findByEmail(user.getEmail()).isPresent()){
            currentUser = this.repository.findByEmail(user.getEmail()).get();
            if (currentUser.getPassword().equals(user.getPassword())){
                result.put("authenticated", true);
                result.put("user", currentUser);
            }else{
                result.put("authenticated", false);
            }
            return result;
        }else return null;
    }
}
