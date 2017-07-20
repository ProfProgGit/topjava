package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.data.MealDao;
import ru.javawebinar.topjava.data.MealMemoryDao;
import ru.javawebinar.topjava.util.*;
import ru.javawebinar.topjava.model.*;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;


import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_LIMIT = 2000;
    private static final MealDao mealDao = new MealMemoryDao();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataFeedUtil.generateMeals(10, mealDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get request processing...");

        request.setAttribute("mealsWithExceeded", MealsUtil.getFilteredWithExceeded(mealDao.readAll(), CALORIES_LIMIT));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("post request processing...");

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        int id = "add".equals(action) ? 0 : Integer.parseInt(request.getParameter("id"));
        if ("delete".equals(action)) {
            mealDao.delete(id);
        } else if ("edit".equals(action)) {
            // edit action makes fields for item with id=mealId available for update
            request.setAttribute("updatedId", id);
        } else {
            LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            if ("add".equals(action)) {
                mealDao.add(new Meal(dateTime, description, calories));
            } else { // update action
                mealDao.update(new Meal(id, dateTime, description, calories));
            }
        }
        request.setAttribute("mealsWithExceeded", MealsUtil.getFilteredWithExceeded(mealDao.readAll(), CALORIES_LIMIT));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
