CREATE TABLE user_address(
  email VARCHAR (255) PRIMARY KEY,
  address VARCHAR (255)
);

INSERT INTO "user"(email, full_name, birthday, gender)
    VALUES
    ('john.doe@mail.com', 'John Doe', '1998-01-02', 'male'),
    ('bill@mail.com', 'Bill Adic', '1993-01-02', 'male'),
    ('eli@mail.com', 'Elisa Mour', '2000-01-02', 'female'),
    ('hann@mail.com', 'Hanna White', '2002-01-02', 'female'),
    ('benji@mail.com', 'Benjamin Franch', '1991-01-02', 'male');

INSERT INTO user_address (email, address)
        VALUES
        ('john.doe@mail.com', 'New York'),
        ('bill@mail.com', 'London'),
        ('eli@mail.com', 'Pert'),
        ('hann@mail.com', 'Brisbane'),
        ('benji@mail.com', 'Alaska');