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

DROP TABLE user;

ALTER TABLE `admin`
    DROP COLUMN help_setting_id;

ALTER TABLE `admin`
    DROP COLUMN `role`;

ALTER TABLE `admin`
    DROP COLUMN status;

ALTER TABLE space
    MODIFY allow_overlap BIT(1) NOT NULL;

ALTER TABLE `admin`
    ADD `role` VARCHAR(255) NOT NULL;

ALTER TABLE device
    DROP COLUMN `role`;

ALTER TABLE device
    ADD `role` VARCHAR(255) NOT NULL;

ALTER TABLE `admin`
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE item
    DROP COLUMN status;

ALTER TABLE item
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE rental
    DROP COLUMN status;

ALTER TABLE rental
    ADD status VARCHAR(255) NOT NULL;