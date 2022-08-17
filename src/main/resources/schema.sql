
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

CREATE TABLE `roles` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `role` varchar(80) NOT NULL UNIQUE
);


CREATE TABLE `users` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` varchar(100) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
    `gender` varchar(20) DEFAULT NULL,
    `email` varchar(255) NOT NULL UNIQUE,
    `phone_number` varchar(20) DEFAULT NULL,
  birth_day varchar(20) DEFAULT NULL,
  enabled TINYINT DEFAULT '1',
  created timestamp NOT NULL DEFAULT current_timestamp,
  updated timestamp DEFAULT current_timestamp,
  login_date timestamp NULL,
  note varchar(255) DEFAULT NULL
);

CREATE TABLE users_roles (
  `user_id` BIGINT(20) NOT NULL,
  `role_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id)
);