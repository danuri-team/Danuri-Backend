-- DEV 환경 초기 데이터
INSERT INTO company (id, name, created_at, updated_at)
VALUES
    (RANDOM_UUID(), '송정다누리', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
-- Admin 데이터
INSERT INTO admin (id, company_id, email, password, phone, role, status, created_at, updated_at)
VALUES
    (RANDOM_UUID(),
     (SELECT id FROM company WHERE name = '송정다누리'),
     'admin@example.com',
     '$2a$12$OhsTomlT51MDkxg3n3gRfubYTkjiBU2lBkkq7hs9gMipzM0NfLWvK',  -- Password1234!
     '010-1111-1111',
     'ROLE_ADMIN',
     'AVAILABLE',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP);

-- Space 데이터
INSERT INTO space (id, company_id, name, allow_overlap, allow_multi_space_booking, start_at, end_at, created_at, updated_at)
VALUES
    (RANDOM_UUID(),
     (SELECT id FROM company WHERE name = '송정다누리'),
     '노래방',
     true,
     false,
     '09:00:00',
     '18:00:00',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP);

-- Item 데이터
INSERT INTO item (id, company_id, name, total_quantity, available_quantity, status, created_at, updated_at)
VALUES
    (RANDOM_UUID(),
     (SELECT id FROM company WHERE name = '송정다누리'),
     '빔프로젝터',
     3,
     3,
     'AVAILABLE',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP);

-- Device 데이터
INSERT INTO device (id, company_id, name, role, created_at, updated_at)
VALUES
    (RANDOM_UUID(),
     (SELECT id FROM company WHERE name = '송정다누리'),
     '입구 키오스크',
     'ROLE_DEVICE',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP);

-- User 데이터
INSERT INTO users (id, company_id, phone, created_at, updated_at)
VALUES
    (RANDOM_UUID(),
     (SELECT id FROM company WHERE name = '송정다누리'),
     '010-5555-5555',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP);

-- Form 데이터
INSERT INTO form (id, company_id, title, form_schema, is_for_sign_up, created_at, updated_at)
VALUES
    (RANDOM_UUID(),
     (SELECT id FROM company WHERE name = '송정다누리'),
     '회원가입 양식',
     '[{"id": 0,"key": "userName","type": "INPUT","label": "이름","labelUrl": null,"options": null,"placeHolder": "이름을 입력해주세요.","isRequired": true,"isMultiSelect": false},{"id": 1,"key": "gender","type": "SELECT","label": "성별","labelUrl": null,"options": [ {"id": 0, "option": "남"},{"id": 1, "option": "여"}],"placeHolder": null,"isRequired": true,"isMultiSelect": false},{"id": 2,"key": "age","type": "SELECT","label": "나이","labelUrl": null,"options": [{"id": 0, "option": "초등학교"},{"id": 1, "option": "중학교"},{"id": 2, "option": "고등학교"},{"id": 3, "option": "대학교"},{"id": 4, "option": "학교 밖 청소년"},{"id": 5, "option": "성인/유아"}],"placeHolder": null,"isRequired": true,"isMultiSelect": false}, {"id": 3,"key": "organisation","type": "INPUT","label": "소속","labelUrl": null,"options": null,"placeHolder": "소속을 입력해주세요.","isRequired": false,"isMultiSelect": false},{"id": 4,"key": "phone","type": "INPUT","label": "(만 14세 미만) 보호자 연락처","labelUrl": null,"options": null,"placeHolder": "연락처를 입력해주세요.","isRequired": false,"isMultiSelect": false},{"id": 5,"key": "consentForPersonalInformation","type": "SELECT","label": "개인정보제공 동의","labelUrl": "https://working-mailman-871.notion.site/2245f20bda6f80c89bb2f2e710fa58b5?pvs=143","options": [  {"id": 0, "option": "동의합니다"}],"placeHolder": null,"isRequired": true,"isMultiSelect": false}]',
     true,
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP);

-- FormResult 데이터

INSERT INTO form_result (id, user_id, result, is_sign_up_result, created_at, updated_at)
VALUES
    (RANDOM_UUID(),
     (SELECT id FROM users WHERE phone = '010-5555-5555'),
     '{"id":0,"이름":"홍길동","성별":"남","나이":"성인/유아","소속":"다누리","개인정보제공 동의":"동의합니다"}',
     true,
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP);