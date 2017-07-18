package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public class MealMemoryDao implements MealDao {
    private final MealMemoryStorage storage;

    public MealMemoryDao(MealMemoryStorage storage) {
        this.storage = storage;
    }

    public int add(LocalDateTime dateTime, String description, int calories) {
        return storage.add(dateTime, description, calories);
    }

    public Meal read(int id) {
        return storage.read(id);
    }

    public List<Meal> readAll() {
        return storage.readAll();
    }

    public void update(Meal meal) {
        storage.update(meal);
    }

    public void delete(int id) {
        storage.delete(id);
    }
}
