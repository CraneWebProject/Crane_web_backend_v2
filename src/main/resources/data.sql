INSERT INTO Authority (authority_name)
SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM Authority WHERE authority_name = 'ROLE_ADMIN');
INSERT INTO Authority (authority_name)
SELECT 'ROLE_GRADUATED' WHERE NOT EXISTS (SELECT 1 FROM Authority WHERE authority_name = 'ROLE_GRADUATED');
INSERT INTO Authority (authority_name)
SELECT 'ROLE_MANAGER' WHERE NOT EXISTS (SELECT 1 FROM Authority WHERE authority_name = 'ROLE_MANAGER');
INSERT INTO Authority (authority_name)
SELECT 'ROLE_MEMBER' WHERE NOT EXISTS (SELECT 1 FROM Authority WHERE authority_name = 'ROLE_MEMBER');
INSERT INTO Authority (authority_name)
SELECT 'ROLE_STANBY' WHERE NOT EXISTS (SELECT 1 FROM Authority WHERE authority_name = 'ROLE_STANBY');
