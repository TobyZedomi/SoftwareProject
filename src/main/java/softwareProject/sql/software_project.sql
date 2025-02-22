DROP
DATABASE IF EXISTS software_project;
CREATE
DATABASE IF NOT EXISTS software_project;

USE
software_project;

CREATE TABLE users
(
    username varchar(50)  UNIQUE NOT NULL,
    displayName varchar(50) UNIQUE NOT NULL,
    email    varchar(255) UNIQUE NOT NULL,
    password varchar(255)        NOT NULL,
    address  varchar(255) 	 NOT NULL,
    dateOfBirth DATE	 NOT NULL,
    isAdmin boolean NOT NULL DEFAULT false,
    createdAt datetime NOT NULL,
    PRIMARY KEY (username)
);


CREATE TABLE subscriptionPlan
(
    subscription_plan_id INT AUTO_INCREMENT,
    subscription_plan_name VARCHAR(255) NOT NULL,
    cost double NOT NULL,
    PRIMARY KEY (subscription_plan_id)
);

CREATE TABLE subscription
(
    subscription_id        INT AUTO_INCREMENT,
    username     varchar(255) NOT NULL,
    subscription_plan_id 	INT(11) NOT NULL,
    subscription_startDate datetime NOT NULL,
    subscription_endDate  datetime NOT NULL,
    PRIMARY KEY (subscription_id),
    FOREIGN KEY (username) REFERENCES users (username),
    FOREIGN KEY (subscription_plan_id) REFERENCES subscriptionPlan (subscription_plan_id)
);


CREATE TABLE review
(
    username varchar(255) NOT NULL,
    movieDb_id int(11) NOT NULL,
    rating double NOT NULL,
    comment text,
    PRIMARY KEY (username, movieDb_id),
    FOREIGN KEY (username) REFERENCES users (username)
);


CREATE TABLE favouriteList
(
    username varchar(255) NOT NULL,
    movieDb_id int(11) NOT NULL,
    PRIMARY KEY (username, movieDb_id),
    FOREIGN KEY (username) REFERENCES users (username)
);


create table friends
(
    friend1 varchar(10) not null,
    friend2 varchar(10) not null,
    request boolean NOT NULL DEFAULT false,
    PRIMARY KEY (friend1, friend2),
    FOREIGN KEY (friend1) REFERENCES users(username) on delete cascade,
    FOREIGN KEY (friend2) REFERENCES users(username) on delete cascade
);


DELIMITER |
CREATE TRIGGER enforce_friendship_order BEFORE INSERT ON friends
    FOR EACH ROW BEGIN
    SET @lowerName := IF(NEW.friend1 < NEW.friend2, NEW.friend1, NEW.friend2);
    SET @higherName := IF(NEW.friend1 > NEW.friend2, NEW.friend1, NEW.friend2);
    SET NEW.friend1 = @lowerName;
    SET NEW.friend2 = @higherName;
END;
|
DELIMITER ;


CREATE TABLE movieProduct
(
    movie_id INT AUTO_INCREMENT,
    movie_name varchar(255) NOT NULL,
    date_of_release DATE NOT NULL,
    movie_length time NOT NULL,
    movie_info varchar(255) NOT NULL,
    movie_image varchar(255) NOT NULL,
    listPrice double NOT NULL,
    PRIMARY KEY (movie_id)
);

CREATE TABLE carts
(
    cart_id INT AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    PRIMARY KEY(cart_id),
    FOREIGN KEY (username) REFERENCES users (username)
);

CREATE TABLE cart_items
(
    cart_id INT(11) NOT NULL,
    movie_id INT(11) NOT NULL,
    PRIMARY KEY (cart_id, movie_id),
    FOREIGN KEY (cart_id) REFERENCES carts (cart_id),
    FOREIGN KEY (movie_id) REFERENCES movieProduct (movie_id)
);



CREATE TABLE shop_order
(
    order_id int(11) NOT NULL,
    username varchar(255) NOT NULL,
    order_date datetime NOT NULL,
    total_price double NOT NULL,
    order_status varchar(15) NOT NULL,
    PRIMARY KEY (order_id),
    FOREIGN KEY (username) REFERENCES users (username)
);

CREATE TABLE orderItem
(
    order_items_id INT AUTO_INCREMENT,
    price double NOT NULL,
    order_id int(11),
    movie_id int(11),
    PRIMARY KEY (order_items_id),
    FOREIGN KEY (movie_id) REFERENCES movieProduct (movie_id),
    FOREIGN KEY (order_id) REFERENCES shop_order (order_id)
);


CREATE TABLE password_reset_tokens (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       email VARCHAR(255) NOT NULL,
                                       token VARCHAR(255) NOT NULL,
                                       expiry TIMESTAMP NOT NULL,
                                       FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE
);