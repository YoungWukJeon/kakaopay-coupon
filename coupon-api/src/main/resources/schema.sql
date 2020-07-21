CREATE TABLE IF NOT EXISTS user (
    no BIGINT PRIMARY KEY AUTO_INCREMENT,
    id VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(40) NOT NULL
);

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

create index index_expiration_date on coupon(expiration_date);
create index index_status on coupon(status);
create index index_user_no on coupon(user_no);