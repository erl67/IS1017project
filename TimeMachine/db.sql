USE erl67is1017;

CREATE TABLE IF NOT EXISTS wx_user (
	id INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    user_name VARCHAR(20) NOT NULL UNIQUE,
    user_pass VARCHAR(20) NOT NULL
);

INSERT INTO wx_user(user_name, user_pass) VALUES ('Guest', '');
INSERT INTO wx_user(user_name, user_pass) VALUES ('Eric', '123');
INSERT INTO wx_user(user_name, user_pass) VALUES ('Andy', '123');
INSERT INTO wx_user(user_name, user_pass) VALUES ('Noah', '123');
INSERT INTO wx_user(user_name, user_pass) VALUES ('Tahim', '123');
INSERT INTO wx_user(user_name, user_pass) VALUES ('1', '1');

CREATE TABLE IF NOT EXISTS wx_data (
    id INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	latitude VARCHAR(50),
    longitude VARCHAR(50),
	timezone VARCHAR(50),
	offset smallint,
	time int,
	summary VARCHAR(100),
	icon VARCHAR(50),
	sunriseTime int,
    sunsetTime int,
	moonphase DOUBLE,
	precipIntensity DOUBLE,
	precipIntensityMax DOUBLE,
	precipIntensityMaxTime DOUBLE,
	precipIntensityProbability DOUBLE,
	precipType VARCHAR(30),
	temperatureMin DOUBLE,
    temperatureMinTime int,
    temperatureMax DOUBLE,
    temperatureMaxTime int,
    apparentTemperatureMin DOUBLE,
    apparentTemperatureMinTime int,
    apparentTemperatureMax DOUBLE,
    apparentTemperatureMaxTime int,
    dewPoint DOUBLE,
    humidity DOUBLE,
    windSpeed DOUBLE,
    windBearing INT,
    visibility DOUBLE,
    cloudCover DOUBLE,
    pressure DOUBLE,
	fk_user_id INT,
	CONSTRAINT fk_uid FOREIGN KEY (fk_user_id) REFERENCES wx_user (id)
);