package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class MealController extends AbstractMealController {

    @GetMapping
    public String getAll(HttpServletRequest request, Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @PostMapping
    public String filter(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        super.delete(getIdFromRequest(request));
        return "redirect:/meals";
    }

    @GetMapping("/mealForm")
    public String getChangeForm(HttpServletRequest request, Model model) {
        Meal meal;
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        } else {
            meal = super.get(getIdFromRequest(request));
        }
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request) {
        super.create(getMealFromRequest(request));
        return "redirect:/meals";
    }

    @PostMapping("/update")
    public String update(HttpServletRequest request) {
        super.update(getMealFromRequest(request), getIdFromRequest(request));
        return "redirect:/meals";
    }


    private int getIdFromRequest(HttpServletRequest request) {
        String id = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(id);
    }

    private Meal getMealFromRequest(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
