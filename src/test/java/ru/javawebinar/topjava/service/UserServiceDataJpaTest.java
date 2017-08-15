package ru.javawebinar.topjava.service;


import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;


@ActiveProfiles("datajpa")
public class UserServiceDataJpaTest extends AbstractUserServiceTest {
    @Test
    public void testGetWithMeals() throws Exception {
        User user = service.getWithMeals(UserTestData.USER_ID);
        UserTestData.MATCHER.assertEquals(UserTestData.USER, user);
        MealTestData.MATCHER.assertCollectionEquals(MealTestData.MEALS, user.getMeals());
    }
}
