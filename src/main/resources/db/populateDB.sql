DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

-- populate users & user_roles

INSERT INTO users (id, name, email, password)
VALUES (1, 'Admin', 'admin@gmail.com', 'admin'),
  (2, 'User', 'user@yandex.ru', 'password');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_ADMIN', 1),
  ('ROLE_USER', 2);

-- populate meals
INSERT INTO meals (id, user_id, date_time, description, calories)
VALUES
  (1001, 1, '2017-07-29 10:00', 'Завтрак админа', 1500),
  (1002, 1, '2017-07-26 13:00', 'Обед админа', 1700),
  (1003, 2, '2017-07-27 15:00', 'Полдник юзера', 200),
  (1004, 2, '2017-07-25 21:00', 'Ужин юзера', 1400),
  (1005, 2, '2017-07-25 9:30', 'Завтрак юзера', 700),
  (1006, 2, '2017-07-25 9:30', '2-й завтрак юзера', 200);




