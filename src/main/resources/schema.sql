CREATE TABLE IF NOT EXISTS TODOITEM(ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL, TITLE VARCHAR2(255), DESCRIPTION VARCHAR2(255), STATUS VARCHAR2(255));

CREATE TABLE IF NOT EXISTS STATUS(NAME VARCHAR2(255) PRIMARY KEY NOT NULL);