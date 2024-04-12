 -- Drop old schema

DROP TABLE IF EXISTS log CASCADE;
DROP TABLE IF EXISTS service CASCADE;
DROP TABLE IF EXISTS endpoint CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS jwt CASCADE;

DROP TYPE IF EXISTS operation_type;
DROP TYPE IF EXISTS message_type;

 -- Types

 CREATE TYPE operation_type AS ENUM ('CREATE', 'UPDATE', 'DELETE', 'READ', 'OTHER');
 CREATE TYPE message_type AS ENUM ('INFORMATION', 'DEBUG', 'OTHER', 'ERROR', 'FATAL');

 -- Tables

 CREATE TABLE log (
     id SERIAL PRIMARY KEY,
     timestamp TIMESTAMP,
     message TEXT,
     message_type message_type,
     operation_type operation_type 
 );

 CREATE TABLE service (
     name TEXT,
     ip INET,
     port INT,
     uuid SERIAL PRIMARY KEY
 );

 CREATE TABLE endpoint (
    uri TEXT
 );

 CREATE TABLE users (
    uuid SERIAL PRIMARY KEY,
    email TEXT
 );

 CREATE TABLE jwt (
    jwtToken TEXT
 );