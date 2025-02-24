INSERT INTO `users` (`username`, `displayName`, `email`, `password`, `dateOfBirth`, `isAdmin`, `createdAt`)
VALUES ('admin', 'adminUser123', 'admin@gmail.com', 'AgcGk+yZvOCWx6I30PDQlfW/62dOVXmuX7TdXxeNlK0=', '2003-02-16', true, '2025-01-30 00:00:00'),
       ('Andrew', 'andrewGamer123', 'andrew@gmail.com', 'passwordDone123@', '2000-12-10', false, '2025-01-30 00:00:00'),
       ('Toby', 'toby123', 'toby@gmail.com', 'password123@',  '2001-18-08', false, '2025-01-30 00:00:00'),
       ('James', 'james123', 'james@gmail.com', 'Football123', '1999-11-18', false, '2025-01-30 00:00:00');


INSERT INTO `subscriptionPlan` (`subscription_plan_id`, `subscription_plan_name`, `cost`)
VALUES (1, 'Standard Plan', 9.99),
       (2, 'Premium Plan ',  14.99),
       (3, 'Student Plan', 4.99);


INSERT INTO `subscription` (`subscription_id`, `username`, `subscription_plan_id`, `subscription_startDate`, `subscription_endDate`)
VALUES (1, 'Toby', 1, '2025-01-30 00:00:00', '2026-01-30 00:00:00'),
       (2, 'Andrew', 2, '2023-11-30 00:00:00', '2024-11-30 00:00:00'),
       (3, 'James', 3, '2024-12-12 00:00:00', '2025-12-12 00:00:00');


INSERT INTO `review` (`username`, `movieDb_id`, `rating`, `comment`)
VALUES ('Toby', 12, 5, 'One of the greatest movies ive watched in my life'),
       ('Toby', 1, 5, 'Will always be an all time classic'),
       ('Toby', 4, 1, 'Hated every second of it'),
       ('Andrew', 12, 3, 'Great movie'),
       ('Andrew', 15, 4.5, 'The story of naruto is honestly amazing, glad ive made in this far '),
       ('James', 7, 3.5, 'Amazing movie'),
       ('James', 15, 3, 'Good movie, i like it a lot');



INSERT INTO `movieProduct` (`movie_id`, `movie_name`, `date_of_release`, `movie_length`, `movie_info`, `movie_image`,`listPrice` )
VALUES (1, 'Spider-Man-2',  '2004-07-16 ', '02:07:00', 'Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus', 'spiderman2.jpg', 10.99),
       (2, 'The Dark Knight',  '2008-07-28 ', '02:32:00', 'Batman must face Joker as he wants to destroy and control Gotham City. Batman struggles to face joker before its to late', 'batman.jpg', 12.99 ),
       (3, 'Inception',  '2010-07-16 ', '02:27:00', 'Cobb enters the dreams of people to steal information and wants to achieve an impossible task', 'inception.jpg', 8.99),
       (4, 'Dont Look Up', '2021-12-10 ', '02:46:00', 'Two astorometers try there best to tell the entire world that a comet is going to destroy earth', 'dontlookup.jpg', 10.99),
       (5, 'Friday',  '1995-04-26 ', '01:37:00', 'Two unemployed slackers are in debt to a gangster', 'friday.jpg', 7.99),
       (6, 'Yes Man',  '2008-12-17', '01:44:00', 'Carl is a lonely man who meets a friend that convinces him to say yes to everything for a whole entire year', 'yesman.jpg', 5.99),
       (7, 'Interstellar',  '2014-11-07', '02:55:00', 'Earth is going to become unlivable in the future so an exNasa pilot and his team is tasked to research other planets for humans', 'interstellar.jpg', 5.99),
       (8, 'The Matrix',  '1999-06-11', '02:16:00', 'Neo a computer hacker has always questioned reality and his questioning was answered as the truth was revealed', 'thematrix.jpg', 12.99),
       (9, 'Limitless',  '2011-03-25 ', '01:45:00', 'Eddie Morra doesnt have a bright future as he loses his job but he discovers an untested drug that improves his decisions', 'limitless.jpg', 15.99),
       (10, 'Se7en',  '1995-09-22', '02:10:00', 'Detectives Somerset and Millis pair up together to solve murders', 'seven.webp', 15.99),
       (11, 'Old',  '2021-07-23', '02:49:00', 'A vacation that turns into a nightmare, as a couple is stuck on a beach with others that make them age rapidly', 'old.jpg', 5.99),
       (12, 'The Prestige', '2006-11-10', '02:10:00', 'Two friends became bitter rivals, as they are in constant competition with each other to achieve fame', 'theprestige.jpg', 15.99),
       (13, 'Spider-Man Into The Spider-Verse',  '2018-10-14', '02:41:00', 'Miles Morales becomes Spider-man but he soon meets alternate versions of himself and gets in a battle to save the multiverse', 'intospiderman.jpg', 8.99),
       (14, 'Kurokos Basketball Last Game',  '2017-03-18', '01:31:00', 'Japan team faces the USA basketball team', 'kuroko.jpg',8.99 ),
       (15, 'The Last',  '2014-03-18', '01:52:00', 'A couple years after the ninja war, Naruto must stop Toneri Otsutuski', 'thelast.jpg', 10.99);



INSERT INTO `carts` (`cart_id`, `username`)
VALUES (1, 'admin');


INSERT INTO `reviews` (`name`, `email`, `content`, `createdAt`)
VALUES
    ('Sarah', 'sarah@mail.com', 'Amazing product, highly recommended!', '2025-02-24 22:15:30'),
    ('Michael', 'michael@gmail.com', 'Pretty Good Site', '2025-02-24 22:20:10'),
    ('Emma', 'emma@gmail.com', 'Best experience ever!', '2025-02-24 22:45:55');




