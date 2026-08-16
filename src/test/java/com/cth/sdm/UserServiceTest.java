package com.cth.sdm;

import com.cth.sdm.entity.User;
import com.cth.sdm.repository.UserRepository;
import com.cth.sdm.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUserAndLockToggle() {
        User user = userService.createUser("testuser", "password123", "test@cth.com", "+1234567890", "MAKER", "admin");
        assertNotNull(user.getId());
        assertEquals("testuser", user.getUsername());
        assertFalse(user.isLocked());

        userService.setUserLockStatus("testuser", true, "admin");
        User updated = userRepository.findByUsername("testuser").orElseThrow();
        assertTrue(updated.isLocked());
    }
}
