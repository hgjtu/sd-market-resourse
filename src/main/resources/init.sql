CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS medias (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      file_name VARCHAR(255) NOT NULL,
      content_type VARCHAR(255) NOT NULL,
      object_key VARCHAR(255) NOT NULL,
      status VARCHAR(50) NOT NULL,
      created_at TIMESTAMP WITH TIME ZONE NOT NULL,
      updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    name_ru VARCHAR(100) NOT NULL,
    short_description VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category_media UUID REFERENCES medias(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS items (
    id BIGSERIAL PRIMARY KEY,
    category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price INTEGER CHECK (price >= 0),
    location VARCHAR(255),
    type VARCHAR(5) NOT NULL,
    publication_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS items_medias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
    media_id UUID REFERENCES medias(id) ON DELETE CASCADE,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    reply_comment_id BIGINT REFERENCES comments(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    likes INTEGER DEFAULT 0 CHECK (likes >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE OR REPLACE FUNCTION check_items_medias_limit()
    RETURNS TRIGGER AS '
    DECLARE
        record_count INTEGER;
    BEGIN
        SELECT COUNT(*) INTO record_count
        FROM items_medias
        WHERE item_id = NEW.item_id;

        IF record_count >= 10 THEN
            RAISE EXCEPTION ''Cannot add more than 10 medias for item_id %'', NEW.item_id
                USING HINT = ''Delete some existing medias for this item first'';
        END IF;

        RETURN NEW;
    END;
' LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_check_items_medias_limit ON items_medias;
CREATE TRIGGER trigger_check_items_medias_limit
    BEFORE INSERT ON items_medias
    FOR EACH ROW
EXECUTE FUNCTION check_items_medias_limit();
