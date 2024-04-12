# Mobility System

## Pre-requisites

- Node.js 10

## API documentation


Access http://localhost:7001/api-docs

## Setup Database

First create the container:

```bash
docker run --name app-postgres \
             --env POSTGRES_USER=cosn \
             --env POSTGRES_PASSWORD=cosn \
             --env POSTGRES_DB=cosn \
             --publish 7777:5432 \
             --detach \
             postgres:13
```

Connect to it using a database client and execute the SQL:

```sql
DROP TABLE IF EXISTS WORKFLOW_ALGORITHMS;
DROP TABLE IF EXISTS ALGORITHMS;
DROP TABLE IF EXISTS WORKFLOWS;

CREATE TABLE WORKFLOWS(
   id uuid primary key,
   created_at timestamp not null default current_timestamp,
   updated_At timestamp not null default current_timestamp
);

CREATE TABLE ALGORITHMS(
   id uuid primary key,
   name varchar(255) not null,
   description varchar(255),
   command varchar(255),
   created_at timestamp not null default current_timestamp,
   updated_At timestamp not null default current_timestamp
);

CREATE TABLE WORKFLOW_ALGORITHMS(
   workflow_id uuid not null,
   algorithm_id uuid not null,
   "order" int not null,
   created_at timestamp not null default current_timestamp,
   updated_At timestamp not null default current_timestamp,
   PRIMARY KEY(workflow_id, algorithm_id),
   CONSTRAINT fk_workflow_algorithms_workflow_id
      FOREIGN KEY(workflow_id) 
	      REFERENCES WORKFLOWS(id) ON DELETE CASCADE,
   CONSTRAINT fk_workflow_algorithms_algorithm_id
      FOREIGN KEY(algorithm_id) 
	      REFERENCES ALGORITHMS(id) ON DELETE CASCADE
);

INSERT INTO public.ALGORITHMS
VALUES ('a25943c4-3f22-4715-82e7-581e1591481c', 'Algoritmo1', 'Descrição do algoritmo1.', 'Comando - algoritmo1.');

INSERT INTO public.ALGORITHMS
VALUES ('8dd064b7-c208-45c8-912a-67fd7409a74b', 'Algoritmo2', 'Descrição do algoritmo2.', 'Comando - algoritmo2.');

INSERT INTO public.ALGORITHMS
VALUES ('6593c6a8-2c93-4716-82f4-853358c6dece', 'Algoritmo3', 'Descrição do algoritmo3.', 'Comando - algoritmo3.');


INSERT INTO public.ALGORITHMS
VALUES ('54334166-de61-430d-8400-44592b301020', 'Algoritmo4', 'Descrição do algoritmo4.','Comando - algoritmo4.');


INSERT INTO public.ALGORITHMS
VALUES ('793dde63-1dea-4b3e-93ce-69898f7480ee', 'Algoritmo5', 'Descrição do algoritmo5.','Comando - algoritmo5.');

INSERT INTO public.ALGORITHMS
VALUES ('3d90b75d-7060-4825-bebe-eff4d8d7f765', 'Algoritmo6','Descrição do algoritmo6.', 'Comando - algoritmo6.');

INSERT INTO public.WORKFLOWS VALUES ('c91efcdf-f3c6-48bd-b06a-adbb8d37e9a6');

INSERT INTO public.WORKFLOW_ALGORITHMS(workflow_id, algorithm_id, "order") 
VALUES ('c91efcdf-f3c6-48bd-b06a-adbb8d37e9a6', 'a25943c4-3f22-4715-82e7-581e1591481c', 1);

INSERT INTO public.WORKFLOW_ALGORITHMS(workflow_id, algorithm_id, "order") 
VALUES ('c91efcdf-f3c6-48bd-b06a-adbb8d37e9a6', '6593c6a8-2c93-4716-82f4-853358c6dece', 2);

INSERT INTO public.WORKFLOW_ALGORITHMS(workflow_id, algorithm_id, "order") 
VALUES ('c91efcdf-f3c6-48bd-b06a-adbb8d37e9a6', '3d90b75d-7060-4825-bebe-eff4d8d7f765', 3);

INSERT INTO public.WORKFLOW_ALGORITHMS(workflow_id, algorithm_id, "order") 
VALUES ('c91efcdf-f3c6-48bd-b06a-adbb8d37e9a6', '54334166-de61-430d-8400-44592b301020', 4);

```

## Start the api

Make sure to run at least once every time you fetch new code from the repository

```bash
npm install
```

Make sure the database container is running:

```bash
docker start app-postgres
```

Then start the server in dev mode:

```bash
npm run watch
```

Then start the server in production mode:

```bash
npm start
```

### Heroku Setup

```
heroku login
heroku apps:create algorithmservice
heroku addons:create heroku-postgresql:hobby-dev

heroku config:set PG_DB=test
heroku config:set PG_USER=test
heroku config:set PG_PASSWORD=test
heroku config:set PG_HOST=test
heroku config:set PG_PORT=test

heroku config:set PG_SSL=true

heroku git:remote -a algorithmservice

git add .
git commit -am "initial commit"
git push heroku dev:master
```

### Destroy the App

```
heroku apps:destroy --confirm algorithmservice
```