CREATE SCHEMA playground_spring;

CREATE TABLE playground_spring.category (
    category_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    parent_category_id INTEGER,
    CONSTRAINT fk_parent_category
      FOREIGN KEY (parent_category_id)
      REFERENCES playground_spring.category(category_id)
);

CREATE TABLE playground_spring.product (
    product_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    product_name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    product_price DECIMAL(10, 2) NOT NULL,
    product_available BOOLEAN,
    category_id INTEGER NOT NULL,
    PRIMARY KEY (product_id),
    CONSTRAINT fk_category
      FOREIGN KEY (category_id)
      REFERENCES playground_spring.category(category_id)
);
