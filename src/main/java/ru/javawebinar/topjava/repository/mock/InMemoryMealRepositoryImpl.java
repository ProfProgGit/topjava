package ru.javawebinar.topjava.repository.mock;


import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
        MealsUtil.MEALS.forEach(meal -> this.save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} by userId={}", meal, userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            return repository.put(meal.getId(), meal);
        } else {
            return  this.get(meal.getId(), userId) == null || repository.replace(meal.getId(), meal) == null ?
                    null : meal;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete id={} by userId={}", id, userId);
        return this.get(id, userId) != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get id={} by userId={}", id, userId);
        Meal meal = repository.get(id);
        return meal != null && meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getFiltered for userId={} && date>={} && date<={}", userId, startDate, endDate);

        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId && DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed()
                        .thenComparing(Comparator.comparing(Meal::getId)))
                .collect(Collectors.toList());
    }

}

