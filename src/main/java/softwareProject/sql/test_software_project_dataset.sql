INSERT INTO `users` (`username`, `displayName`, `email`, `password`, `address`, `dateOfBirth`, `isAdmin`, `createdAt`)
VALUES ('admin', 'adminUser123', 'admin@gmail.com', 'admin', '78 Wheaton Hall Dublin Road, Co.Louth', '2003-02-16 00:00:00', true, '2025-01-30 00:00:00'),
       ('Andrew', 'andrewGamer123', 'andrew@gmail.com', 'passwordDone123@', '88 Wheaton Hall Dublin Road, Co.Louth', '2000-12-10 00:00:00', false, '2025-01-30 00:00:00'),
       ('Toby', 'toby123', 'toby@gmail.com', 'password123@', '56 Mell Town Dublin Road, Co.Louth', '2001-18-08 00:00:00', false, '2025-01-30 00:00:00'),
       ('James', 'james123', 'james@gmail.com', 'Football123', '32 TullyAllen Dublin Road, Co.Louth', '1999-11-18 00:00:00', false, '2025-01-30 00:00:00');


INSERT INTO `subscriptionPlan` (`subscription_plan_id`, `subscription_plan_name`, `cost`)
VALUES (1, 'Standard Plan', 9.99),
       (2, 'Premium Plan ',  14.99),
       (3, 'Student Plan', 4.99);


INSERT INTO `subscription` (`subscription_id`, `username`, `subscription_plan_id`, `subscription_startDate`, `subscription_endDate`)
VALUES (1, 'Toby', 1, '2025-01-30 00:00:00', '2026-01-30 00:00:00'),
       (2, 'Andrew', 2, '2023-11-30 00:00:00', '2024-11-30 00:00:00'),
       (3, 'James', 3, '2024-12-12 00:00:00', '2025-12-12 00:00:00');


INSERT INTO `genre` (`genre_id`, `genre_name`)
VALUES (1, 'Action'),
       (2, 'Comedy'),
       (3, 'Science-Fiction'),
       (4, 'Thriller'),
       (5, 'Animation');

INSERT INTO `ageRequirement` (`age_id`, `age`)
VALUES (1, 12),
       (2, 13),
       (3, 16),
       (4, 18);

INSERT INTO `movies` (`movie_id`, `movie_name`, `genre_id`, `age_id`, `date_of_release`, `movie_length`, `movie_info`, `movie_image`)
VALUES (1, 'Spider-Man-2', 1, 3, '2004-07-16 00:00:00', '02:07:00', 'Peter Parker is very unhappy with life as he loses his job, Mary Jane and powers but must manage to save New York from the evil Doctor Octopus', 'images\spiderman2.jpg'),
       (2, 'The Dark Knight', 1, 3, '2008-07-28 00:00:00', '02:32:00', 'Batman must face Joker as he wants to destroy and control Gotham City. Batman struggles to face joker before its to late', 'images\batman.jpg' ),
       (3, 'Inception', 1, 2, '2010-07-16 00:00:00', '02:27:00', 'Cobb enters the dreams of people to steal information and wants to achieve an impossible task', 'images\inception.jpg'),
       (4, 'Dont Look Up', 2, 1, '2021-12-10 00:00:00', '02:46:00', 'Two astorometers try there best to tell the entire world that a comet is going to destroy earth', 'images\dontlookup.jpg'),
       (5, 'Friday', 2, 2, '1995-04-26 00:00:00', '01:37:00', 'Two unemployed slackers are in debt to a gangster', 'images\friday.jpg'),
       (6, 'Yes Man', 2, 2, '2008-12-17 00:00:00', '01:44:00', 'Carl is a lonely man who meets a friend that convinces him to say yes to everything for a whole entire year', 'images\yesman.jpg'),
       (7, 'Interstellar', 3, 2, '2014-11-07 00:00:00', '02:55:00', 'Earth is going to become unlivable in the future so an exNasa pilot and his team is tasked to research other planets for humans', 'images\interstellar.jpg'),
       (8, 'The Matrix', 3, 3, '1999-06-11 00:00:00', '02:16:00', 'Neo a computer hacker has always questioned reality and his questioning was answered as the truth was revealed', 'images\thematrix.jpg'),
       (9, 'Limitless', 3, 4, '2011-03-25 00:00:00', '01:45:00', 'Eddie Morra doesnt have a bright future as he loses his job but he discovers an untested drug that improves his decisions', 'images\limitless.jpg'),
       (10, 'Se7en', 4, 4, '1995-09-22 00:00:00', '02:10:00', 'Detectives Somerset and Millis pair up together to solve murders', 'images\seven.webp'),
       (11, 'Old', 4, 3, '2021-07-23 00:00:00', '02:49:00', 'A vacation that turns into a nightmare, as a couple is stuck on a beach with others that make them age rapidly', 'images\old.jpg'),
       (12, 'The Prestige', 4, 2, '2006-11-10 00:00:00', '02:10:00', 'Two friends became bitter rivals, as they are in constant competition with each other to achieve fame', 'images\theprestige.jpg'),
       (13, 'Spider-Man Into The Spider-Verse', 5, 1, '2018-10-14 00:00:00', '02:41:00', 'Miles Morales becomes Spider-man but he soon meets alternate versions of himself and gets in a battle to save the multiverse', 'images\intospiderman.jpg'),
       (14, 'Kurokos Basketball Last Game', 5, 1, '2017-03-18 00:00:00', '01:31:00', 'Japan team faces the USA basketball team', 'images\kuroko.jpg'),
       (15, 'The Last', 5, 1, '2014-03-18 00:00:00', '01:52:00', 'A couple years after the ninja war, Naruto must stop Toneri Otsutuski', 'images\thelast.jpg');



INSERT INTO `streamingService` (`streaming_service_id`, `movie_id`, `streaming_service_name`, `streaming_service_link`, `cost`)
VALUES (1, 1, 'Netflix', 'https://www.netflix.com/ie/', 9.99),
       (2, 1, 'Amazon Prime Video', 'https://www.primevideo.com/', 6.99),
       (3, 1, 'Hulu', 'https://www.hulu.com/', 9.99),
       (4, 2, 'Disney+', 'https://www.disneyplus.com/en-ie', 8.99),
       (5, 2, 'HBO Max', 'https://www.max.com/', 10.99),
       (6, 3, 'Amazon Prime Video', 'https://www.primevideo.com/', 6.99),
       (7, 3, 'Hulu', 'https://www.hulu.com/', 9.99),
       (8, 4, 'Disney+', 'https://www.disneyplus.com/en-ie', 8.99),
       (9, 5, 'HBO Max', 'https://www.max.com/', 10.99),
       (10, 5, 'Amazon Prime Video', 'https://www.primevideo.com/', 6.99),
       (11, 6, 'Hulu', 'https://www.hulu.com/', 9.99),
       (12, 6, 'Disney+', 'https://www.disneyplus.com/en-ie', 8.99),
       (13, 7, 'HBO Max', 'https://www.max.com/', 10.99),
       (14, 8, 'Amazon Prime Video', 'https://www.primevideo.com/', 6.99),
       (15, 9, 'Hulu', 'https://www.hulu.com/', 9.99),
       (16, 10, 'Disney+', 'https://www.disneyplus.com/en-ie', 8.99),
       (17, 10, 'HBO Max', 'https://www.max.com/', 10.99),
       (18, 10, 'Amazon Prime Video', 'https://www.primevideo.com/', 6.99),
       (19, 11, 'Hulu', 'https://www.hulu.com/', 9.99),
       (20, 11, 'Disney+', 'https://www.disneyplus.com/en-ie', 8.99),
       (21, 12, 'HBO Max', 'https://www.max.com/', 10.99),
       (22, 12, 'Amazon Prime Video', 'https://www.primevideo.com/', 6.99),
       (23, 13, 'Hulu', 'https://www.hulu.com/', 9.99),
       (24, 13, 'Disney+', 'https://www.disneyplus.com/en-ie', 8.99),
       (25, 13, 'HBO Max', 'https://www.max.com/', 10.99),
       (26, 14, 'CrunchyRoll', 'https://www.crunchyroll.com/', 5.99),
       (27, 14, 'Hulu', 'https://www.hulu.com/', 9.99),
       (28, 15, 'CrunchyRoll', 'https://www.crunchyroll.com/', 5.99),
       (29, 15, 'HBO Max', 'https://www.max.com/', 10.99);



INSERT INTO `review` (`username`, `movie_id`, `rating`, `comment`)
VALUES ('Toby', 12, 5, 'One of the greatest movies ive watched in my life'),
       ('Toby', 1, 5, 'Will always be an all time classic'),
       ('Toby', 4, 1, 'Hated every second of it'),
       ('Andrew', 12, 3, 'Great movie'),
       ('Andrew', 15, 4.5, 'The story of naruto is honestly amazing, glad ive made in this far '),
       ('James', 7, 3.5, 'Amazing movie'),
       ('James', 15, 3, 'Good movie, i like it a lot');




