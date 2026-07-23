-- ═══════════════════════════════════════════════════════════
-- BLOOM STORE — MySQL Setup Script
-- Run this ONCE before starting the app.
-- ═══════════════════════════════════════════════════════════

-- 1. Create and select database
CREATE DATABASE IF NOT EXISTS bloom_store;
USE bloom_store;

-- 2. Tables are auto-created by Hibernate (hbm2ddl.auto=update)
--    Run this file just for seed data after first startup.

-- ── SEED: Users ─────────────────────────────────────────────
-- Password for all seed users = "bloom123" (SHA-256 hashed with Base64 encoding)
INSERT IGNORE INTO users (first_name, last_name, email, password, role) VALUES
('Admin', 'Bloom',  'admin@bloom.com',  'mOwMkAd8bUE6ZlAiVgtWd3NlhUuixooZkKSPQMnOJS0=', 'ADMIN'),
('Arjun', 'Sharma', 'arjun@example.com','mOwMkAd8bUE6ZlAiVgtWd3NlhUuixooZkKSPQMnOJS0=', 'USER'),
('Priya', 'Gupta',  'priya@example.com','mOwMkAd8bUE6ZlAiVgtWd3NlhUuixooZkKSPQMnOJS0=', 'USER');

UPDATE users
SET password = 'mOwMkAd8bUE6ZlAiVgtWd3NlhUuixooZkKSPQMnOJS0='
WHERE email IN ('admin@bloom.com', 'arjun@example.com', 'priya@example.com');

-- ── SEED: Products ──────────────────────────────────────────
INSERT IGNORE INTO products (name, description, price, stock, category, is_featured) VALUES
-- Plants
('Monstera Deliciosa',   'The classic split-leaf plant. Easy to care for, stunning indoors.', 799.00, 30, 'Plants', 1),
('Peace Lily',           'Elegant white blooms. Thrives in low light. Air-purifying.',         549.00, 25, 'Plants', 1),
('Snake Plant',          'Near-indestructible. Perfect for beginners. Filters air at night.',   449.00, 40, 'Plants', 1),
('Pothos Golden',        'Trailing vines with golden-green leaves. Extremely low maintenance.',  299.00, 50, 'Plants', 1),
('Rubber Plant',         'Bold, glossy dark leaves. A statement piece for any room.',           699.00, 20, 'Plants', 0),
('Fiddle Leaf Fig',      'Dramatic, architectural leaves loved by interior designers.',         999.00,  8, 'Plants', 1),
('ZZ Plant',             'Glossy leaves, drought-tolerant. Virtually indestructible.',          599.00, 35, 'Plants', 0),
('Bird of Paradise',     'Tropical showstopper with enormous paddle-shaped leaves.',           1299.00,  5, 'Plants', 0),

-- Pots
('Terracotta Classic Pot',  'Handmade unglazed terracotta. Breathable for roots.',             349.00, 60, 'Pots', 1),
('Ceramic Matte White',     'Minimal matte white glaze. Drainage hole included.',              499.00, 45, 'Pots', 0),
('Woven Rattan Basket',     'Natural rattan basket planter. Fits 6-inch pots.',                399.00, 30, 'Pots', 1),
('Cement Cylinder Pot',     'Industrial-style concrete finish. Heavy and sturdy.',             599.00, 20, 'Pots', 0),

-- Care
('Organic Liquid Fertiliser', 'All-purpose NPK blend. Safe for indoors. 500ml bottle.',        199.00, 80, 'Care', 1),
('Well-Draining Potting Mix', 'Perlite-enriched soil. Excellent drainage for tropicals.',      299.00, 60, 'Care', 0),
('Neem Oil Spray',            'Natural pest control. Cold-pressed neem. 250ml.',               149.00, 90, 'Care', 1),
('Pebble Tray Set',           'Humidity tray with decorative pebbles. Set of 2.',              249.00, 40, 'Care', 0);

INSERT IGNORE INTO products (name, description, price, stock, category, is_featured) VALUES
('Areca Palm',              'Feathery indoor palm that brightens living rooms and balconies.', 899.00, 18, 'Plants', 1),
('Calathea Orbifolia',      'Large striped leaves with a soft tropical look for shaded rooms.', 749.00, 14, 'Plants', 1),
('Hanging Macrame Planter', 'Cotton macrame hanger with a compact planter for trailing vines.', 449.00, 32, 'Pots', 1),
('Self-Watering Pot',       'Modern planter with a water reservoir for easy plant care.',       649.00, 24, 'Pots', 0),
('Leaf Shine Spray',        'Gentle leaf cleaner that removes dust and adds a natural finish.', 179.00, 55, 'Care', 0),
('Mini Gardening Tool Set', 'Small rake, trowel, and pruning snip set for indoor gardening.',   399.00, 28, 'Care', 1);

-- ── VERIFICATION ────────────────────────────────────────────
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1614594975525-e45190c55d0b?auto=format&fit=crop&w=800&q=80' WHERE name = 'Monstera Deliciosa';
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1598880940080-ff9a29891b85?auto=format&fit=crop&w=800&q=80' WHERE name = 'Peace Lily';
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1593691509543-c55fb32d8de5?auto=format&fit=crop&w=800&q=80' WHERE name = 'Snake Plant';
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1622398925373-3f91b1e275f5?auto=format&fit=crop&w=800&q=80' WHERE name = 'Pothos Golden';
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1463320726281-696a485928c7?auto=format&fit=crop&w=800&q=80' WHERE name IN ('Rubber Plant', 'Fiddle Leaf Fig', 'ZZ Plant', 'Bird of Paradise');
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1604762524889-3e2fcc145683?auto=format&fit=crop&w=800&q=80' WHERE name = 'Areca Palm';
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1616500285636-f5dca1191dbe?auto=format&fit=crop&w=800&q=80' WHERE name = 'Calathea Orbifolia';
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1485955900006-10f4d324d411?auto=format&fit=crop&w=800&q=80' WHERE name = 'Terracotta Classic Pot';
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1602923668104-8f483c6e8f82?auto=format&fit=crop&w=800&q=80' WHERE name = 'Hanging Macrame Planter';
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&w=800&q=80' WHERE name IN ('Self-Watering Pot', 'Ceramic Matte White', 'Woven Rattan Basket', 'Cement Cylinder Pot');
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1611073061165-68a6f8080f29?auto=format&fit=crop&w=800&q=80' WHERE name IN ('Organic Liquid Fertiliser', 'Neem Oil Spray', 'Leaf Shine Spray');
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1416879595882-3373a0480b5b?auto=format&fit=crop&w=800&q=80' WHERE name = 'Mini Gardening Tool Set';
UPDATE products SET image_url = 'https://images.unsplash.com/photo-1459156212016-c812468e2115?auto=format&fit=crop&w=800&q=80' WHERE name IN ('Well-Draining Potting Mix', 'Pebble Tray Set');

SELECT 'Users:'    AS '', COUNT(*) FROM users;
SELECT 'Products:' AS '', COUNT(*) FROM products;
