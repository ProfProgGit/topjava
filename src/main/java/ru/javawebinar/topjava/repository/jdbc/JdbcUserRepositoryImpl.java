package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
            insertRoles(user);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        return (user == null) ? user : attachRoles(user);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return attachRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        return attachRoles(jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER));
    }

    private User attachRoles(User user) {
        Set<Role> roles = EnumSet.noneOf(Role.class);
        jdbcTemplate.query("SELECT role FROM user_roles WHERE user_id=?",
                new Object[]{user.getId()},
                rs -> {
                    roles.add(Role.valueOf(rs.getString("role")));
                }
        );
        if (!roles.isEmpty()) {
            user.setRoles(roles);
        }
        return user;
    }

    private List<User> attachRoles(List<User> users) {
        Map<Integer, Set<Role>> rolesSetByUserId = new HashMap<>();
        jdbcTemplate.query("SELECT user_id, role FROM user_roles",
                rs -> {
                    rolesSetByUserId
                            .computeIfAbsent(rs.getInt("user_id"), userId -> EnumSet.noneOf(Role.class))
                            .add(Role.valueOf(rs.getString("role")));
                }
        );
        users.forEach(user -> user.setRoles(rolesSetByUserId.get(user.getId())));
        return users;
    }

    private void insertRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (roles != null && !roles.isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                    roles.stream()
                            .map(role -> new Object[]{user.getId(), role.name()})
                            .collect(Collectors.toList()));
        }
    }
}
