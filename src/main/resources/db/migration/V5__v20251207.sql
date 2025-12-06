ALTER TABLE danuri_prod.user
    DROP FOREIGN KEY FK2yuxsfrkkrnkn5emoobcnnc3r;

ALTER TABLE danuri_prod.usage_history
    DROP FOREIGN KEY FK9cxquw9seccmm2q6mxf1vqhc7;

ALTER TABLE danuri_prod.form_result
    DROP FOREIGN KEY FKfnsvjxihpgpncxskfcisqk61i;

CREATE TABLE danuri_prod.users
(
    id         BINARY(16)  NOT NULL,
    created_at datetime    NOT NULL,
    updated_at datetime    NOT NULL,
    company_id BINARY(16)  NULL,
    phone      VARCHAR(30) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE danuri_prod.space
    ADD allow_multi_space_booking BIT(1) NULL;

ALTER TABLE danuri_prod.space
    MODIFY allow_multi_space_booking BIT(1) NOT NULL;

ALTER TABLE danuri_prod.form_result
    ADD CONSTRAINT FK_FORMRESULT_ON_USER FOREIGN KEY (user_id) REFERENCES danuri_prod.users (id);

ALTER TABLE danuri_prod.usage_history
    ADD CONSTRAINT FK_USAGEHISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES danuri_prod.users (id);

ALTER TABLE danuri_prod.users
    ADD CONSTRAINT FK_USERS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES danuri_prod.company (id);

DROP TABLE danuri_prod.user;

ALTER TABLE danuri_prod.`admin`
    DROP COLUMN help_setting_id;

ALTER TABLE danuri_prod.`admin`
    DROP COLUMN `role`;

ALTER TABLE danuri_prod.`admin`
    DROP COLUMN status;

ALTER TABLE danuri_prod.space
    MODIFY allow_overlap BIT(1) NOT NULL;

ALTER TABLE danuri_prod.`admin`
    ADD `role` VARCHAR(255) NOT NULL;

ALTER TABLE danuri_prod.device
    DROP COLUMN `role`;

ALTER TABLE danuri_prod.device
    ADD `role` VARCHAR(255) NOT NULL;

ALTER TABLE danuri_prod.`admin`
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE danuri_prod.item
    DROP COLUMN status;

ALTER TABLE danuri_prod.item
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE danuri_prod.rental
    DROP COLUMN status;

ALTER TABLE danuri_prod.rental
    ADD status VARCHAR(255) NOT NULL;