package tyt.auth.config;

import tyt.auth.service.JwtService;
import tyt.auth.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should forward the request without modification when request URI is /auth/login")
    void requestUriIsLogin() throws Exception {
        when(request.getRequestURI()).thenReturn("/auth/login");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should forward the request without modification when Authorization header is missing")
    void missingAuthorizationHeader() throws Exception {
        when(request.getRequestURI()).thenReturn("/some/other/uri");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should set authentication when JWT is valid")
    void validJwt() throws Exception {
        when(request.getRequestURI()).thenReturn("/some/other/uri");
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserEmail("token")).thenReturn("email");
        when(userService.loadUserByUsername("email")).thenReturn(mock(UserDetails.class));
        when(jwtService.validateToken("token", "email")).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should not set authentication when JWT is invalid")
    void invalidJwt() throws Exception {
        when(request.getRequestURI()).thenReturn("/some/other/uri");
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserEmail("token")).thenReturn("email");
        when(userService.loadUserByUsername("email")).thenReturn(mock(UserDetails.class));
        when(jwtService.validateToken("token", "email")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}