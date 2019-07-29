package ru.stakhanov.voting.repository;

import ru.stakhanov.voting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @RestResource(path = "by-name")
    @Transactional(readOnly = true)
    @Modifying
    @Query("SELECT r FROM Restaurant r WHERE r.name =:name")
    List<Restaurant> getByName(@Param("name") String name);

    @Override
    @Secured("ROLE_ADMIN")
    Restaurant save(Restaurant entity);

    @Transactional
    @Secured("ROLE_ADMIN")
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);
}
