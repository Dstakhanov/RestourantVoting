package ru.stakhanov.voting.service;
import ru.stakhanov.voting.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stakhanov.voting.repository.UserRepository;
import ru.stakhanov.voting.AuthorizedUser;


@Service("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public AuthorizedUser loadUserByUsername(final String email) {
        String lowercaseLogin = email.toLowerCase();
        log.debug("Authenticating {}", email);
        User user = userRepository.getByEmail(lowercaseLogin);
        if (user == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("User " + lowercaseLogin + " was not activated");
        }
        return new AuthorizedUser(user);
    }
}