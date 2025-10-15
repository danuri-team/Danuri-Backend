CREATE TABLE `admin`
(
    id              BINARY(16)   NOT NULL,
    created_at      datetime     NOT NULL,
    updated_at      datetime     NOT NULL,
    company_id      BINARY(16)   NULL,
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    phone           VARCHAR(255) NOT NULL,
    `role`          VARCHAR(255) NOT NULL,
    status          VARCHAR(255) NOT NULL,
    help_setting_id BINARY(16)   NULL,
    CONSTRAINT pk_admin PRIMARY KEY (id)
);

CREATE TABLE company
(
    id         BINARY(16)   NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_company PRIMARY KEY (id)
);

CREATE TABLE device
(
    id         BINARY(16)   NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    name       VARCHAR(255) NOT NULL,
    company_id BINARY(16)   NULL,
    `role`     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_device PRIMARY KEY (id)
);

CREATE TABLE form
(
    id             BINARY(16)   NOT NULL,
    created_at     datetime     NOT NULL,
    updated_at     datetime     NOT NULL,
    title          VARCHAR(255) NULL,
    form_schema    LONGTEXT     NULL,
    company_id     BINARY(16)   NOT NULL,
    is_for_sign_up BIT(1)       NOT NULL,
    CONSTRAINT pk_form PRIMARY KEY (id)
);

CREATE TABLE form_result
(
    id                BINARY(16) NOT NULL,
    created_at        datetime   NOT NULL,
    updated_at        datetime   NOT NULL,
    result            LONGTEXT   NULL,
    user_id           BINARY(16) NOT NULL,
    is_sign_up_result BIT(1)     NOT NULL,
    CONSTRAINT pk_formresult PRIMARY KEY (id)
);

CREATE TABLE help_history
(
    id               BINARY(16) NOT NULL,
    created_at       datetime   NOT NULL,
    updated_at       datetime   NOT NULL,
    company_id       BINARY(16) NOT NULL,
    checked_admin_id BINARY(16) NULL,
    is_resolved      BIT(1)     NULL,
    CONSTRAINT pk_helphistory PRIMARY KEY (id)
);

CREATE TABLE help_setting
(
    id         BINARY(16) NOT NULL,
    created_at datetime   NOT NULL,
    updated_at datetime   NOT NULL,
    enable     BIT(1)     NULL,
    company_id BINARY(16) NULL,
    CONSTRAINT pk_helpsetting PRIMARY KEY (id)
);

CREATE TABLE item
(
    id                 BINARY(16)   NOT NULL,
    created_at         datetime     NOT NULL,
    updated_at         datetime     NOT NULL,
    name               VARCHAR(10)  NOT NULL,
    total_quantity     INT          NOT NULL,
    available_quantity INT          NOT NULL,
    status             VARCHAR(255) NOT NULL,
    company_id         BINARY(16)   NULL,
    CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE rental
(
    id                BINARY(16)   NOT NULL,
    created_at        datetime     NOT NULL,
    updated_at        datetime     NOT NULL,
    item_id           BINARY(16)   NULL,
    usage_id          BINARY(16)   NULL,
    quantity          INT          NOT NULL,
    borrowed_at       datetime     NOT NULL,
    returned_at       datetime     NULL,
    returned_quantity INT          NOT NULL,
    status            VARCHAR(255) NOT NULL,
    CONSTRAINT pk_rental PRIMARY KEY (id)
);

CREATE TABLE space
(
    id         BINARY(16)  NOT NULL,
    created_at datetime    NOT NULL,
    updated_at datetime    NOT NULL,
    company_id BINARY(16)  NULL,
    name       VARCHAR(50) NOT NULL,
    start_at   time        NOT NULL,
    end_at     time        NOT NULL,
    CONSTRAINT pk_space PRIMARY KEY (id)
);

CREATE TABLE usage_history
(
    id         BINARY(16) NOT NULL,
    created_at datetime   NOT NULL,
    updated_at datetime   NOT NULL,
    user_id    BINARY(16) NULL,
    space_id   BINARY(16) NULL,
    start_at   datetime   NOT NULL,
    end_at     datetime   NOT NULL,
    CONSTRAINT pk_usagehistory PRIMARY KEY (id)
);

CREATE TABLE user
(
    id         BINARY(16)  NOT NULL,
    created_at datetime    NOT NULL,
    updated_at datetime    NOT NULL,
    company_id BINARY(16)  NULL,
    phone      VARCHAR(30) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE form_result
    ADD CONSTRAINT uc_formresult_user UNIQUE (user_id);

ALTER TABLE help_setting
    ADD CONSTRAINT uc_helpsetting_company UNIQUE (company_id);

ALTER TABLE `admin`
    ADD CONSTRAINT FK_ADMIN_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE `admin`
    ADD CONSTRAINT FK_ADMIN_ON_HELP_SETTING FOREIGN KEY (help_setting_id) REFERENCES help_setting (id);

ALTER TABLE device
    ADD CONSTRAINT FK_DEVICE_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE form_result
    ADD CONSTRAINT FK_FORMRESULT_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE form
    ADD CONSTRAINT FK_FORM_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE help_history
    ADD CONSTRAINT FK_HELPHISTORY_ON_CHECKED_ADMIN FOREIGN KEY (checked_admin_id) REFERENCES `admin` (id);

ALTER TABLE help_history
    ADD CONSTRAINT FK_HELPHISTORY_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE help_setting
    ADD CONSTRAINT FK_HELPSETTING_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE item
    ADD CONSTRAINT FK_ITEM_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE rental
    ADD CONSTRAINT FK_RENTAL_ON_ITEM FOREIGN KEY (item_id) REFERENCES item (id);

ALTER TABLE rental
    ADD CONSTRAINT FK_RENTAL_ON_USAGE FOREIGN KEY (usage_id) REFERENCES usage_history (id);

ALTER TABLE space
    ADD CONSTRAINT FK_SPACE_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE usage_history
    ADD CONSTRAINT FK_USAGEHISTORY_ON_SPACE FOREIGN KEY (space_id) REFERENCES space (id);

ALTER TABLE usage_history
    ADD CONSTRAINT FK_USAGEHISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE user
    ADD CONSTRAINT FK_USER_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);