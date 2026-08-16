INSERT INTO system_config (config_key, config_value, description) VALUES ('app.name', 'Software Development Document Environment', 'Application Title');
INSERT INTO system_config (config_key, config_value, description) VALUES ('app.code', 'SDM', 'Default 3-character Application Code');
INSERT INTO system_config (config_key, config_value, description) VALUES ('auth.ldap.enabled', 'false', 'LDAP Active Directory authentication toggle');
INSERT INTO system_config (config_key, config_value, description) VALUES ('notification.email.enabled', 'false', 'Email Notification toggle for Maker/Checker');
INSERT INTO system_config (config_key, config_value, description) VALUES ('notification.sms.enabled', 'false', 'SMS Notification toggle for Maker/Checker');
INSERT INTO system_config (config_key, config_value, description) VALUES ('intake.folder.path', './intake_folder', 'Automated document pickup directory');
INSERT INTO system_config (config_key, config_value, description) VALUES ('intake.handlers.count', '2', 'Number of worker handlers for folder intake');

INSERT INTO users (username, password, email, mobile_number, role, active, locked, password_updated_at)
VALUES ('admin', '$2a$10$eD2gP2QfA4e/cW3C0eYkO.xU3uT4k6I.7M.J6n6kM2fO2.pQ4', 'admin@cth.com', '+10000000000', 'ADMIN', 1, 0, GETDATE());

INSERT INTO users (username, password, email, mobile_number, role, active, locked, password_updated_at)
VALUES ('maker', '$2a$10$eD2gP2QfA4e/cW3C0eYkO.xU3uT4k6I.7M.J6n6kM2fO2.pQ4', 'maker@cth.com', '+10000000001', 'MAKER', 1, 0, GETDATE());

INSERT INTO users (username, password, email, mobile_number, role, active, locked, password_updated_at)
VALUES ('checker', '$2a$10$eD2gP2QfA4e/cW3C0eYkO.xU3uT4k6I.7M.J6n6kM2fO2.pQ4', 'checker@cth.com', '+10000000002', 'CHECKER', 1, 0, GETDATE());
