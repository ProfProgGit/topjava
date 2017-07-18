package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {
    int add(LocalDateTime dateTime, String description, int calories);

    Meal read(int id);

    List<Meal> readAll();

    void update(Meal meal);

    void delete(int id);
}
