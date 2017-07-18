package ru.javawebinar.topjava.data;


import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MealMemoryStorage {
    private int lastGeneratedId = 0;
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    int add(LocalDateTime dateTime, String description, int calories) {
        int id = generateId();
        mealMap.put(id, new Meal(id, dateTime, description, calories));
        return id;
    }

    private synchronized int generateId() {
        return ++lastGeneratedId;
    }

    Meal read(int id) {
        return mealMap.get(id);
    }

    void update(Meal value) {
        mealMap.replace(value.getId(), value);
    }

    void delete(int id) {
        mealMap.remove(id);
    }

    List<Meal> readAll() {
        return new ArrayList<>(mealMap.values());
    }

}
