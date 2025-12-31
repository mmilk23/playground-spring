--DELETE FROM category;
SET SCHEMA playground_spring;

-- Insere categorias principais (sem categoria pai)
INSERT INTO category (category_name, parent_category_id) VALUES ('Electronics', NULL);
INSERT INTO category (category_name, parent_category_id) VALUES ('Books', NULL);
INSERT INTO category (category_name, parent_category_id) VALUES ('Clothers', NULL);

-- Insere subcategorias (com categoria pai)
-- Para referenciar a categoria pai, usamos o ID das categorias principais inseridas anteriormente
INSERT INTO category (category_name, parent_category_id) VALUES ('Mobile Phones', 1); -- 'Electronics' com ID 1
INSERT INTO category (category_name, parent_category_id) VALUES ('Laptops', 1); -- 'Electronics' com ID 1
INSERT INTO category (category_name, parent_category_id) VALUES ('Fiction', 2); -- 'Books' com ID 2
INSERT INTO category (category_name, parent_category_id) VALUES ('Non-Fiction', 2); -- 'Books' com ID 2


INSERT INTO product (product_name, description, category_id, product_price, product_available) VALUES ('Amazon Kindle', 'Amazon Kindle is a eletronic book reader', 1, 99.9, true);
INSERT INTO product (product_name, description, category_id, product_price, product_available)  VALUES ('Apple Iphone', 'A most expansive phone ever made', 2, 899.9, true);
INSERT INTO product (product_name, description, category_id, product_price, product_available) VALUES ('The Hitchhiker''s Guide to the Galaxy', '42 is the answer to the "Ultimate Question of Life, the Universe, and Everything"', 5, 9.9, true);
INSERT INTO product (product_name, description, category_id, product_price, product_available) VALUES ('Sapiens: A Brief History of Humankind', 'Author: Yuval Noah Harari', 6,  29, false);

