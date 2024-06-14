package tyt.auth.repository;

import tyt.auth.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserRepositoryTest {

    @Mock
    UserRepository userRepositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByEmailReturnsUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");

        when(userRepositoryMock.findByEmail(anyString())).thenReturn(userEntity);

        UserEntity result = userRepositoryMock.findByEmail("test@test.com");

        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void findByEmailReturnsNullWhenNoUserEntityFound() {
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(null);

        UserEntity result = userRepositoryMock.findByEmail("test@test.com");

        assertNull(result);
    }
}