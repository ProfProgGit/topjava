package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal, 0); // TODO: null is not good! but 0 is not good either!
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFound(repository.delete(id, userId), id, userId);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFound(repository.get(id, userId), id, userId);
    }

    @Override
    public void update(Meal meal, int userId) {
        checkNotFound(repository.save(meal, userId), meal.getId(), userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }
}