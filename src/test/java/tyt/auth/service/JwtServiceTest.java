package tyt.auth.service;

import tyt.auth.model.Role;
import tyt.auth.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "TNDbOVzVNAzfbxBaqEkGRuY/bV5mMPjv5ZNL0kWuqMc=");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000); // 1 hour
    }

    @Test
    void generateToken() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        Set<Role> roles = EnumSet.of(Role.ADMIN);
        user.setRoles(roles);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJhbGciOiJIUzI1NiJ9"));
    }

    @Test
    void validateToken_validToken() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        Set<Role> roles = EnumSet.of(Role.ADMIN);
        user.setRoles(roles);

        String token = jwtService.generateToken(user);

        Boolean isValid = jwtService.validateToken(token, "test@example.com");

        assertTrue(isValid);
    }

    @Test
    void validateToken_invalidToken_wrongEmail() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        Set<Role> roles = EnumSet.of(Role.ADMIN);
        user.setRoles(roles);

        String token = jwtService.generateToken(user);

        Boolean isValid = jwtService.validateToken(token, "wrong@example.com");

        assertFalse(isValid);
    }

    @Test
    void validateToken_invalidToken_expired() {
        JwtService testService = Mockito.spy(jwtService);
        Mockito.doReturn("2002-01-19T00:00:00.000+00:00")
                .when(testService)
                .extractClaim(Mockito.anyString(), Mockito.any());

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        Set<Role> roles = EnumSet.of(Role.ADMIN);
        user.setRoles(roles);

        String token = testService.generateToken(user);

        Boolean isValid = testService.validateToken(token, "test@example.com");

        assertFalse(isValid);
    }

    @Test
    void extractUserEmail() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        Set<Role> roles = EnumSet.of(Role.ADMIN);
        user.setRoles(roles);

        String token = jwtService.generateToken(user);

        String email = jwtService.extractUserEmail(token);

        assertEquals("test@example.com", email);
    }

    @Test
    void isTokenExpired() {
        JwtService testService = Mockito.spy(jwtService);
        Mockito.doReturn(new Date(0)).when(testService).extractClaim(Mockito.anyString(), Mockito.any());

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        Set<Role> roles = EnumSet.of(Role.ADMIN);
        user.setRoles(roles);

        String token = testService.generateToken(user);

        Boolean isExpired = testService.isTokenExpired(token);

        assertTrue(isExpired);
    }


}