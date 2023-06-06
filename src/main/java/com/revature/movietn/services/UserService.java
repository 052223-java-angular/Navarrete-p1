package com.revature.movietn.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.revature.movietn.entities.Role;
import com.revature.movietn.entities.User;
import com.revature.movietn.repositories.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    /**
     * Creates a new user with the given username and password credentials.
     * 
     * @param username the user's username
     * @param password the user's password
     * @return the new registers User object
     */
    public User register(String username, String email, String password) {
        // find role
        Role role = roleService.findByName("USER");

        // hash password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // create new user
        User user = new User(username, email, hashedPassword, role);

        // save user
        return userRepository.save(user);
    }

    /**
     * Checks if the username is valid. A valid username must be between 8 - 20
     * characters in length. A valid username must not have '_' or '.' at the
     * beginning or end. A valid username must not have '__', '_.', '._' or '..'.
     * Allowed characers are [a-zA-z0-9._].
     * 
     * @param username the username to validate
     * @return true if username is valid, false otherwise
     */
    public boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    /**
     * Checks against the database to see if a user with given username is in the
     * database.
     * 
     * @param username the username to check against the database
     * @return true if user with given username is not found in db, otherwise false
     */
    public boolean isUniqueUsername(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Checks if email address is valid.
     * 
     * @param email the email to validate
     * @return true if email is valid, false otherwise.
     */
    public boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * Checks if the password is valid. A valid password must at least 8 characters
     * long (no constraints on max length). A valid password must have at least one
     * digit, lowercase letter, uppercase letter, and special character
     * ([@#$%^&+=]).A valid password must not have any whitespaces.
     * 
     * @param password the password name to validate
     * @return true if password is valid, otherwise false
     */
    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

    /**
     * Checks if password and password confirmation match.
     * 
     * @param password        the initial password
     * @param confirmPassword the confirmation password
     * @return true if passwords match, otherwise false
     */
    public boolean isSamePassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

}
