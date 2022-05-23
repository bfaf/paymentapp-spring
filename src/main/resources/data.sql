-- User user/pass
INSERT INTO users (username, password, enabled)
  values ('admin',
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
    1) ON CONFLICT DO NOTHING;

INSERT INTO authorities (username, authority)
  values ('admin', 'ROLE_ADMIN') ON CONFLICT DO NOTHING;