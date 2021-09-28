package com.example.springcrudapicheckpoint;

import com.example.springcrudapicheckpoint.Model.User;
import com.example.springcrudapicheckpoint.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository repository;

    @Test
    @Transactional
    @Rollback
    public void canGetAllUsers() throws Exception{
        User testUser = new User();
        testUser.setEmail("bob@example.com");
        testUser.setPassword("password");

        this.repository.save(testUser);

        this.mvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email", is("bob@example.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void canCreateAUser() throws Exception{
        User testUser = new User();
        testUser.setEmail("bob@example.com");
        testUser.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testUser);

        this.mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email", is("bob@example.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void canGetUsersById() throws Exception{
        User testUser = new User();
        testUser.setEmail("bob@example.com");
        testUser.setPassword("password");

        String path = String.format("/users/%s", this.repository.save(testUser).getId());

        this.mvc.perform(get(path))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email", is("bob@example.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void canUpdateUser() throws Exception{
        User testUser = new User();
        testUser.setEmail("bob@example.com");
        testUser.setPassword("password");
        String path = String.format("/users/%s", this.repository.save(testUser).getId());

        User testUpdateUser = new User();
        testUpdateUser.setEmail("bob@yahoo.com");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testUpdateUser);

        this.mvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email", is("bob@yahoo.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void canDeleteUserById() throws Exception{
        User testUser = new User();
        testUser.setEmail("bob@example.com");
        testUser.setPassword("password");

        String path = String.format("/users/%s", this.repository.save(testUser).getId());

        this.mvc.perform(delete(path))
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @Transactional
    @Rollback
    public void canAuthenticateUser() throws Exception{
        User testUser = new User();
        testUser.setEmail("bob@example.com");
        testUser.setPassword("password");
        this.repository.save(testUser);

        String json = "{\n" +
                "   \"email\":\"bob@example.com\",\n" +
                "   \"password\":\"password\"\n" +
                "}";

        this.mvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.user.id").exists())
                .andExpect(jsonPath("$.authenticated", is(true)));
    }
}
