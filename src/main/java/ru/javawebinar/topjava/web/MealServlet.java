package ru.javawebinar.topjava.web;


import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

import java.time.format.DateTimeParseException;


public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                null,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        if (meal.isNew()) {
            mealRestController.create(meal);
        } else {
            mealRestController.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        HttpSession session = request.getSession();
        switch (action == null ? "all" : action) {
            case "delete":
                mealRestController.delete(getId(request));
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
            case "filter":
                Arrays.stream(new String[]{"startDate", "endDate", "startTime", "endTime"})
                        .forEach(name -> session.setAttribute(name, request.getParameter(name)));
            case "all":
            default:
                request.setAttribute("meals", mealRestController.getFiltered(
                        tryToParseDate((String) session.getAttribute("startDate")),
                        tryToParseDate((String) session.getAttribute("endDate")),
                        tryToParseTime((String) session.getAttribute("startTime")),
                        tryToParseTime((String) session.getAttribute("endTime"))
                ));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    public void destroy() {
        appCtx.close();
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    private LocalDate tryToParseDate(String dateString) {
        try {
            return LocalDate.parse(Objects.requireNonNull(dateString));
        } catch (NullPointerException | DateTimeParseException e) {
            return null;
        }
    }

    private LocalTime tryToParseTime(String timeString) {
        try {
            return LocalTime.parse(Objects.requireNonNull(timeString));
        } catch (NullPointerException | DateTimeParseException e) {
            return null;
        }
    }
}
