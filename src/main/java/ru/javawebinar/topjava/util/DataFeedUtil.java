package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.data.MealDao;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.lang.Math.*;

import java.util.stream.IntStream;

public class DataFeedUtil {
    public static void generateMeals(int numberOfDays, MealDao mealDao) {
        LocalDateTime startDate = LocalDate.now().atStartOfDay().minusDays((long) numberOfDays);
        IntStream.range(0, numberOfDays * 3)
                .forEach(i -> {
                    int minCalories;
                    int maxCalories;
                    String description;
                    if (i % 3 == 0) {
                        minCalories = 300;
                        maxCalories = 800;
                        description = "Ужин";
                    } else if (i % 2 == 0) {
                        minCalories = 800;
                        maxCalories = 1700;
                        description = "Обед";
                    } else {
                        minCalories = 300;
                        maxCalories = 600;
                        description = "Завтрак";
                    }
                    mealDao.add(new Meal(startDate.plusDays((long) (i / 3)),
                            description,
                            randomInt(minCalories, maxCalories)));
                });
    }

    public static List<Meal> generateMeals() {
        return Arrays.asList(
                new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(2, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(3, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(4, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(5, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(6, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
    }

    private static int randomInt(int minNumber, int maxNumber) {
        return (int) (round(random() * (double) ((long) maxNumber - (long) minNumber)) + minNumber);
    }
}
