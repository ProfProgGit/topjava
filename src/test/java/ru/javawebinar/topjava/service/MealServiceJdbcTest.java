package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("jdbc")
public class MealServiceJdbcTest extends AbstractMealServiceTest {
    @AfterClass
    public static void printResult() {
        AbstractMealServiceTest.printResult(MealServiceJdbcTest.class.getName());
    }

    @Override
    protected void appendToReport(String report) {
        appendToReportByClass(getClass().getName(), report);
    }
}
