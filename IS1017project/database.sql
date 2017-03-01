USE erl67is1017;

CREATE TABLE IF NOT EXISTS user (
    user_id INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    description VARCHAR(50) NOT NULL UNIQUE,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP 
);


ALTER TABLE todolist
ADD UNIQUE (description);


CREATE TABLE t1 (id int primary key, name varchar(50), 
  ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP );



INSERT INTO todolist(description, timestamp) VALUES ('eat', CURRENT_TIMESTAMP());
INSERT INTO todolist(description, timestamp) VALUES ('work', CURRENT_TIMESTAMP());
INSERT INTO todolist(description, timestamp) VALUES ('study', CURRENT_TIMESTAMP());
INSERT INTO todolist(description, timestamp) VALUES ('go home', CURRENT_TIMESTAMP());


INSERT INTO todolist(description, timestamp) VALUES ('sleep', CURRENT_TIMESTAMP());
INSERT INTO todolist(description, timestamp) VALUES ('clean', CURRENT_TIMESTAMP());
INSERT INTO todolist(description, timestamp) VALUES ('sleep', CURRENT_TIMESTAMP());

INSERT INTO todolist(description) VALUES ('Shop');

