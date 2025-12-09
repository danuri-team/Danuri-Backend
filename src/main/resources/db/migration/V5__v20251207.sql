ALTER TABLE user
    DROP FOREIGN KEY FK2yuxsfrkkrnkn5emoobcnnc3r;

ALTER TABLE usage_history
    DROP FOREIGN KEY FK9cxquw9seccmm2q6mxf1vqhc7;

ALTER TABLE form_result
    DROP FOREIGN KEY FKfnsvjxihpgpncxskfcisqk61i;

ALTER TABLE space
    ADD allow_multi_space_booking BIT(1) NULL DEFAULT 0;

ALTER TABLE space
    MODIFY allow_multi_space_booking BIT(1) NOT NULL DEFAULT 0;

RENAME TABLE "USER" TO users;

ALTER TABLE `admin`
    DROP COLUMN help_setting_id;

ALTER TABLE admin
    MODIFY role VARCHAR(255) NOT NULL;

ALTER TABLE admin
    MODIFY status VARCHAR(255) NOT NULL;

ALTER TABLE space
    MODIFY allow_overlap BIT(1) NOT NULL;

ALTER TABLE device
    MODIFY role VARCHAR(255) NOT NULL;

ALTER TABLE item
    MODIFY status VARCHAR(255) NOT NULL;

ALTER TABLE rental
    MODIFY status VARCHAR(255) NOT NULL;
