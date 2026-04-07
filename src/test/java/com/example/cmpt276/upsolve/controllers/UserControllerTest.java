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
        regularUser = new User("user1", "email@domain.com", "pass1", "What is your pet's name?", "Fluffy");
        regularUser.setUserRole("USER");

        adminUser = new User("admin", "admin@domain.com", "adminpass", "What is your favorite color?", "Blue");
        adminUser.setUserRole("ADMIN");
    }

    @Test
    public void testLoginSuccessRegularUser() throws Exception {
        Mockito.when(userRepository.findByUserNameAndUserPassword("user1", "pass1"))
               .thenReturn(Arrays.asList(regularUser));

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "user1")
                .param("userPassword", "pass1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    public void testLoginSuccessAdmin() throws Exception {
        Mockito.when(userRepository.findByUserNameAndUserPassword("admin", "adminpass"))
               .thenReturn(Arrays.asList(adminUser));

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "admin")
                .param("userPassword", "adminpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_dashboard"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        Mockito.when(userRepository.findByUserNameAndUserPassword("user1", "wrong"))
               .thenReturn(Arrays.asList());

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "user1")
                .param("userPassword", "wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        Mockito.when(userRepository.findByUserName("newuser"))
               .thenReturn(Arrays.asList());

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "newuser")
                .param("userPassword", "pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testRegisterDuplicate() throws Exception {
        Mockito.when(userRepository.findByUserName("user1"))
               .thenReturn(Arrays.asList(regularUser));

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userName", "user1")
                .param("userPassword", "pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }

    @Test
    public void testIndexNoUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    public void testIndexRegularUser() throws Exception {
        mockMvc.perform(get("/")
                .sessionAttr("session_user", regularUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    public void testIndexAdminUser() throws Exception {
        mockMvc.perform(get("/")
                .sessionAttr("session_user", adminUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_dashboard"));
    }

    @Test
    public void testDashboardRequiresLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testAdminDashboardAsAdmin() throws Exception {
        Mockito.when(userRepository.findAll())
               .thenReturn(Arrays.asList(regularUser, adminUser));

        mockMvc.perform(get("/admin_dashboard")
                .sessionAttr("session_user", adminUser))
                .andExpect(status().isOk())
                .andExpect(view().name("users/admin"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}