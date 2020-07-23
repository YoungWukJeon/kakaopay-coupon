CREATE TABLE IF NOT EXISTS coupon (
    no BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(30) NOT NULL UNIQUE,
    created_date DATETIME NOT NULL,
    published_date DATETIME,
    used_date DATETIME,
    expiration_date DATETIME,
    status VARCHAR(20) NOT NULL,
    user_no BIGINT
);

CREATE INDEX index_expiration_date ON coupon (expiration_date);
CREATE INDEX index_status ON coupon(status);
CREATE INDEX index_user_no ON coupon(user_no);

CREATE TABLE IF NOT EXISTS user (
    no BIGINT PRIMARY KEY AUTO_INCREMENT,
    id VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_entity_roles (
    user_entity_no BIGINT NOT NULL,
    roles VARCHAR(255),
    FOREIGN KEY (user_entity_no) REFERENCES user (no)
);