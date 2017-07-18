package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    void add(Meal meal);

    Meal read(int id);

    List<Meal> readAll();

    void update(Meal meal);

    void delete(int id);
}
