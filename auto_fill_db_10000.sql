WITH random_strings AS (
    SELECT generate_series(1, 10000) AS id, 'str' || generate_series(1, 10000) AS value
), new_users AS (
    SELECT uid.value, email.value || '@gmail.com', p.value, sn.value, fn.value, 1
    FROM (((random_strings AS uid INNER JOIN random_strings AS email ON uid.id = email.id)
        INNER JOIN random_strings AS p ON uid.id = p.id)
        INNER JOIN random_strings AS sn ON uid.id = sn.id)
           INNER JOIN random_strings AS fn ON uid.id = fn.id
)

INSERT INTO users (id, email, password, s_name, f_name, enabled)
SELECT *
FROM new_users
    ON CONFLICT DO NOTHING;
-- -------------------------------------------------------------------------------------------------------

INSERT INTO user_roles (role_id, user_id)
SELECT 1, id
FROM users
LIMIT 50
    ON CONFLICT DO NOTHING;
-- -------------------------------------------------------------------------------------------------------


INSERT INTO user_roles (role_id, user_id)
SELECT 2, id
FROM users
LIMIT 9850 OFFSET 50
    ON CONFLICT DO NOTHING;
-- -------------------------------------------------------------------------------------------------------


INSERT INTO user_roles (role_id, user_id)
SELECT 3, id
FROM users
LIMIT 100 OFFSET 9900
    ON CONFLICT DO NOTHING;
-- -------------------------------------------------------------------------------------------------------

WITH random_strings AS (
    SELECT generate_series(1, 10000) AS id, 'str' || generate_series(1, 10000) AS value
), new_equip AS (
    SELECT a.value, b.value, c.value, d.value
    FROM (((random_strings AS a INNER JOIN random_strings AS b ON a.id = b.id)
        INNER JOIN random_strings AS c ON a.id = c.id)
        INNER JOIN random_strings AS d ON a.id = d.id)
)

INSERT INTO equipment (id, model, producer, country)
SELECT *
FROM new_equip
    ON CONFLICT DO NOTHING;
-- -------------------------------------------------------------------------------------------------------


WITH random_strings AS (
    SELECT generate_series(1, 10000) AS id, 'str' || generate_series(1, 10000) AS value
), new_warehouses AS (
    SELECT a.value, b.value, c.value
    FROM ((random_strings AS a INNER JOIN random_strings AS b ON a.id = b.id)
        INNER JOIN random_strings AS c ON a.id = c.id)
)

INSERT INTO warehouses (id, adress, contact_number)
SELECT *
FROM new_warehouses
    ON CONFLICT DO NOTHING;
-- -------------------------------------------------------------------------------------------------------


INSERT INTO user_warehouses (user_id, warehouse_id)
SELECT u.id, w.id
FROM users AS u
       INNER JOIN warehouses AS w ON u.id = w.id
    ON CONFLICT DO NOTHING;
-- -------------------------------------------------------------------------------------------------------

WITH random_numb AS (
    SELECT floor(random() * 10000 + 1): :int AS a
    FROM (SELECT generate_series(1, 10000) AS a) AS b
)
INSERT INTO warehouse_equipment (warehouse_id, equipment_id, quantity)
SELECT w.id, e.id, q.a
FROM (warehouses AS w INNER JOIN equipment AS e ON w.id = e.id)
       INNER JOIN random_numb AS q ON w.id = 'str' || q.a
    ON CONFLICT DO NOTHING;
-- -------------------------------------------------------------------------------------------------------

WITH u_w AS (
    SELECT u.id AS uid, w.id AS wid
    FROM users AS u
           INNER JOIN warehouses AS w ON u.id = w.id || '0'
), u_3 AS (
    SELECT user_id
    FROM user_roles
    WHERE role_id = 3
), executors AS (
    SELECT u_w.uid, u_w.wid
    FROM u_3
           INNER JOIN u_w ON u_3.user_id = u_w.uid
), random_strings AS (
    SELECT generate_series(1, 10000) AS id, 'str' || generate_series(1, 10000) AS value
), random_numb AS (
    SELECT floor(random() * 10000 + 1): :int AS a
    FROM (SELECT generate_series(1, 10000) AS a) AS b
)

INSERT INTO requests (id, warehouse_id, creator_id, executor_id, req_type_id, title, status, description, archive)
    (SELECT r.value,
            w.id,
            u.id,
            'str99'       AS eid,
            'iruds213'    AS req_type_id,
            'title'       AS title,
            'Opened'      AS status,
            'description' AS d,
            false         AS arc
     FROM ((random_strings AS r INNER JOIN warehouses AS w ON r.value = w.id)
         INNER JOIN users AS u ON r.value = u.id)
            LEFT JOIN executors AS e ON r.value = e.uid
     LIMIT 4000)
UNION
    (SELECT r.value,
            w.id,
            u.id,
            'str98'       AS eid,
            'iruds213'    AS req_type_id,
            'title'       AS title,
            'Opened'      AS status,
            'description' AS d,
            false         AS arc
     FROM ((random_strings AS r INNER JOIN warehouses AS w ON r.value = w.id)
         INNER JOIN users AS u ON r.value = u.id)
            LEFT JOIN executors AS e ON r.value = e.uid
     LIMIT 3000 OFFSET 4000)
UNION
    (SELECT r.value,
            w.id,
            u.id,
            'str97'       AS eid,
            'iruds213'    AS req_type_id,
            'title'       AS title,
            'Opened'      AS status,
            'description' AS d,
            false         AS arc
     FROM ((random_strings AS r INNER JOIN warehouses AS w ON r.value = w.id)
         INNER JOIN users AS u ON r.value = u.id)
            LEFT JOIN executors AS e ON r.value = e.uid
     LIMIT 3000 OFFSET 7000)
    ON CONFLICT DO NOTHING;
