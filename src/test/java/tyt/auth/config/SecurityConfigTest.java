package tyt.auth.config;

import tyt.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private UserService userService;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return a valid PasswordEncoder")
    void validPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    @DisplayName("Should return a valid AuthenticationProvider")
    void validAuthenticationProvider() {
        AuthenticationProvider provider = securityConfig.authenticationProvider();
        assertNotNull(provider);
    }

@Test
@DisplayName("Should configure HttpSecurity correctly and return a valid SecurityFilterChain")
void validFilterChain() throws Exception {
    HttpSecurity httpSecurityMock = mock(HttpSecurity.class);
    DefaultSecurityFilterChain filterChainMock = mock(DefaultSecurityFilterChain.class);

    when(httpSecurityMock.csrf(any())).thenReturn(httpSecurityMock);
    when(httpSecurityMock.authorizeHttpRequests(any())).thenReturn(httpSecurityMock);
    when(httpSecurityMock.sessionManagement(any())).thenReturn(httpSecurityMock); // Add this line
    when(httpSecurityMock.addFilterBefore(any(), any())).thenReturn(httpSecurityMock);
    when(httpSecurityMock.authenticationProvider(any())).thenReturn(httpSecurityMock);
    when(httpSecurityMock.build()).thenReturn(filterChainMock);

    SecurityFilterChain filterChain = securityConfig.filterChain(httpSecurityMock);

    assertNotNull(filterChain);

    verify(httpSecurityMock).csrf(any());
    verify(httpSecurityMock).authorizeHttpRequests(any());
    verify(httpSecurityMock).sessionManagement(any());
    verify(httpSecurityMock).addFilterBefore(any(JwtAuthFilter.class), eq(UsernamePasswordAuthenticationFilter.class));
    verify(httpSecurityMock).authenticationProvider(any(AuthenticationProvider.class));
    verify(httpSecurityMock).build();
}
}