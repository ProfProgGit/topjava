package ru.javawebinar.topjava;


import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    protected final static Logger log = LoggerFactory.getLogger(SpringMain.class);

    public static void main(String[] args) {



        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            log.info("Bean definition names: {} ", Arrays.toString(appCtx.getBeanDefinitionNames()));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(1, LocalDateTime.now(), "Просто поел", 3000));
            log.info("getAll output: {}" , Arrays.toString(mealRestController.getAll().toArray()));
        }
    }
}
