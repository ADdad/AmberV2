CREATE TABLE public.roles
(
    id integer DEFAULT nextval('roles_id_seq'::regclass) PRIMARY KEY NOT NULL,
    name varchar(40)
);
INSERT INTO public.roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.roles (id, name) VALUES (2, 'ROLE_USER');
CREATE TABLE public.user_roles
(
    role_id integer NOT NULL,
    user_id varchar(100) NOT NULL,
    CONSTRAINT user_roles_pk PRIMARY KEY (role_id, user_id),
    CONSTRAINT role_id FOREIGN KEY (role_id) REFERENCES public.roles (id),
    CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users (id)
);
INSERT INTO public.user_roles (role_id, user_id) VALUES (1, '1');
INSERT INTO public.user_roles (role_id, user_id) VALUES (2, '0083b40e-aed7-491b-be76-5e90c59e70b6');
INSERT INTO public.user_roles (role_id, user_id) VALUES (2, '9f047c13-8790-45a7-91c5-6fa8859c4884');
CREATE TABLE public.users
(
    id varchar(100) PRIMARY KEY NOT NULL,
    email varchar(80) NOT NULL,
    password varchar(255) NOT NULL,
    s_name varchar(40) NOT NULL,
    f_name varchar(40) NOT NULL,
    enabled smallint NOT NULL
);
INSERT INTO public.users (id, email, password, s_name, f_name, enabled) VALUES ('9f047c13-8790-45a7-91c5-6fa8859c4884', 'user4@lol.com', '$2a$10$HBsXhdvojxvaqDutuUpdA.wA65ypP34TOC0eVL/tPnlKZRXbg5JPa', 'Podolov', 'Vladimir', 1);
INSERT INTO public.users (id, email, password, s_name, f_name, enabled) VALUES ('0083b40e-aed7-491b-be76-5e90c59e70b6', 'user4', '$2a$10$zAJMCF7kp81OmCbXnDvsJOy0TxFpcPl/XDW7crI6IkLfrepYLF2ny', 'Petr', 'Petrenko', 1);
INSERT INTO public.users (id, email, password, s_name, f_name, enabled) VALUES ('1', 'user1', '$2a$04$Ye7/lJoJin6.m9sOJZ9ujeTgHEVM4VXgI2Ingpsnf9gXyXEXf/IlW', 'Vladimir', 'Porohovich', 1);