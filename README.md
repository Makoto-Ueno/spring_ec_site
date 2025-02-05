## MySQL
- ユーザー名：spring_ec_training
- パスワード：password
- データベース：spring_ec_training_db

### テーブル作成SQL

```sql
# user
CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    mail VARCHAR(400) UNIQUE NOT NULL,
    password VARCHAR(400) NOT NULL,
    type INT NOT NULL,
    create_at DATETIME NOT NULL,
    update_at DATETIME NOT NULL
);

# product
CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    amount INT NOT NULL,
    description VARCHAR(300) NOT NULL,
    image_url VARCHAR(100),
    status INT NOT NULL,
    create_at DATETIME NOT NULL,
    create_user INT NOT NULL,
    update_at DATETIME NOT NULL,
    update_user INT NOT NULL
);

# stock
CREATE TABLE stock (
    product_id INT PRIMARY KEY,
    quantity INT NOT NULL,
    create_at DATETIME NOT NULL,
    create_user INT NOT NULL,
    update_at DATETIME NOT NULL,
    update_user INT NOT NULL,
    FOREIGN KEY(product_id) REFERENCES product(product_id)
);

# cart
CREATE TABLE cart (
    user_id INT,
    product_id INT,
    quantity INT NOT NULL,
    create_at DATETIME NOT NULL,
    create_user INT NOT NULL,
    update_at DATETIME NOT NULL,
    update_user INT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES user(user_id),
    FOREIGN KEY(product_id) REFERENCES product(product_id),
    PRIMARY KEY(user_id,product_id)
);

# order_info
CREATE TABLE IF NOT EXISTS order_info (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount INT NOT NULL,
    create_at DATETIME NOT NULL,
    create_user INT NOT NULL,
    update_at DATETIME NOT NULL,
    update_user INT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES user(user_id)
);

# order_detail
CREATE TABLE IF NOT EXISTS order_detail (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    amount INT NOT NULL,
    quantity INT NOT NULL,
    create_at DATETIME NOT NULL,
    create_user INT NOT NULL,
    update_at DATETIME NOT NULL,
    update_user INT NOT NULL,
    FOREIGN KEY(order_id) REFERENCES order_info(order_id),
    FOREIGN KEY(product_id) REFERENCES product(product_id)
);

# order_delivery
CREATE TABLE IF NOT EXISTS order_delivery (
    order_id INT PRIMARY KEY,
    address VARCHAR(1000) NOT NULL,
    create_at DATETIME NOT NULL,
    create_user INT NOT NULL,
    update_at DATETIME NOT NULL,
    update_user INT NOT NULL,
    FOREIGN KEY(order_id) REFERENCES order_info(order_id)
);
```
