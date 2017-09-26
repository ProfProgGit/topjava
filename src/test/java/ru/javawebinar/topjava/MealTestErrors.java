package ru.javawebinar.topjava;

import ru.javawebinar.topjava.util.exception.ErrorInfo;

import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;

public class MealTestErrors {
    private static final String urlMeal1Update = "http://localhost/rest/profile/meals/" + MEAL1_ID;
    private static final String argumentNotValid = "MethodArgumentNotValidException";
    private static final String integrityViolation = "DataIntegrityViolationException";

    public static final ErrorInfo DATETIME_NULL_ERROR = new ErrorInfo(urlMeal1Update, argumentNotValid, "dateTime must not be null");
    public static final ErrorInfo CALORIES_OUT_OF_RANGE_ERROR = new ErrorInfo(urlMeal1Update, argumentNotValid, "calories must be between 10 and 5000");
    public static final ErrorInfo DESCRIPTION_BALNK_ERROR = new ErrorInfo(urlMeal1Update, argumentNotValid, "description must not be blank");

    public static final ErrorInfo DATETIME_DUPLICATION_ERROR = new ErrorInfo(urlMeal1Update, integrityViolation, "Meal with the specified date and time already exists");
}
