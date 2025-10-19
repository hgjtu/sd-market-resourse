CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    name_ru VARCHAR(100) NOT NULL,
    short_description VARCHAR(255) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    id BIGSERIAL PRIMARY KEY,
    category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    images_urls TEXT[],
    price INTEGER CHECK (price >= 0),
    location VARCHAR(255),
    type VARCHAR(5) NOT NULL,
    publication_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    reply_comment_id BIGINT REFERENCES comments(id) ON DELETE SET NULL,
    content TEXT NOT NULL
);
