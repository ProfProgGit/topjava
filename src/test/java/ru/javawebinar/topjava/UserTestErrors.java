package ru.javawebinar.topjava;

import ru.javawebinar.topjava.util.exception.ErrorInfo;

import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class UserTestErrors {
    private static final String urlUserUpdate = "http://localhost/rest/admin/users/" + USER_ID;
    private static final String argumentNotValid = "MethodArgumentNotValidException";
    private static final String integrityViolation = "DataIntegrityViolationException";

    public static final ErrorInfo EMAIL_BAD_FORMAT_ERROR = new ErrorInfo(urlUserUpdate, argumentNotValid, "email must be a well-formed email address");
    public static final ErrorInfo PASSWORD_BLANK_ERROR = new ErrorInfo(urlUserUpdate, argumentNotValid, "password must not be blank");
    public static final ErrorInfo NAME_BLANK_ERROR = new ErrorInfo(urlUserUpdate, argumentNotValid, "name must not be blank");
    public static final ErrorInfo CALORIES_OUT_OF_RANGE_ERROR = new ErrorInfo(urlUserUpdate, argumentNotValid, "caloriesPerDay must be between 10 and 10000");

    public static final ErrorInfo DUPLICATE_EMAIL_ERROR = new ErrorInfo(urlUserUpdate, integrityViolation, "User with this email already exists");
}
