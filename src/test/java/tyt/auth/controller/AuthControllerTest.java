package tyt.auth.controller;

import tyt.auth.model.dto.AuthRequest;
import tyt.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthenticationProvider authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return OK status and token when login is successful")
    void successfulLogin() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken(any())).thenReturn("token");

        ResponseEntity<String> response = authController.login(new AuthRequest("email", "password"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED status when login fails")
    void unsuccessfulLogin() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(auth);

        ResponseEntity<String> response = authController.login(new AuthRequest("email", "password"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password", response.getBody());
    }
}