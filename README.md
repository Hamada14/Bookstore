# Bookstore
It's a JavaFx based application that utilizes an RPC framework, Jax, to connect to the backend.
That backend uses MySQL.

## Dependencies:

### Lombok:
That Jar is already added in the gradle configuration.
But to add Lombok support to Eclipse compiler download and run the [Lombok Jar](https://projectlombok.org/download)

### MySQL2:
MySQL2 gem is used to populate the Database.
run `gem install mysql2`

### Faker:
Faker gem is used to generate fake data to populate the database.
run `gem install faker`
## Usage:
### Faking database:
First you have to use the schema define in `DB Schema/schema script.sql` to build the tables.

Then run the following commands in the terminal to generate fake data and insert it into the tables.

```
cd 'DB schema/Populate Database'
gem install faker
gem install mysql2
ruby populate_db.rb
```
### Running the program:
1. Sync Gradle file to install the required dependencies such as `Lombok`, `MySQL connector`, `Jasper`
1. Run the Backend located in `Publisher.java`
1. Run the Client entry point located in `BookStoreApp.java`
1. Buy all the books you want (you booknerd -.-).

## License:
The code is licensed under an [MIT License](https://github.com/Hamada14/Bookstore/blob/master/LICENSE)
