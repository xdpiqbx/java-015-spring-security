ALTER TABLE "user" ADD COLUMN password VARCHAR(200) DEFAULT 'user';
ALTER TABLE "user" ADD COLUMN authority VARCHAR (100) DEFAULT 'USER';

INSERT INTO "user"(email, full_name, birthday, gender, password, authority)
    VALUES
    ('ben@mail.com', 'Big Ben', '2002-01-02', 'male', '{noop}user_pass', 'USER'),
    ('hector@mail.com', 'Hector Snober', '1991-05-11', 'male', '{bcrypt}$2a$10$u2wQg.6NvDKPALEO6qULgeQbLvgagjdB7LitQJDczkDt4qd.Jdeiu', 'ADMIN,USER');

INSERT INTO user_address (email, address)
        VALUES
        ('ben@mail.com', 'Kyiv'),
        ('hector@mail.com', 'Ohio');

--INSERT INTO "user"(email, full_name, birthday, gender, password, authority)
--    VALUES
--    ('ben@mail.com', 'Big Ben', '2002-01-02', 'male', 'user_pass', 'USER') ON CONFLICT (email) DO NOTHING,
--    ('hector@mail.com', 'Hector Snober', '1991-05-11', 'male', 'admin_pass', 'ADMIN,USER') ON CONFLICT (email) DO NOTHING;
