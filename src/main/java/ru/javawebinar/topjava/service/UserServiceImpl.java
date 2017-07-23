package ru.javawebinar.topjava.service;


import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFound(repository.delete(id), id);
    }

    @Override
    public User get(int id) throws NotFoundException {
        return checkNotFound(repository.get(id), id);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    public void update(User user) {
        repository.save(user);
    }
}