-- Catalogue des produits.
-- Les contraintes protègent les données même si une requête contourne l'API :
-- la base est la dernière ligne de défense.
CREATE TABLE product (
    id              BIGSERIAL      PRIMARY KEY,
    sku             VARCHAR(40)    NOT NULL,
    name            VARCHAR(120)   NOT NULL,
    description     VARCHAR(1000),
    unit_price      NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    stock_available INTEGER        NOT NULL DEFAULT 0 CHECK (stock_available >= 0),
    active          BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ    NOT NULL,
    updated_at      TIMESTAMPTZ    NOT NULL
);

-- Unicité du SKU sans tenir compte de la casse : "ABC-1" et "abc-1" sont le même produit
CREATE UNIQUE INDEX uk_product_sku ON product (LOWER(sku));
