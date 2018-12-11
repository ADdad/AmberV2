/* 18 tables to drop */
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS user_warehouses;
DROP TABLE IF EXISTS warehouse_equipment;
DROP TABLE IF EXISTS request_equipment;
DROP TABLE IF EXISTS attributes_res_values;
DROP TABLE IF EXISTS reserved_values;
DROP TABLE IF EXISTS request_values;
DROP TABLE IF EXISTS request_types_attributes;
DROP TABLE IF EXISTS attributes;
DROP TABLE IF EXISTS attribute_types;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS request_types;
DROP TABLE IF EXISTS equipment;
DROP TABLE IF EXISTS warehouses;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS email_templates;


CREATE TABLE users
(
    id varchar(100) NOT NULL PRIMARY KEY,
    email varchar(60) NOT NULL,
    password varchar(255) NOT NULL,
    f_name varchar(40) NOT NULL,
    s_name varchar(40) NOT NULL,
    enabled smallint NOT NULL
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL
);

CREATE TABLE user_roles (
    user_id varchar(100) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    role_id SERIAL REFERENCES roles (id) ON UPDATE CASCADE,
    CONSTRAINT role_user_pkey PRIMARY KEY (user_id, role_id)
);

CREATE TABLE warehouses (
    id varchar(100) NOT NULL PRIMARY KEY,
    adress varchar(40) NOT NULL,
    contact_number varchar(20) NOT NULL
);

CREATE TABLE equipment (
    id varchar(100) NOT NULL PRIMARY KEY,
    model varchar(40) NOT NULL,
    producer varchar(40) NOT NULL,
    country varchar(40) NOT NULL
);

CREATE TABLE CREATE TABLE user_warehouses (
    user_id varchar(100) REFERENCES users(id) ON UPDATE CASCADE,
    warehouse_id varchar(100) REFERENCES warehouses(id) ON UPDATE CASCADE,
    CONSTRAINT user_warehouse_pkey PRIMARY KEY (user_id, warehouse_id)
);

CREATE TABLE warehouse_equipment (
    warehouse_id varchar(100) REFERENCES warehouses (id) ON UPDATE CASCADE,
    equipment_id varchar(100) REFERENCES equipment (id) ON UPDATE CASCADE,
    quantity numeric NOT NULL DEFAULT 0,
    CONSTRAINT warehouse_equipment_pkey PRIMARY KEY (warehouse_id, equipment_id)
);

CREATE TABLE request_types (
    id varchar(100) NOT NULL PRIMARY KEY,
    name varchar(40) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE requests (
  id varchar(100) NOT NULL PRIMARY KEY,
  warehouse_id varchar(100) REFERENCES warehouses (id) ON UPDATE CASCADE,
  creator_id varchar(100) REFERENCES users (id) ON UPDATE CASCADE,
  executor_id varchar(100) REFERENCES users (id) ON UPDATE CASCADE,
  req_type_id varchar(100) REFERENCES request_types (id),
  connected_request varchar(100) REFERENCES requests (id),
  title varchar(100) NOT NULL,
  status varchar(40) NOT NULL,
  creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  description text,
  archive bool NOT NULL
);

CREATE TABLE request_equipment (
    request_id varchar(100) REFERENCES requests (id),
    equipment_id varchar(100) REFERENCES equipment (id),
    quantity numeric NOT NULL DEFAULT 1,
    CONSTRAINT request_equipment_pkey PRIMARY KEY (request_id, equipment_id)
);

CREATE TABLE comments (
    id varchar(100) PRIMARY KEY NOT NULL,
    request_id varchar(100) REFERENCES requests (id) NOT NULL,
    user_id varchar(100) REFERENCES users (id) NOT NULL,
    comment_text text NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attribute_types (
    id varchar(100) PRIMARY KEY NOT NULL,
    name varchar(40) NOT NULL
);

CREATE TABLE attributes (
    id varchar(100) PRIMARY KEY NOT NULL,
    attr_type_id varchar(100) REFERENCES attribute_types (id) NOT NULL,
    name varchar(40) NOT NULL,
    attr_order SERIAL,
    multiple bool NOT NULL
);

CREATE TABLE request_types_attributes (
    req_type_id varchar(100) REFERENCES request_types(id) ON UPDATE CASCADE,
    attr_id varchar(100) REFERENCES attributes (id),
    mandatory bool NOT NULL,
    immutable bool NOT NULL,
    CONSTRAINT request_types_attr_pkey PRIMARY KEY (req_type_id, attr_id)
);

CREATE TABLE request_values (
    request_id varchar(100) REFERENCES requests (id),
    attr_id varchar(100) REFERENCES attributes (id),
    string_value varchar(200),
    date_value TIMESTAMP,
    decimal_value decimal,
    CONSTRAINT request_values_pkey PRIMARY KEY (request_id, attr_id)
);

CREATE TABLE reserved_values (
  id varchar(100) NOT NULL PRIMARY KEY,
  value_content varchar(100) NOT NULL
);

CREATE TABLE attributes_res_values (
    attr_id varchar(100) REFERENCES attributes (id),
    value_id varchar(100) REFERENCES reserved_values (id),
    CONSTRAINT attributes_res_values_pkey PRIMARY KEY (attr_id, value_id)
);

CREATE TABLE email_templates (
  id varchar(100) PRIMARY KEY NOT NULL,
  template varchar(300) NOT NULL
);


INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_KEEPER');

INSERT INTO users (id, email, password, s_name, f_name, enabled) VALUES ('9f047c13-8790-45a7-91c5-6fa8859c4884', 'user4@lol.com', '$2a$10$HBsXhdvojxvaqDutuUpdA.wA65ypP34TOC0eVL/tPnlKZRXbg5JPa', 'Podolov', 'Vladimir', 1);
INSERT INTO users (id, email, password, s_name, f_name, enabled) VALUES ('0083b40e-aed7-491b-be76-5e90c59e70b6', 'user4', '$2a$10$zAJMCF7kp81OmCbXnDvsJOy0TxFpcPl/XDW7crI6IkLfrepYLF2ny', 'Petr', 'Petrenko', 1);
INSERT INTO users (id, email, password, s_name, f_name, enabled) VALUES ('1', 'user1', '$2a$04$Ye7/lJoJin6.m9sOJZ9ujeTgHEVM4VXgI2Ingpsnf9gXyXEXf/IlW', 'Vladimir', 'Porohovich', 1);

INSERT INTO user_roles (role_id, user_id) VALUES (1, '1');
INSERT INTO user_roles (role_id, user_id) VALUES (2, '0083b40e-aed7-491b-be76-5e90c59e70b6');
INSERT INTO user_roles (role_id, user_id) VALUES (2, '9f047c13-8790-45a7-91c5-6fa8859c4884');

INSERT INTO email_templates (id, template) VALUES ('Registration', 'Dear %s, you have been registered in ''Amber warehouse system''!');
INSERT INTO email_templates (id, template) VALUES ('Request_status_changed', 'Dear %s, your request ''%s'' changed status from %s to %s.');
INSERT INTO email_templates (id, template) VALUES ('User_roles_changed', 'Dear %s, your role list has been changed. This is new list of your roles: %s.');
INSERT INTO email_templates (id, template) VALUES ('Request_created', 'Dear %s, your request ''%s'' has been created.');


INSERT INTO equipment (id, model, producer, country) VALUES ('3456387', 'model1', 'producer1', 'Ukraine');
INSERT INTO equipment (id, model, producer, country) VALUES ('dkljsal231', 'model2', 'producer2', 'Germany');
INSERT INTO equipment (id, model, producer, country) VALUES ('jkd730', 'model3', 'producer3', 'United Kingdom');
INSERT INTO equipment (id, model, producer, country) VALUES ('4kjld3', 'model4', 'producer1', 'Poland');
INSERT INTO equipment (id, model, producer, country) VALUES ('jkls94', 'model5', 'producer2', 'Ukraine');
INSERT INTO equipment (id, model, producer, country) VALUES ('jlkf4924', 'model6', 'producer1', 'Poland');

INSERT INTO request_types (id, name) VALUES ('iruds213', 'order');
INSERT INTO request_types (id, name) VALUES ('78923df', 'refund');
INSERT INTO request_types (id, name) VALUES ('7593jhhd', 'replenishment');

INSERT INTO attribute_types (id, name) VALUES ('jdklsa39f', 'select');
INSERT INTO attribute_types (id, name) VALUES ('80943jld', 'radio');
INSERT INTO attribute_types (id, name) VALUES ('j77gcj', 'text');
INSERT INTO attribute_types (id, name) VALUES ('lkciuod', 'checkbox');
INSERT INTO attribute_types (id, name) VALUES ('jclud', 'date');

INSERT INTO attributes (id, attr_type_id, name, multiple) VALUES ('ioyera', 'j77gcj', 'Pet name', false);
INSERT INTO attributes (id, attr_type_id, name, multiple) VALUES ('809jjsd', '80943jld', 'Type of pet', true);
INSERT INTO attributes (id, attr_type_id, name, multiple) VALUES ('djliui', 'jdklsa39f', 'Best sport', true);
INSERT INTO attributes (id, attr_type_id, name, multiple) VALUES ('djliuijljds', 'lkciuod', 'Favorite languages', true);
INSERT INTO attributes (id, attr_type_id, name, multiple) VALUES ('uio89h4f', 'jclud', 'Birth ', false);

INSERT INTO request_types_attributes (req_type_id, attr_id, mandatory, immutable)  VALUES ('iruds213', 'ioyera', true , false );
INSERT INTO request_types_attributes (req_type_id, attr_id, mandatory, immutable)  VALUES ('iruds213', '809jjsd', false, false );
INSERT INTO request_types_attributes (req_type_id, attr_id, mandatory, immutable)  VALUES ('iruds213', 'djliui', false, false );
INSERT INTO request_types_attributes (req_type_id, attr_id, mandatory, immutable)  VALUES ('iruds213', 'djliuijljds', false, false );
INSERT INTO request_types_attributes (req_type_id, attr_id, mandatory, immutable)  VALUES ('iruds213', 'uio89h4f', false, false );

INSERT INTO reserved_values (id, value_content) VALUES ('djslka344', 'Animal');
INSERT INTO reserved_values (id, value_content) VALUES ('jlkdyv', 'Ficus');
INSERT INTO reserved_values (id, value_content) VALUES ('78tjvh', 'Cybersport');
INSERT INTO reserved_values (id, value_content) VALUES ('jlkvy3888', 'Football');
INSERT INTO reserved_values (id, value_content) VALUES ('jyyf', 'Soccer');
INSERT INTO reserved_values (id, value_content) VALUES ('77893fhvv', 'C++');
INSERT INTO reserved_values (id, value_content) VALUES ('j6789', 'Java');
INSERT INTO reserved_values (id, value_content) VALUES ('poiiuooif', 'JavaScript');

INSERT INTO attributes_res_values (attr_id, value_id) VALUES ('809jjsd', 'djslka344');
INSERT INTO attributes_res_values (attr_id, value_id) VALUES ('809jjsd', 'jlkdyv');
INSERT INTO attributes_res_values (attr_id, value_id) VALUES ('djliui', '78tjvh');
INSERT INTO attributes_res_values (attr_id, value_id) VALUES ('djliui', 'jlkvy3888');
INSERT INTO attributes_res_values (attr_id, value_id) VALUES ('djliui', 'jyyf');
INSERT INTO attributes_res_values (attr_id, value_id) VALUES ('djliuijljds', '77893fhvv');
INSERT INTO attributes_res_values (attr_id, value_id) VALUES ('djliuijljds', 'j6789');
INSERT INTO attributes_res_values (attr_id, value_id) VALUES ('djliuijljds', 'poiiuooif');

INSERT INTO warehouses(id, adress, contact_number) VALUES ('jldkfgbhdd', 'Main str, Kiev, Ukraine', '835-55-35');
INSERT INTO warehouses(id, adress, contact_number) VALUES ('jldkfgbhdd2', 'Second str, Kiev, Ukraine', '825-55-35');
INSERT INTO warehouses(id, adress, contact_number) VALUES ('jldkfgbhdd3', 'Momo str, Lviv, Ukraine', '835-5885-35');
INSERT INTO warehouses(id, adress, contact_number) VALUES ('jldkfgbhdd4', 'Down str, New York, USA', '835-5335-35');

