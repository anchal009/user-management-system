
DELETE FROM users_roles;
DELETE FROM roles;

DELETE FROM users;

INSERT INTO roles(id, role) VALUES (1, 'ADMINISTRATOR');


INSERT INTO users(id, username, password, first_name, last_name, gender, email, enabled) VALUES (1, 'anchal', 'cGFzc3dvcmQ=', 'Anchal', 'Agrawal', 'Male', 'anchal@gmail.com', 1);
INSERT INTO users(id, username, password, first_name, last_name, gender, email, enabled) VALUES (2, 'kim', 'YWRtaW4=', 'Kim', 'Mohn', 'Male', 'kim@gmail.com', 1);

INSERT INTO users_roles(user_id, role_id) VALUES (1, 1);
INSERT INTO users_roles(user_id, role_id) VALUES (2, 1);

