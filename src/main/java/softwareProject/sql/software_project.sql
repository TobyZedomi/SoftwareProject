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


CREATE TABLE genre
(
    genre_id INT AUTO_INCREMENT,
    genre_name varchar(255),
    PRIMARY KEY (genre_id)
);

CREATE TABLE ageRequirement
(
    age_id INT AUTO_INCREMENT,
    age INT(50),
    PRIMARY KEY(age_id)
);


CREATE TABLE movies
(
    movie_id INT AUTO_INCREMENT,
    movie_name varchar(255) NOT NULL,
    genre_id int(11) NOT NULL,
    age_id INT(50) NOT NULL,
    date_of_release DATE NOT NULL,
    movie_length time NOT NULL,
    movie_info varchar(255) NOT NULL,
    movie_image varchar(255) NOT NULL,
    PRIMARY KEY (movie_id),
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id),
    FOREIGN KEY (age_id) REFERENCES ageRequirement (age_id) ON UPDATE CASCADE

);

CREATE TABLE streamingService
(
    streaming_service_id INT AUTO_INCREMENT,
    movie_id int(11),
    streaming_service_name varchar(255) NOT NULL,
    streaming_service_link varchar(255) NOT NULL,
    cost double NOT NULL,
    PRIMARY KEY (streaming_service_id),
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
);

CREATE TABLE review
(
    username varchar(255) NOT NULL,
    movie_id int(11) NOT NULL,
    rating double NOT NULL,
    comment text,
    PRIMARY KEY (username, movie_id),
    FOREIGN KEY (username) REFERENCES users (username),
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
);


CREATE TABLE favouriteList
(
    username varchar(255) NOT NULL,
    movie_id int(11) NOT NULL,
    PRIMARY KEY (username, movie_id),
    FOREIGN KEY (username) REFERENCES users (username),
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
);




create table friends
(
    friend1 varchar(10) not null,
    friend2 varchar(10) not null,
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




CREATE TABLE movieItem
(
    movieItem_id INT AUTO_INCREMENT,
    movieItem_name varchar(255) NOT NULL,
    movie_id int(11) NOT NULL,
    list_price double NOT NULL,
    stock int NOT NULL,
    PRIMARY KEY (movieItem_id),
    FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
);


CREATE TABLE orderItems
(
    order_id INT AUTO_INCREMENT,
    movieItem_id int(11),
    quantity int (11),
    price double NOT NULL,
    PRIMARY KEY (order_id),
    FOREIGN KEY (movieItem_id) REFERENCES movieItem (movieItem_id)
);



CREATE TABLE orders
(
    order_id int(11) NOT NULL,
    username varchar(255) NOT NULL,
    total_price double NOT NULL,
    order_date datetime NOT NULL,
    status varchar(15) NOT NULL,
    comments text,
    PRIMARY KEY (order_id, username),
    FOREIGN KEY (order_id) REFERENCES orderItems (order_id),
    FOREIGN KEY (username) REFERENCES users (username)
);

