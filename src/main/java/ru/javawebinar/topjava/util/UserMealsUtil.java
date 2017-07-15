package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

import java.time.Month;
import java.util.*;

import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceededStreams(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceededPureStreams(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceededMoreStreams(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Objects.requireNonNull(mealList, "mealList must not be null");
        Objects.requireNonNull(startTime, "startTime must not be null");
        Objects.requireNonNull(endTime, "endTime must not be null");

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        Map<LocalDate, Integer> totalCaloriesPerDay = getTotalCaloriesPerDay(mealList);

        mealList.forEach(meal -> {
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExceedList.add(
                        new UserMealWithExceed(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                totalCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });
        return userMealWithExceedList;
    }

    private static Map<LocalDate, Integer> getTotalCaloriesPerDay(List<UserMeal> mealList) {
        Objects.requireNonNull(mealList, "mealList must not be null");

        Map<LocalDate, Integer> totalCaloriesPerDay = new HashMap<>();
        mealList.forEach(meal -> {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            totalCaloriesPerDay.put(mealDate, totalCaloriesPerDay.getOrDefault(mealDate, 0) + meal.getCalories());
        });
        return totalCaloriesPerDay;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStreams(List<UserMeal> mealList,
                                                                          LocalTime startTime,
                                                                          LocalTime endTime,
                                                                          int caloriesPerDay) {
        Map<LocalDate, Integer> daysCaloriesExceed = mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));


        return mealList.stream()
                .map(meal -> new UserMealWithExceed(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        daysCaloriesExceed.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededPureStreams(List<UserMeal> mealList,
                                                                              LocalTime startTime,
                                                                              LocalTime endTime,
                                                                              int caloriesPerDay) {
        return
                mealList.stream()
                        .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()))
                        .values()
                        .stream()
                        .collect(Collectors.partitioningBy(mealsPerDay ->
                                mealsPerDay.stream()
                                        .mapToInt(UserMeal::getCalories).sum() > caloriesPerDay))
                        .entrySet()
                        .stream()
                        .flatMap(listOfLists ->
                                listOfLists.getValue().stream()
                                        .flatMap(listOfMeals -> listOfMeals.stream()
                                                .filter(meal ->
                                                        TimeUtil.isBetween(
                                                                meal.getDateTime().toLocalTime(),
                                                                startTime,
                                                                endTime))
                                                .map(meal -> new UserMealWithExceed(
                                                        meal.getDateTime(),
                                                        meal.getDescription(),
                                                        meal.getCalories(),
                                                        listOfLists.getKey()))))
                        .collect(Collectors.toList());


    }

    public static List<UserMealWithExceed> getFilteredWithExceededMoreStreams(List<UserMeal> mealList,
                                                                              LocalTime startTime,
                                                                              LocalTime endTime,
                                                                              int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()))
                .values()
                .stream()
                .flatMap(mealsByDay -> {
                    boolean exceeds = mealsByDay.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                    return mealsByDay.stream()
                            .filter(meal -> TimeUtil.isBetween(
                                    meal.getDateTime().toLocalTime(),
                                    startTime,
                                    endTime))
                            .map(userMeal -> new UserMealWithExceed(
                                    userMeal.getDateTime(),
                                    userMeal.getDescription(),
                                    userMeal.getCalories(),
                                    exceeds));
                })
                .collect(Collectors.toList());

    }
}
