package ru.stakhanov.voting.repository;

import ru.stakhanov.voting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @RestResource(path = "by-email")
    @Transactional(readOnly = true)
    @Query("SELECT u FROM User u " +
            " LEFT JOIN u.roles WHERE u.email=:email")
    User getByEmail(@Param("email") String email);

}
