package com.example.cmpt276.upsolve.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import com.example.cmpt276.upsolve.models.User;
import com.example.cmpt276.upsolve.models.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User regularUser;
    private User adminUser;

    @BeforeEach
    public void setup() {
        regularUser = new User("user1", "email@domain.com", "pass1");
        regularUser.setUserRole("USER");

        adminUser = new User("admin", "admin@domain.com", "adminpass");
        adminUser.setUserRole("ADMIN");
    }

    @Test
    public void testLoginSuccessRegularUser() throws Exception {
        // Mock both repository calls
        Mockito.when(userRepository.findByUserName("user1")).thenReturn(Arrays.asList(regularUser));
        Mockito.when(userRepository.findByUserNameAndUserPassword("user1", "pass1"))
               .thenReturn(Arrays.asList(regularUser));

        mockMvc.perform(post("/login")
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .param("userName", "user1")
                .param("userPassword", "pass1"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testLoginSuccessAdmin() throws Exception {
        Mockito.when(userRepository.findByUserName("admin")).thenReturn(Arrays.asList(adminUser));
        Mockito.when(userRepository.findByUserNameAndUserPassword("admin", "adminpass"))
               .thenReturn(Arrays.asList(adminUser));
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(regularUser, adminUser));

        mockMvc.perform(post("/login")
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .param("userName", "admin")
                .param("userPassword", "adminpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin_dashboard"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    public void testLoginUsernameNotFound() throws Exception {
        Mockito.when(userRepository.findByUserName("unknown")).thenReturn(Arrays.asList());

        mockMvc.perform(post("/login")
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .param("userName", "unknown")
                .param("userPassword", "pass"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    public void testLoginIncorrectPassword() throws Exception {
        Mockito.when(userRepository.findByUserName("user1")).thenReturn(Arrays.asList(regularUser));
        Mockito.when(userRepository.findByUserNameAndUserPassword("user1", "wrongpass"))
               .thenReturn(Arrays.asList());

        mockMvc.perform(post("/login")
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .param("userName", "user1")
                .param("userPassword", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        Mockito.when(userRepository.findByUserName("newuser")).thenReturn(Arrays.asList());

        mockMvc.perform(post("/register")
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .param("userName", "newuser")
                .param("userPassword", "newpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testRegisterDuplicateUsername() throws Exception {
        Mockito.when(userRepository.findByUserName("user1")).thenReturn(Arrays.asList(regularUser));

        mockMvc.perform(post("/register")
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .param("userName", "user1")
                .param("userPassword", "pass"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}