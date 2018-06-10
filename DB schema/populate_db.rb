require 'mysql2'
require 'faker'

$PUBLISHER_COUNT = 50_000
$AUTHOR_COUNT = 200_000
$BOOK_COUNT = 1000_000
$USER_COUNT = 100_000
$SHOPPING_ORDER_COUNT_PER_USER = 1
$ITEM_PER_SHOPPING_ORDER = 2

$batched_queries = Array.new
$MAX_BATCH_SIZE = 2000

Faker::Config.random = nil # seeds the PRNG using default entropy sources

def mysql_client()
  Mysql2::Client.new(
    host: '127.0.0.1',
    username: 'root',
    password: 'admin',
    port: '3306',
    database: 'bookstore',
    encoding: 'utf8',
    connect_timeout: 50000,
    flags: Mysql2::Client::MULTI_STATEMENTS
  )
end

def batch_query(client, query)
  $batched_queries << query
  if $batched_queries.size >= $MAX_BATCH_SIZE
    execute_batched_queries(client)
  end
end

def execute_batched_queries(client)
  if $batched_queries.empty?
    return
  end
  while client.next_result
  end
  client.query($batched_queries.join(";") + ";")
  $batched_queries.clear
end

def start_populate_message(table_name)
  "[" + table_name + "] Started to populate the Table"
end

def end_populate_message(table_name)
   "[" + table_name + "] Ended successfully populating"
end

def populate_publisher_table(client)
  table_name = "PUBLISHER"
  statement = "insert into PUBLISHER(NAME)VALUES(\"%s\")"
  puts(start_populate_message(table_name))
  $PUBLISHER_COUNT.times do
    publisher_name = Faker::Company.name
    batch_query(client, statement%[publisher_name])
  end
  execute_batched_queries(client)
  puts(end_populate_message(table_name))
end

def populate_publisher_phone_table(client)
  table_name = "PUBLISHER_PHONE"
  statement = "INSERT INTO PUBLISHER_PHONE(ID, PHONE)VALUES(%d, \"%d\")"
  puts(start_populate_message(table_name))
  phone = 00201000000000
  $PUBLISHER_COUNT.times do |v|
    batch_query(client, statement%[v + 1, phone])
    phone = phone + 1
  end
  execute_batched_queries(client)
  puts(end_populate_message(table_name))
end

def populate_publisher_address_table(client)
  table_name = "PUBLISHER_ADDRESS"
  statement = "INSERT INTO PUBLISHER_ADDRESS(ID, STREET, CITY, COUNTRY) VALUES(%d, \"%s\", \"%s\", \"%s\")"
  puts(start_populate_message(table_name))
  $PUBLISHER_COUNT.times do |v|
    street = Faker::Address.street_address
    city = Faker::Address.city
    country = Faker::Address.country
    batch_query(client, statement%[v+1, street, city, country])
  end
  execute_batched_queries(client)
  puts(end_populate_message(table_name))
end

def populate_author_table(client)
  table_name = "AUTHOR"
  statement = "INSERT INTO AUTHOR(NAME)VALUES(\"%s\")"
  puts(start_populate_message(table_name))
  $AUTHOR_COUNT.times do
    author_name = Faker::Book.author
    batch_query(client, statement%[author_name])
  end
  execute_batched_queries(client)
  puts(end_populate_message(table_name))
end

def populate_user_table(client)
  table_name = "CUSTOMER"
  statement = "INSERT INTO CUSTOMER(USER_NAME, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD_SHA1, PHONE_NUMBER, STREET, CITY, COUNTRY)"\
			 "VALUES(\"%s\", \"%s\", \"%s\", \"%s\", SHA1(\"%s\"), \"%s\", \"%s\", \"%s\", \"%s\")"
  puts(start_populate_message(table_name))
  phone = 00201005000000
  index = 1;
  $USER_COUNT.times do
    index = index + 1
    user_name = Faker::Internet.user_name + index.to_s
    email = user_name + "@gmail.com"
    first_name = Faker::Name.name
    last_name = Faker::Name.name
    password = "password"
    phone = phone + 5
    street = Faker::Address.street_address
    city = Faker::Address.city
    country = Faker::Address.country
    batch_query(client, statement%[user_name, email, first_name, last_name, password, phone, street, city, country])
  end
  execute_batched_queries(client)
  puts(end_populate_message(table_name))
end

def populate_book_table(client)
  table_name = "BOOK"
  statement = "INSERT INTO BOOK(ISBN, TITLE, PUBLISHER_ID, PUBLICATION_YEAR, SELLING_PRICE, CATEGORY, MIN_THRESHOLD, QUANTITY)\
   VALUES(\"%s\",\"%s\",%d,%d,%d,%d,%d,%d)"
  puts(start_populate_message(table_name))
  publisher_id = 1
  publication_year = 2000
  max_price = 80
  min_price = 50
  category = 1
  category_count = 5
  File.open('ISBN_10.txt').each do |line|
    isbn = line;
    title = Faker::Book.title
    publisher_id = 1 + (publisher_id % ($PUBLISHER_COUNT))
    publication_year = publication_year == 2018 ? 2000 : publication_year + 1
    selling_price = min_price + (max_price - min_price) * rand
    category = 1 + (category % category_count)
    min_threshold = rand * 50
    quantity = min_threshold  + ($BOOK_COUNT/$USER_COUNT) + 30
    batch_query(client, statement%[isbn, title, publisher_id, publication_year, selling_price, category, min_threshold, quantity])
  end
  execute_batched_queries(client)
  puts(end_populate_message(table_name))
end

def populate_book_author_table(client)
  table_name = "BOOK_AUTHOR"
  statement = "INSERT INTO BOOK_AUTHOR (BOOK_ISBN, AUTHOR_ID) VALUES (\"%s\", \"%s\")"
  puts(start_populate_message(table_name))
  author_id = 1
  File.open('ISBN_10.txt').each do |line|
    isbn = line
    author1 = 1 + (rand * ($AUTHOR_COUNT - 1))
    author2 = 1 + (rand * ($AUTHOR_COUNT - 1))
    author_id = author2
    batch_query(client, statement%[isbn, author1])
    if author1 != author2 then batch_query(client, statement%[isbn, author2]) end
  end
  execute_batched_queries(client)
  puts(end_populate_message(table_name))
end


client = mysql_client
populate_publisher_table(client)
populate_publisher_phone_table(client)
populate_publisher_address_table(client)
populate_author_table(client)
populate_user_table(client)
populate_book_table(client)
populate_book_author_table(client)
