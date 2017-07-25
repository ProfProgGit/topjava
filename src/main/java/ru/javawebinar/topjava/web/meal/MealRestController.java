package ru.javawebinar.topjava.web.meal;


import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;


@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return getFiltered(null, null, null, null);
    }

    public List<MealWithExceed> getFiltered(LocalDate startDate,
                                            LocalDate endDate,
                                            LocalTime startTime,
                                            LocalTime endTime) {
        log.info("getFiltered by date>={} && date<={} && time>={} && time<={}", startDate, endDate, startTime, endTime);
        return MealsUtil.getFilteredWithExceeded(
                service.getFiltered(AuthorizedUser.id(),
                        startDate == null ? LocalDate.MIN : startDate,
                        endDate == null ? LocalDate.MAX : endDate),
                startTime == null ? LocalTime.MIN : startTime,
                endTime == null ? LocalTime.MAX : endTime,
                MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(int id) {
        log.info("get id={}", id);
        return service.get(id, AuthorizedUser.id());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        meal.setUserId(AuthorizedUser.id());
        return service.create(meal);
    }

    public void delete(int id) {
        log.info("delete id={}", id);
        service.delete(id, AuthorizedUser.id());
    }

    public void update(Meal meal) {
        log.info("update {}", meal);
        meal.setUserId(AuthorizedUser.id());
        service.update(meal, AuthorizedUser.id());
    }
}