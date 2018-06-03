DROP SCHEMA BOOKSTORE;

CREATE SCHEMA BOOKSTORE;

USE BOOKSTORE;

# Books, publishers, and Authors tables.

## PUBILSHERS TABLES
# Check https://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#executeUpdate%28java.lang.String,%20int%29
# To obtain auto generated ID after insert.
CREATE TABLE IF NOT EXISTS PUBLISHERS (
	PUBLISHER_ID	        INT NOT NULL AUTO_INCREMENT,
    PUBLISHER_NAME	        VARCHAR(45) NOT NULL,

    PRIMARY KEY (PUBLISHER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLISHERS_PHONES (
	PUBLISHER_ID			INT NOT NULL,
    PUBLISHER_PHONE			VARCHAR(15) NOT NULL,
    
    UNIQUE(PUBLISHER_PHONE),
    PRIMARY KEY(PUBLISHER_ID, PUBLISHER_PHONE),
    FOREIGN KEY(PUBLISHER_ID) REFERENCES PUBLISHERS(PUBLISHER_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLISHERS_ADDRESSES (
	PUBLISHER_ID			INT NOT NULL,
    PUBLISHER_ADDRESS		VARCHAR(35) NOT NULL,
    
    UNIQUE(PUBLISHER_ADDRESS),
    PRIMARY KEY(PUBLISHER_ID, PUBLISHER_ADDRESS),
    FOREIGN KEY(PUBLISHER_ID) REFERENCES PUBLISHERS(PUBLISHER_ID) ON DELETE CASCADE ON UPDATE CASCADE
);
##

# Check https://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#executeUpdate%28java.lang.String,%20int%29
# To obtain auto generated ID after insert.
CREATE TABLE IF NOT EXISTS AUTHORS (
	AUTHOR_ID	            INT NOT NULL AUTO_INCREMENT,
    AUTHOR_NAME	            VARCHAR(45) NOT NULL,

    PRIMARY KEY (AUTHOR_ID)
);

#############################################################################
CREATE TABLE IF NOT EXISTS BOOKS (
	BOOK_ISBN               VARCHAR(15) NOT NULL,
    BOOK_TITLE	            VARCHAR(45) NOT NULL,
    PUBLISHER_ID			INT NOT NULL,
    PUBLICATION_YEAR	    YEAR NOT NULL,
    SELLING_PRICE	        DECIMAL(6, 2) NOT NULL,
    BOOK_CATEGORY	        VARCHAR(9) NOT NULL,
    MIN_THRESHOLD			INT UNSIGNED NOT NULL,
	QUANTITY				INT UNSIGNED NOT NULL,
    ORDER_QUANTITY			INT UNSIGNED NOT NULL,
    
    PRIMARY KEY (BOOK_ISBN),
    INDEX (BOOK_TITLE, PUBLICATION_YEAR),
	INDEX (BOOK_CATEGORY),
    INDEX (PUBLISHER_ID)
);

delimiter $$
create trigger POSITIVE_QUANTITY_INSERT before insert on BOOKS
for each row
begin
	if new.MIN_THRESHOLD < 0 OR new.QUANTITY < 0 then
		signal sqlstate '45000';
	end if;
end;$$
delimiter ;

delimiter $$
create trigger POSITIVE_QUANTITY_UPDATE before update on BOOKS
for each row
begin
	if new.MIN_THRESHOLD < 0 OR new.QUANTITY < 0 then
		signal sqlstate '45000';
	end if;
end;$$
delimiter ;

delimiter $$
create trigger BOOK_CATEGORY_INSERT before insert on BOOKS
for each row
begin
	if new.BOOK_CATEGORY <> "Science" AND new.BOOK_CATEGORY <> "Art" AND
		new.BOOK_CATEGORY <> "Religion" AND new.BOOK_CATEGORY <> "History" AND
        new.BOOK_CATEGORY <> "Geography" then
		signal sqlstate '45000';
	end if;
end;$$
delimiter ;

delimiter $$
create trigger BOOK_CATEGORY_UPDATE before update on BOOKS
for each row
begin
	if new.BOOK_CATEGORY <> "Science" AND new.BOOK_CATEGORY <> "Art" AND
		new.BOOK_CATEGORY <> "Religion" AND new.BOOK_CATEGORY <> "History" AND
        new.BOOK_CATEGORY <> "Geography" then
		signal sqlstate '45000';
	end if;
end;$$
delimiter ;

delimiter $$
create trigger BOOK_ORDER_BELOW_THRESHOLD after update on BOOKS
for each row
begin
	if new.QUANTITY < new.MIN_THRESHOLD AND old.QUANTITY >= new.MIN_THRESHOLD then
		INSERT into BOOK_ORDER(BOOK_ISBN, QUANTITY) VALUES(new.BOOK_ISBN, new.ORDER_QUANTITY);
    end if;
end;$$
delimiter ;
#############################################################################

CREATE TABLE IF NOT EXISTS BOOK_AUTHORS (
	BOOK_ISBN				VARCHAR(15) NOT NULL,
    AUTHOR_ID	     		INT NOT NULL,
    
    PRIMARY KEY(BOOK_ISBN, AUTHOR_ID),
    FOREIGN KEY(BOOK_ISBN) REFERENCES BOOKS(BOOK_ISBN) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(AUTHOR_ID) REFERENCES AUTHORS(AUTHOR_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX(AUTHOR_ID)
);

###########################################################################

# Check https://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#executeUpdate%28java.lang.String,%20int%29
# To obtain auto generated ID after insert.
CREATE TABLE IF NOT EXISTS BOOK_ORDER(
	ORDER_ID				INT NOT NULL AUTO_INCREMENT,
	BOOK_ISBN				VARCHAR(15) NOT NULL,
    QUANTITY				INT NOT NULL,
    ISSUE_TIME				TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY(ORDER_ID),
    FOREIGN KEY(BOOK_ISBN) REFERENCES BOOKS(BOOK_ISBN) ON DELETE CASCADE ON UPDATE CASCADE
);

delimiter $$
create trigger UPDATE_BOOK_QUANTITY_AFTER_ORDER before delete on BOOK_ORDER
for each row
begin
	INSERT INTO COMPLETE_ORDERS(ORDER_ID, BOOK_ISBN, QUANTITY, ISSUE_TIME) 
		VALUES(old.ORDER_ID, old.BOOK_ISBN, old.QUANTITY, old.ISSUE_TIME);
	UPDATE BOOKS
		SET BOOKS.QUANTITY = BOOKS.QUANTITY + old.QUANTITY
        where BOOKS.ISBN = old.BOOK_ISBN;
end;$$
delimiter ;

CREATE TABLE IF NOT EXISTS COMPLETE_ORDERS(
	ORDER_ID				INT NOT NULL,
	BOOK_ISBN				VARCHAR(15) NOT NULL,
    QUANTITY				INT NOT NULL,
    ISSUE_TIME				TIMESTAMP NOT NULL,
    COMPLETE_TIME			TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY(ORDER_ID),
    FOREIGN KEY(BOOK_ISBN) REFERENCES BOOKS(BOOK_ISBN) ON DELETE CASCADE ON UPDATE CASCADE
);


###########################################################################

# User table, It uses SHA1 hashing algorithm for the password.

CREATE TABLE IF NOT EXISTS USERS(
	USER_NAME				VARCHAR(20) NOT NULL,
    EMAIL					VARCHAR(20) NOT NULL,
    FIRST_NAME				VARCHAR(20) NOT NULL,
    LAST_NAME				VARCHAR(20) NOT NULL,
    PASSWORD_SHA1			VARCHAR(40) NOT NULL,
    PHONE_NUMBER			VARCHAR(15) NOT NULL,
	ADDRESS					VARCHAR(50) NOT NULL,
    IS_MANAGER				TINYINT(1) DEFAULT 0,
    
    PRIMARY KEY(USER_NAME),
    UNIQUE(EMAIL)
);
select * from USERS where USER_NAME = "12345678" AND PASSWORD_SHA1 = SHA1("12345678");

# Check https://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#executeUpdate%28java.lang.String,%20int%29
# To obtain auto generated ID after insert.
CREATE TABLE IF NOT EXISTS SHOPPING_ORDER (
	SHOPPING_ORDER_ID		INT NOT NULL AUTO_INCREMENT,
	USER_NAME					VARCHAR(20) NOT NULL,
    CHECKOUT_TIME			TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY(SHOPPING_ORDER_ID),
    FOREIGN KEY(USER_NAME) REFERENCES USERS(USER_NAME) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX(CHECKOUT_TIME)
);

# Event used to delete all the old data in the shopping order table.
# It basically runs every day once and delete every shopping order that is older than three months.\
CREATE EVENT remove_old_orders
ON SCHEDULE EVERY 1 DAY
DO
	DELETE FROM SHOPPING_ORDER WHERE CHECKOUT_TIME < NOW() - interval 3 MONTH;
    
    
CREATE TABLE IF NOT EXISTS SHOPPING_ORDER_ITEMS (
	SHOPPING_ORDER_ID		INT NOT NULL,
    BOOK_ISBN				VARCHAR(20) NOT NULL,
    QUANTITY				INT DEFAULT 1,
    
    PRIMARY KEY(SHOPPING_ORDER_ID, BOOK_ISBN),
    FOREIGN KEY(SHOPPING_ORDER_ID) REFERENCES SHOPPING_ORDER(SHOPPING_ORDER_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(BOOK_ISBN) REFERENCES BOOKS(BOOK_ISBN) ON DELETE CASCADE ON UPDATE CASCADE
);

delimiter $$
create trigger REMOVE_ITEMS_FOR_BOOKS before insert on SHOPPING_ORDER_ITEMS
for each row
begin
	UPDATE BOOKS
		SET BOOKS.QUANTITY = BOOKS.QUANTITY - new.QUANTITY
        where BOOKS.ISBN = new.BOOK_ISBN AND BOOKS.QUANTITY >= new.QUANTITY;
	if(ROW_COUNT() = 0) then
		signal sqlstate '45000';
	end if;
end;$$
delimiter ;