package tyt.auth.service;

import tyt.auth.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Service class for handling JWT (JSON Web Token) operations.
 */
@Service
public class JwtService {

    // Secret key for signing the JWT
    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    // Expiration time for the JWT
    @Value("${spring.security.jwt.expiration}")
    private long expiration;

    /**
     * Generates a JWT for a given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT.
     */
    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .claim("id", user.getId())
                .claim("roles", user.getRoles())
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a given JWT against a given email.
     *
     * @param token The JWT to validate.
     * @param email The email to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token, String email) {
        final String userEmail = extractClaim(token, Claims::getSubject);
        return (userEmail.equals(email) && !isTokenExpired(token));
    }

    /**
     * Generates a signing key from the secret key.
     *
     * @return The generated signing key.
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Checks if a given JWT is expired.
     *
     * @param token The JWT to check.
     * @return True if the token is expired, false otherwise.
     */
    Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Extracts the user email from a given JWT.
     *
     * @param token The JWT to extract from.
     * @return The extracted user email.
     */
    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from a given JWT using a given claims' resolver.
     *
     * @param token          The JWT to extract from.
     * @param claimsResolver The claims resolver to use for extraction.
     * @return The extracted claim.
     */
    protected <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}