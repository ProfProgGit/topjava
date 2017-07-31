package ru.javawebinar.topjava.service;


import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;


@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringRunner.class)
public class MealServiceTest {
    @Autowired
    private MealService testedService;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testSave() throws Exception {
        Meal meal = new Meal(LocalDateTime.of(2017, 7, 28, 10, 0), "новый завтрак User", 100);
        Meal savedMeal = testedService.save(meal, USER_ID);
        meal.setId(SEQUENCE_START_ID + 1);
        MATCHER.assertEquals(meal, savedMeal);
    }

    @Test
    public void testGet() throws Exception {
        MATCHER.assertEquals(USER_MEAL_1003, testedService.get(USER_MEAL_ID_1003, USER_ID));
    }

    @Test
    public void testDelete() throws Exception {
        testedService.delete(USER_MEAL_ID_1006, USER_ID);
        boolean deleteVerified = false;
        try {
            testedService.get(USER_MEAL_ID_1006, USER_ID);
        } catch (NotFoundException nfe) {
            deleteVerified = true;
        }
        Assert.assertTrue("The deleted meal still exists", deleteVerified);
        testedService.delete(USER_MEAL_ID_1004, USER_ID);

        testedService.delete(USER_MEAL_ID_1003, USER_ID);
        testedService.delete(USER_MEAL_ID_1005, USER_ID);
        Assert.assertTrue("Unexpected number of meals in user meal list",
                testedService.getAll(USER_ID).size() == USER_MEALS_INITIAL_COUNT - 4);
    }

    @Test
    public void testGetBetweenDates() throws Exception {
        Collection<Meal> filtered = testedService.getBetweenDates(LocalDate.of(2017, 7, 25), LocalDate.of(2017, 7, 25), USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(USER_MEAL_1004, USER_MEAL_1006, USER_MEAL_1005), filtered);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        Collection<Meal> filtered = testedService.getBetweenDateTimes(LocalDateTime.of(2017, 7, 25, 9, 30),
                LocalDateTime.of(2017, 7, 25, 9, 30),
                USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(USER_MEAL_1006, USER_MEAL_1005), filtered);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Meal> all = testedService.getAll(USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(USER_MEAL_1003,
                USER_MEAL_1004,
                USER_MEAL_1006,
                USER_MEAL_1005), all);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(USER_MEAL_ID_1003, LocalDateTime.of(2017, 6, 1, 12, 0), "приём пищи", 900);
        testedService.update(updated, USER_ID);
        MATCHER.assertEquals(updated, testedService.get(USER_MEAL_ID_1003, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testInvalidUserIdUpdate() throws Exception {
        Meal updated = new Meal(USER_MEAL_ID_1003, LocalDateTime.of(2017, 6, 1, 12, 0), "приём пищи", 900);
        testedService.update(updated, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testInvalidUserIdDelete() throws Exception {
        testedService.delete(USER_MEAL_ID_1006, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testInvalidUserIdGet() throws Exception {
        testedService.get(USER_MEAL_ID_1003, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testInvalidMealIdGet() throws Exception {
        testedService.get(NONEXISTENT_MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testInvalidMealIdDelete() throws Exception {
        testedService.delete(NONEXISTENT_MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testInvalidMealIdUpdate() throws Exception {
        Meal updated = new Meal(NONEXISTENT_MEAL_ID, LocalDateTime.of(2017, 6, 1, 12, 0), "приём пищи", 900);
        testedService.update(updated, USER_ID);
    }

}