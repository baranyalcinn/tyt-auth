package tyt.auth.service;

import tyt.auth.model.UserEntity;
import tyt.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserExists() {
        UserEntity userEntity = new UserEntity();
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDetails result = userService.loadUserByUsername("baran@gmail.com");

        assertEquals(userEntity, result);
    }

    @Test
    void loadUserByUsernameThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("baran@gmail.com"));
    }
}