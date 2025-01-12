CREATE TABLE userinfo (
    id IDENTITY,
    userName VARCHAR NOT NULL,
    roomId INT NOT NULL,
    score INT,
    rank INT,
    active INT
);
CREATE TABLE word (
    id IDENTITY,
    word VARCHAR NOT NULL,
    hinsi VARCHAR NOT NULL,
    mean VARCHAR NOT NULL
);
CREATE TABLE room (
    roomId INT PRIMARY KEY,
    process INT NOT NULL,
    count INT,
    roomSize INT
);
