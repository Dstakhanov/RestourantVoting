package ru.stakhanov.voting.repository;

import ru.stakhanov.voting.model.Menu;
import ru.stakhanov.voting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Override
    @Secured("ROLE_ADMIN")
    Menu save(Menu entity);

    @Override
    @Secured("ROLE_ADMIN")
    void deleteById(Integer id);

    @RestResource(path = "by-restaurant")
    @Transactional(readOnly = true)
    @Modifying
    @Query("SELECT m FROM Menu m WHERE m.restaurant=:restaurant")
    List<Menu> findByRestaurant(@Param("restaurant") Restaurant restaurant);


    @RestResource(path = "by-date")
    @Transactional(readOnly = true)
    @Modifying
    @Query("SELECT m FROM Menu m WHERE m.date=:date")
    List<Menu> findByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);
}
