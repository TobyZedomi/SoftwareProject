DROP
DATABASE IF EXISTS software_project;
CREATE
DATABASE IF NOT EXISTS software_project;

USE
software_project;

CREATE TABLE users
(
    username    varchar(50) UNIQUE  NOT NULL,
    displayName varchar(50)         NOT NULL,
    email       varchar(255) UNIQUE NOT NULL,
    password    varchar(255)        NOT NULL,
    dateOfBirth DATE                NOT NULL,
    isAdmin     boolean             NOT NULL DEFAULT false,
    createdAt   datetime            NOT NULL,
    user_image     varchar(255) NOT NULL,
    PRIMARY KEY (username)
);


CREATE TABLE subscriptionPlan
(
    subscription_plan_id   INT AUTO_INCREMENT,
    subscription_plan_name VARCHAR(255) NOT NULL,
    cost                   double       NOT NULL,
    PRIMARY KEY (subscription_plan_id)
);

CREATE TABLE subscription
(
    subscription_id        INT AUTO_INCREMENT,
    username               varchar(255) NOT NULL,
    subscription_plan_id   INT(11) NOT NULL,
    subscription_startDate datetime     NOT NULL,
    subscription_endDate   datetime     NOT NULL,
    PRIMARY KEY (subscription_id),
    FOREIGN KEY (username) REFERENCES users (username),
    FOREIGN KEY (subscription_plan_id) REFERENCES subscriptionPlan (subscription_plan_id)
);


CREATE TABLE review
(
    username   varchar(255) NOT NULL,
    movieDb_id int(11) NOT NULL,
    rating     double       NOT NULL,
    comment    text,
    PRIMARY KEY (username, movieDb_id),
    FOREIGN KEY (username) REFERENCES users (username)
);


CREATE TABLE favouriteList
(
    username   varchar(255) NOT NULL,
    movieDb_id int(11) NOT NULL,
    backdrop_path VARCHAR(255),
    overview VARCHAR(8000),
    title VARCHAR(255),
    PRIMARY KEY (username, movieDb_id),
    FOREIGN KEY (username) REFERENCES users (username)
);


CREATE TABLE genreForMovie
(
    genreForMovie_id INT AUTO_INCREMENT,
    username   varchar(255) NOT NULL,
    movieDb_id int(11) NOT NULL,
    genre_id int(255),
    PRIMARY KEY (genreForMovie_id),
    FOREIGN KEY (username) REFERENCES users (username)
);


create table friends
(
    friend1 varchar(10) not null,
    friend2 varchar(10) not null,
    request boolean     NOT NULL DEFAULT false,
    PRIMARY KEY (friend1, friend2),
    FOREIGN KEY (friend1) REFERENCES users (username) on delete cascade,
    FOREIGN KEY (friend2) REFERENCES users (username) on delete cascade
);


CREATE TABLE movieProduct
(
    movie_id        INT AUTO_INCREMENT,
    movie_name      varchar(255) NOT NULL,
    date_of_release DATE         NOT NULL,
    movie_length    time         NOT NULL,
    movie_info      varchar(255) NOT NULL,
    movie_image     varchar(255) NOT NULL,
    listPrice       double       NOT NULL,
    PRIMARY KEY (movie_id)
);

CREATE TABLE carts
(
    cart_id  INT AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    PRIMARY KEY (cart_id),
    FOREIGN KEY (username) REFERENCES users (username)
);

CREATE TABLE cart_items
(
    cart_id  INT(11) NOT NULL,
    movie_id INT(11) NOT NULL,
    PRIMARY KEY (cart_id, movie_id),
    FOREIGN KEY (cart_id) REFERENCES carts (cart_id),
    FOREIGN KEY (movie_id) REFERENCES movieProduct (movie_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE billing_address
(
    billing_address_id INT AUTO_INCREMENT,
    username           varchar(255)        NOT NULL,
    fullName           varchar(255)        NOT NULL,
    email              varchar(255) UNIQUE NOT NULL,
    address            varchar(255)        NOT NULL,
    city               varchar(255),
    county             varchar(255),
    postcode           varchar(255),
    PRIMARY KEY (billing_address_id),
    FOREIGN KEY (username) REFERENCES users (username)
);


CREATE TABLE shop_order
(
    order_id           INT AUTO_INCREMENT,
    username           varchar(255) NOT NULL,
    billing_address_id int(11),
    order_date         datetime     NOT NULL,
    total_price        double       NOT NULL,
    order_status       varchar(15)  NOT NULL,
    PRIMARY KEY (order_id),
    FOREIGN KEY (username) REFERENCES users (username),
    FOREIGN KEY (billing_address_id) REFERENCES billing_address (billing_address_id)
);

CREATE TABLE orderItem
(
    order_items_id INT AUTO_INCREMENT,
    price          double NOT NULL,
    order_id       int(11),
    movie_id       int(11),
    PRIMARY KEY (order_items_id),
    FOREIGN KEY (movie_id) REFERENCES movieProduct (movie_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES shop_order (order_id)
);

CREATE TABLE genre
(
    id   INT(255),
    name VARCHAR(255),
    PRIMARY KEY (id)
);


CREATE TABLE password_reset_tokens
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    email  VARCHAR(255) NOT NULL,
    token  VARCHAR(255) NOT NULL,
    expiry TIMESTAMP    NOT NULL,
    FOREIGN KEY (email) REFERENCES users (email) ON DELETE CASCADE
);


CREATE TABLE `reviews`
(
     `username` VARCHAR(50) NOT NULL,
     `movie_id` INT NOT NULL,
     `movie_title` VARCHAR(255) NOT NULL,
     `content` TEXT NOT NULL,
     `rating` INT CHECK (rating BETWEEN 1 AND 5),
     `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (`username`, `movie_id`),
      FOREIGN KEY (`username`) REFERENCES users(`username`) ON DELETE CASCADE
);



/* TRIGGERS AND TABLES FOR TRIGGERS  */

CREATE TABLE auditsMovieProduct
(
    auditsMovieProductID INT AUTO_INCREMENT,
    table_name           VARCHAR(255),
    transaction_name     VARCHAR(10),
    movieName            VARCHAR(255),
    transdate            datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (auditsMovieProductID)
);

CREATE TABLE auditsUpdateMovieProduct
(
    auditsMovieProductID INT AUTO_INCREMENT,
    table_name           VARCHAR(255),
    transaction_name     VARCHAR(10),
    movieName            VARCHAR(255),
    oldPrice             double,
    newPrice             double,
    transdate            datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (auditsMovieProductID)
);

CREATE TABLE auditsCartItems
(
    auditsCartItemsID INT AUTO_INCREMENT,
    table_name        VARCHAR(255),
    transaction_name  VARCHAR(10),
    movie_id          int(11),
    transdate         datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (auditsCartItemsID),
    FOREIGN KEY (movie_id) REFERENCES movieProduct (movie_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE auditPurchasedItems
(
    auditPurchasedItemsID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    order_id INT NOT NULL,
    price DOUBLE NOT NULL,
    created_at DATETIME DEFAULT current_timestamp,
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (order_id) REFERENCES shop_order(order_id)
);

DELIMITER //

CREATE TRIGGER logPurchaseItemInsert
    AFTER INSERT ON orderItem
    FOR EACH ROW
BEGIN
    DECLARE orderUser VARCHAR(255);

    SELECT username INTO orderUser
    FROM shop_order
    WHERE order_id = NEW.order_id;

    INSERT INTO auditPurchasedItems (username, order_id, price)
    VALUES (orderUser, NEW.order_id, NEW.price);
END;
//

DELIMITER ;

DELIMITER
//

CREATE TRIGGER addMovieProduct
    AFTER INSERT
    ON movieproduct
    FOR EACH ROW
BEGIN
    INSERT INTO auditsMovieProduct(table_name, transaction_name, movieName)
    VALUES ('MovieProduct', 'INSERT', NEW.movie_name);
END //
DELIMITER //


DELIMITER //

CREATE TRIGGER deleteMovieProduct
    AFTER DELETE
    ON movieproduct
    FOR EACH ROW
BEGIN
    INSERT INTO auditsMovieProduct(table_name, transaction_name, movieName)
    VALUES ('MovieProduct', 'DELETE', OLD.movie_name);
END //
DELIMITER //


DELIMITER //

CREATE TRIGGER updateMovieProduct
    AFTER UPDATE
    ON movieproduct
    FOR EACH ROW
BEGIN
    INSERT INTO auditsUpdateMovieProduct(table_name, transaction_name, movieName, oldPrice, newPrice)
    VALUES ('MovieProduct', 'UPDATE', OLD.movie_name, OLD.listPrice, NEW.listPrice);
END //
DELIMITER //



DELIMITER //

CREATE TRIGGER addDeletedCartItems
    AFTER DELETE
    ON cart_items
    FOR EACH ROW
BEGIN
    INSERT INTO auditsCartItems(table_name, transaction_name, movie_id)
    VALUES ('cart_items', 'DELETE', OLD.movie_id);
END //
DELIMITER //


/*
 Stored Procedures
 */


DELIMITER //
CREATE PROCEDURE selectAllMovieProducts()
BEGIN
SELECT *
FROM movieproduct;
END
//
DELIMITER //


DELIMITER //
CREATE PROCEDURE addIntoCartItem(param_cart_id INT, param_movie_id INT)
BEGIN
INSERT INTO cart_items(cart_id, movie_id)
VALUES (param_cart_id, param_movie_id);
END
//
DELIMITER //