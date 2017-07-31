package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

public class MealTestData {
    public static final int SEQUENCE_START_ID = 100000;

    public static final int ADMIN_ID = 1;
    public static final int USER_ID = 2;

    public static final int USER_MEALS_INITIAL_COUNT = 4;

    public static final int USER_MEAL_ID_1003 = 1003;
    public static final int USER_MEAL_ID_1004 = 1004;
    public static final int USER_MEAL_ID_1005 = 1005;
    public static final int USER_MEAL_ID_1006 = 1006;

    public static final int NONEXISTENT_MEAL_ID = 1001001;

    public static final Meal USER_MEAL_1003 = new Meal(USER_MEAL_ID_1003,
            LocalDateTime.of(2017, 7, 27, 15, 0),
            "Полдник юзера",
            200);

    public static final Meal USER_MEAL_1004 = new Meal(USER_MEAL_ID_1004,
            LocalDateTime.of(2017, 7, 25, 21, 0),
            "Ужин юзера",
            1400);

    public static final Meal USER_MEAL_1005 = new Meal(USER_MEAL_ID_1005,
            LocalDateTime.of(2017, 7, 25, 9, 30),
            "Завтрак юзера",
            700);

    public static final Meal USER_MEAL_1006 = new Meal(USER_MEAL_ID_1006,
            LocalDateTime.of(2017, 7, 25, 9, 30),
            "2-й завтрак юзера",
            200);

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<>();
}
