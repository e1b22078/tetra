CREATE TABLE userinfo (
    id IDENTITY,
    userName VARCHAR NOT NULL,
    roomId INT NOT NULL
);
CREATE TABLE word (
    id IDENTITY,
    word VARCHAR NOT NULL,
    hinsi VARCHAR NOT NULL,
    mean VARCHAR NOT NULL
);
CREATE TABLE room (
    roomId INT PRIMARY KEY,
    process INT NOT NULL
);
