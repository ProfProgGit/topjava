package ru.javawebinar.topjava;

import org.springframework.test.context.ActiveProfilesResolver;
import org.springframework.test.context.support.DefaultActiveProfilesResolver;

import java.util.Arrays;

//http://stackoverflow.com/questions/23871255/spring-profiles-simple-example-of-activeprofilesresolver
public class ActiveDbProfileResolver implements ActiveProfilesResolver {

    private final DefaultActiveProfilesResolver defaultActiveProfilesResolver = new DefaultActiveProfilesResolver();

    @Override
    public String[] resolve(Class<?> testClass) {
        String[] declarativeProfiles = defaultActiveProfilesResolver.resolve(testClass);
        String[] allProfiles = Arrays.copyOf(declarativeProfiles, declarativeProfiles.length + 1);
        allProfiles[allProfiles.length - 1] = Profiles.getActiveDbProfile();
        return allProfiles;
    }
}