 -- Drop old schema

DROP TABLE IF EXISTS workflow CASCADE;
DROP TABLE IF EXISTS algorithm CASCADE;

 -- Tables

 CREATE TABLE workflow (
     id SERIAL PRIMARY KEY,
     created_at TIMESTAMP,
     updated_at TIMESTAMP
 );

 CREATE TABLE algorithm (
     id SERIAL PRIMARY KEY,
     name TEXT,
     description TEXT,
     command TEXT,
     created_at TIMESTAMP,
     updated_at TIMESTAMP
 );