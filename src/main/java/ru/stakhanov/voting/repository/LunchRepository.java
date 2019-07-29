package ru.stakhanov.voting.repository;

import ru.stakhanov.voting.model.Lunch;
import ru.stakhanov.voting.model.Menu;
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

public interface LunchRepository extends JpaRepository<Lunch, Integer> {

    @Override
    @Secured("ROLE_ADMIN")
    Lunch save(Lunch entity);

    @Override
    @Secured("ROLE_ADMIN")
    void deleteById(Integer id);

    @Transactional(readOnly = true)
    @RestResource(path = "by-menu")
    @Modifying
    @Query("SELECT l FROM Lunch l WHERE l.menu=:menu")
    List<Menu> getByMenu(@Param("menu") Menu menu);

    @RestResource(path = "by-date")
    @Transactional(readOnly = true)
    @Modifying
    @Query("SELECT l FROM Lunch l WHERE l.menu.date=:date")
    List<Lunch> getByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);


}
