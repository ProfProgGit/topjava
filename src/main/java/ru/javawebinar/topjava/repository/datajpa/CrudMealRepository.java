package ru.javawebinar.topjava.repository.datajpa;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {


    @Transactional
    @Override
    Meal save(Meal meal);

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    Meal findOneByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserId(int userId, Sort sort);

    List<Meal> getByUserIdAndDateTimeBetween(int userId,
                                             LocalDateTime startDate,
                                             LocalDateTime endDate,
                                             Sort sort);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user u WHERE m.id = ?1 AND u.id = ?2")
    Meal getWithUser(int id, int userId);
}
