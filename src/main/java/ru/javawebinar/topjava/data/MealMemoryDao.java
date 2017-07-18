package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MealMemoryDao implements MealDao {
    private int lastGeneratedId = 0;
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    public synchronized void add(Meal meal) {
        meal.setId(++lastGeneratedId);
        mealMap.put(lastGeneratedId, meal);
    }

    public Meal read(int id) {
        return mealMap.get(id);
    }

    public List<Meal> readAll() {
        return new ArrayList<>(mealMap.values());
    }

    public void update(Meal meal) {
        mealMap.replace(meal.getId(), meal);
    }

    public void delete(int id) {
        mealMap.remove(id);
    }
}
