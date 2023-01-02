package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> res = userController.createUser(r);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());

        User u = res.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void createUserWrongPassword() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPasswor");

        final ResponseEntity<User> res = userController.createUser(r);
        assertNotNull(res);
        assertEquals(400, res.getStatusCodeValue());

    }

    @Test
    public void createUserWrongLengthPassword() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testP");
        r.setConfirmPassword("testP");

        final ResponseEntity<User> res = userController.createUser(r);
        assertNotNull(res);
        assertEquals(400, res.getStatusCodeValue());
    }

    @Test
    public void findByIdTest() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        final ResponseEntity<User> res = userController.findById(1l);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());

        User u = res.getBody();
        assertNotNull(u);
        assertEquals(1, u.getId());
    }

    @Test
    public void findByUserNameTest() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("aaa");
        when(userRepository.findByUsername("aaa")).thenReturn(user);

        final ResponseEntity<User> res = userController.findByUserName("aaa");
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());

        User u = res.getBody();
        assertNotNull(u);
        assertEquals(1, u.getId());
        assertEquals("aaa", u.getUsername());
    }
}
