package ru.stakhanov.voting;

import ru.stakhanov.voting.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import static java.util.Objects.requireNonNull;


public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    static final long serialVersionUID = 1L;

    private int id;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.id = user.getId();
    }

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object user = auth.getPrincipal();
        return (user instanceof AuthorizedUser) ? (AuthorizedUser) user : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public static int id() {
        return get().id;
    }

    @Override
    public String toString() {
        return "User (" + id + ',' + getUsername() + ")";
    }
}