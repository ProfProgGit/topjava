package ru.javawebinar.topjava.repository;


import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.time.LocalDate;


public interface MealRepository {
    // null if not found
    Meal save(Meal Meal, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Meal get(int id, int userId);

    List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate);

}
