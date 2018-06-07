DROP SCHEMA BOOKSTORE;

CREATE SCHEMA BOOKSTORE;

USE BOOKSTORE;

# Books, publishers, and Authors tables.

## PUBILSHERS TABLES
# Check https://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#executeUpdate%28java.lang.String,%20int%29
# To obtain auto generated ID after insert.
CREATE TABLE IF NOT EXISTS PUBLISHER (
    ID             INT NOT NULL AUTO_INCREMENT,
    NAME           VARCHAR(50) NOT NULL,

    PRIMARY KEY (ID)
);

#INSERT INTO PUBLISHERS(PUBLISHER_NAME)VALUES("PUBLISHER1");

CREATE TABLE IF NOT EXISTS PUBLISHER_PHONE (
    ID         INT NOT NULL,
    PHONE      VARCHAR(15) NOT NULL,

    UNIQUE(PHONE),
    PRIMARY KEY(ID, PHONE),
    FOREIGN KEY(ID) REFERENCES PUBLISHER(ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLISHERS_ADDRESS (
    ID        INT NOT NULL,
    STREET    VARCHAR(50) NOT NULL,
    CITY      VARCHAR(50) NOT NULL,
    COUNTRY   VARCHAR(50) NOT NULL,

    PRIMARY KEY(ID),
    FOREIGN KEY(ID) REFERENCES PUBLISHER(ID) ON DELETE CASCADE ON UPDATE CASCADE
);
##

# Check https://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#executeUpdate%28java.lang.String,%20int%29
# To obtain auto generated ID after insert.
CREATE TABLE IF NOT EXISTS AUTHOR (
    ID              INT NOT NULL AUTO_INCREMENT,
    FIRST_NAME      VARCHAR(50) NOT NULL,
    LAST_NAME       VARCHAR(50) NOT NULL,

    PRIMARY KEY (ID)
);

#INSERT INTO AUTHORS(AUTHOR_NAME)VALUES("AUTHOR1");

#############################################################################
CREATE TABLE IF NOT EXISTS BOOK_CATEGORY (
    ID          TINYINT NOT NULL AUTO_INCREMENT,
    CATEGORY    VARCHAR(15) NOT NULL,

    PRIMARY KEY(ID)
);
INSERT INTO BOOK_CATEGORY(CATEGORY) VALUES("Art");
INSERT INTO BOOK_CATEGORY(CATEGORY) VALUES("Geography");
INSERT INTO BOOK_CATEGORY(CATEGORY) VALUES("History");
INSERT INTO BOOK_CATEGORY(CATEGORY) VALUES("Religion");
INSERT INTO BOOK_CATEGORY(CATEGORY) VALUES("Science");

CREATE TABLE IF NOT EXISTS BOOK (
    ISBN                VARCHAR(15) NOT NULL,
    TITLE               VARCHAR(45) NOT NULL,
    PUBLISHER_ID        INT NOT NULL,
    PUBLICATION_YEAR    YEAR NOT NULL,
    SELLING_PRICE       DECIMAL(6, 2) NOT NULL,
    CATEGORY            TINYINT NOT NULL,
    MIN_THRESHOLD       INT UNSIGNED NOT NULL,
    QUANTITY            INT UNSIGNED NOT NULL DEFAULT 0,

    PRIMARY KEY (ISBN),
    FOREIGN KEY(PUBLISHER_ID) REFERENCES PUBLISHER(ID),
    FOREIGN KEY(CATEGORY) REFERENCES BOOK_CATEGORY(ID),
    INDEX (TITLE, PUBLICATION_YEAR),
    INDEX (CATEGORY),
    INDEX (PUBLISHER_ID)
);

#INSERT INTO BOOKS VALUES("ISBN1", "TITLE1", 1, 2017, 50, "Art", 20, 20, 20);

delimiter $$
create trigger POSITIVE_QUANTITY_INSERT before insert on BOOK
for each row
begin
  if new.MIN_THRESHOLD < 0 OR new.QUANTITY < 0 then
    signal sqlstate '45000';
  end if;
end;$$
delimiter ;

delimiter $$
create trigger POSITIVE_QUANTITY_UPDATE before update on BOOK
for each row
begin
  if new.MIN_THRESHOLD < 0 OR new.QUANTITY < 0 then
    signal sqlstate '45000';
  end if;
end;$$
delimiter ;

delimiter $$
create trigger BOOK_ORDER_BELOW_THRESHOLD after update on BOOK
for each row
begin
  if new.QUANTITY < new.MIN_THRESHOLD AND old.QUANTITY >= new.MIN_THRESHOLD then
    INSERT into BOOK_ORDER(BOOK_ISBN, QUANTITY) VALUES(new.ISBN, new.MIN_THRESHOLD);
  end if;
end;$$
delimiter ;

delimiter $$
create trigger BOOK_ORDER_BELOW_THRESHOLD_INSERT after insert on BOOK
for each row
begin
  if new.QUANTITY < new.MIN_THRESHOLD then
    INSERT into BOOK_ORDER(BOOK_ISBN, QUANTITY) VALUES(new.ISBN, new.MIN_THRESHOLD);
  end if;
end;$$
delimiter ;
#############################################################################

CREATE TABLE IF NOT EXISTS BOOK_AUTHOR (
    BOOK_ISBN          VARCHAR(15) NOT NULL,
    AUTHOR_ID          INT NOT NULL,

    PRIMARY KEY(BOOK_ISBN, AUTHOR_ID),
    FOREIGN KEY(BOOK_ISBN) REFERENCES BOOK(ISBN) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(AUTHOR_ID) REFERENCES AUTHOR(ID) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX(AUTHOR_ID)
);

###########################################################################

# Check https://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#executeUpdate%28java.lang.String,%20int%29
# To obtain auto generated ID after insert.
CREATE TABLE IF NOT EXISTS BOOK_ORDER(
    ORDER_ID        INT NOT NULL AUTO_INCREMENT,
    BOOK_ISBN       VARCHAR(15) NOT NULL,
    QUANTITY        INT NOT NULL,
    ISSUE_TIME      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(ORDER_ID),
    FOREIGN KEY(BOOK_ISBN) REFERENCES BOOK(ISBN) ON DELETE CASCADE ON UPDATE CASCADE
);

delimiter $$
create trigger UPDATE_BOOK_QUANTITY_AFTER_ORDER before delete on BOOK_ORDER
for each row
begin
  INSERT INTO COMPLETE_ORDER(ORDER_ID, BOOK_ISBN, QUANTITY, ISSUE_TIME)
    VALUES(old.ORDER_ID, old.BOOK_ISBN, old.QUANTITY, old.ISSUE_TIME);
  UPDATE BOOK
    SET BOOK.QUANTITY = BOOK.QUANTITY + old.QUANTITY
        where BOOK.ISBN = old.BOOK_ISBN;
end;$$
delimiter ;

CREATE TABLE IF NOT EXISTS COMPLETE_ORDER(
    ORDER_ID          INT NOT NULL,
    BOOK_ISBN         VARCHAR(15) NOT NULL,
    QUANTITY          INT NOT NULL,
    ISSUE_TIME        TIMESTAMP NOT NULL,
    COMPLETE_TIME     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(ORDER_ID),
    FOREIGN KEY(BOOK_ISBN) REFERENCES BOOK(ISBN) ON DELETE CASCADE ON UPDATE CASCADE
);


###########################################################################

# User table, It uses SHA1 hashing algorithm for the password.

CREATE TABLE IF NOT EXISTS CUSTOMER (
    USER_NAME         VARCHAR(50) NOT NULL,
    EMAIL             VARCHAR(50) NOT NULL,
    FIRST_NAME        VARCHAR(50) NOT NULL,
    LAST_NAME         VARCHAR(50) NOT NULL,
    PASSWORD_SHA1     VARCHAR(50) NOT NULL,
    PHONE_NUMBER      VARCHAR(20) NOT NULL,
	STREET            VARCHAR(50) NOT NULL,
    CITY              VARCHAR(50) NOT NULL,
    COUNTRY           VARCHAR(50) NOT NULL,

    PRIMARY KEY(USER_NAME),
    UNIQUE(EMAIL)
);
use bookstore;
insert into customer values("usernamee", "email", "first", "last", "password", "phone", "street", "city", "country");
CREATE TABLE IF NOT EXISTS MANAGER (
    USER_NAME        VARCHAR(20) NOT NULL,

    PRIMARY KEY(USER_NAME),
    FOREIGN KEY(USER_NAME) REFERENCES CUSTOMER(USER_NAME) on DELETE CASCADE ON UPDATE CASCADE
);

#INSERT INTO USERS VALUES("USER1", "EMAIL1", "FIRST1", "LAST1", "PASSWORD1", "NUMBER1", "ADDRESS1", 0);

# Check https://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#executeUpdate%28java.lang.String,%20int%29
# To obtain auto generated ID after insert.
CREATE TABLE IF NOT EXISTS SHOPPING_ORDER (
    ID                INT NOT NULL AUTO_INCREMENT,
    USER_NAME         VARCHAR(20) NOT NULL,
    CHECKOUT_TIME     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(ID),
    FOREIGN KEY(USER_NAME) REFERENCES CUSTOMER(USER_NAME) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX(CHECKOUT_TIME)
);

#INSERT INTO SHOPPING_ORDER(USER_NAME) VALUES("USER1");
#INSERT INTO SHOPPING_ORDER(USER_NAME) VALUES("USER1");


# Event used to delete all the old data in the shopping order table.
# It basically runs every day once and delete every shopping order that is older than three months.\
CREATE EVENT remove_old_orders
ON SCHEDULE EVERY 1 DAY
DO
  DELETE FROM SHOPPING_ORDER WHERE CHECKOUT_TIME < NOW() - interval 3 MONTH;

use bookstore;
CREATE TABLE IF NOT EXISTS SHOPPING_ORDER_ITEM (
    SHOPPING_ORDER_ID    INT NOT NULL,
    BOOK_ISBN            VARCHAR(20) NOT NULL,
    QUANTITY             INT DEFAULT 1,
    SELLING_PRICE        DECIMAL(8, 2) NOT NULL,

    PRIMARY KEY(SHOPPING_ORDER_ID, BOOK_ISBN),
    FOREIGN KEY(SHOPPING_ORDER_ID) REFERENCES SHOPPING_ORDER(ID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(BOOK_ISBN) REFERENCES BOOK(ISBN) ON DELETE CASCADE ON UPDATE CASCADE
);
#INSERT INTO SHOPPING_ORDER_ITEMS VALUES(1, "ISBN1", 1, 70);
#INSERT INTO SHOPPING_ORDER_ITEMS VALUES(2, "ISBN1", 1, 30);

delimiter $$
create trigger PROCESS_BOOK_CHECKOUT before insert on SHOPPING_ORDER_ITEM
for each row
begin
  UPDATE BOOK
    SET BOOK.QUANTITY = BOOK.QUANTITY - new.QUANTITY
        where BOOK.ISBN = new.BOOK_ISBN AND BOOK.QUANTITY >= new.QUANTITY;
  if(ROW_COUNT() = 0) then
    signal sqlstate '45000';
  end if;
end;$$
delimiter ;
