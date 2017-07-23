package ru.javawebinar.topjava.repository.mock;


import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal->this.save(meal, 0)); // TODO: null is not good! but 0 is not good either!
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for userId={}", meal, userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        } else if ( meal.getUserId() != userId ) { // TODO refactor when understand where userId should be set (!)
            // TODO may be better using checkUserId(Meal meal, int userId); method instead?
            return null;
        }
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id );
        Meal meal = repository.get(id);
        log.info("delete id={} meal={} userId={}", id, meal, userId );
        return checkUserId(meal, userId) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Meal meal = repository.get(id);
        return checkUserId(meal, userId) ? meal : null;
    }


    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for user id={}", userId);

        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()); // TODO what returns if no items (null or empty list?)
    }

    private boolean checkUserId(Meal meal, int userId){
        return meal!= null && meal.getUserId()== userId;
    }
}

