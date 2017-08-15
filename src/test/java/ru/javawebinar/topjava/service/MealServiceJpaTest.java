package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("jpa")
public class MealServiceJpaTest extends AbstractMealServiceTest {
    @AfterClass
    public static void printResult() {
        AbstractMealServiceTest.printResult(MealServiceJpaTest.class.getName());
    }

    @Override
    protected void appendToReport(String report) {
        appendToReportByClass(getClass().getName(), report);
    }
}
