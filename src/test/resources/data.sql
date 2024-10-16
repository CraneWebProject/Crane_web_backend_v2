INSERT INTO Authority (authority_name)
SELECT new_roles.authority_name
FROM (
         SELECT 'ROLE_ADMIN' AS authority_name
         UNION ALL
         SELECT 'ROLE_GRADUATED'
         UNION ALL
         SELECT 'ROLE_MANAGER'
         UNION ALL
         SELECT 'ROLE_MEMBER'
         UNION ALL
         SELECT 'ROLE_STANBY'
     ) AS new_roles
WHERE NOT EXISTS (
        SELECT 1
        FROM Authority
        WHERE Authority.authority_name = new_roles.authority_name
    );