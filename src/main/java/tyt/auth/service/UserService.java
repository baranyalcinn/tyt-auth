package tyt.auth.service;

import tyt.auth.model.UserEntity;
import tyt.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserService is a service class that implements UserDetailsService interface.
 * It is responsible for loading user-specific data.
 */
@Service
public class UserService implements UserDetailsService {

    // UserRepository instance for interacting with the database.
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     * @param userRepository UserRepository instance for database interaction.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Overridden method from UserDetailsService interface.
     * It loads the user data based on the provided email.
     * @param email The email of the user.
     * @return UserDetails instance containing user data.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userEntity;
    }
}