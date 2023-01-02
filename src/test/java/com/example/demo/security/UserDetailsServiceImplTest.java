package com.example.demo.security;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl userDetailsService;

    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        userDetailsService = new UserDetailsServiceImpl();
        TestUtils.injectObjects(userDetailsService, "userRepository", userRepository);
    }

    @Test
    public void loadUserByUsername_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("aaa");
        user.setPassword("abcd");
        when(userRepository.findByUsername("aaa")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("aaa");
        assertEquals("aaa", userDetails.getUsername());
    }

    @Test
    public void loadUserByUsername_notfound() throws Exception {
        when(userRepository.findByUsername("aaa")).thenReturn(null);

        try {
            userDetailsService.loadUserByUsername("aaa");
        } catch (UsernameNotFoundException e) {

        }

    }

}
