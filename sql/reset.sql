DROP TABLE IF EXISTS movie_actor_character_r;
DROP TABLE muff_likes_actor;
DROP TABLE muff_likes_character;
DROP TABLE IF EXISTS actor;
DROP TABLE IF EXISTS character;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS follows;
DROP TABLE IF EXISTS password;
DROP TABLE IF EXISTS muff;

/*
  Anything can be done with data as long as the sql constraints are followed
  The idea is to put max possible constraints in the sql space itself
  and give responsibility of the rest to the high level language above db
*/

/*            */
/* Muff stuff */
/*            */
-- handle should not be allowed to update
-- needs complex update chains
-- takes too long db operation
-- of course a row should be allowed to delete
-- in which case all the dependents are cascaded
-- BUT,
-- If we use another field id and use that as primary key
-- then the handle field does not have any dependents in terms of fks
-- then handle being unique is enough
-- now it can be easily changed without complex update chains or much db time
/*
  In general if primary key is unique and constant for a tuple through out its lifetime
  (from creation to deletion) then operations are simplified. So if there is a need to update any
  field which could be a primary key, one could create a (synthetic if you may) primary key field
  which can be unique and constant through out the life time of the tuple
*/
-- handle update can be provided
CREATE TABLE muff (
  id     SERIAL,
  handle VARCHAR(50) NOT NULL,
  name   VARCHAR(50) NOT NULL,
  UNIQUE (handle),
  PRIMARY KEY (id)
);

-- password update can be provided
CREATE TABLE password (
  id       INT,
  password VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES muff (id)
  ON DELETE CASCADE
);

-- follow and un-follow operations can be supported
CREATE TABLE follows (
  id1 INT,
  id2 INT,
  PRIMARY KEY (id1, id2),
  FOREIGN KEY (id1) REFERENCES muff (id)
  ON DELETE CASCADE,
  FOREIGN KEY (id2) REFERENCES muff (id)
  ON DELETE CASCADE
);

/*             */
/* Movie stuff */
/*             */
-- name update can be allowed
CREATE TABLE movie (
  id   SERIAL,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

-- name update can be allowed
CREATE TABLE actor (
  id   SERIAL,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

-- name update can be allowed
CREATE TABLE character (
  id   SERIAL,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

-- updates can be allowed
-- here an actor can play two characters in a movie
-- for ex.
-- prabhas in bahubali as shivudu (mahendra bahubali) and amarendra bahubali
CREATE TABLE movie_actor_character_r (
  id           SERIAL,
  movie_id     INT NOT NULL,
  actor_id     INT NOT NULL,
  character_id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (movie_id) REFERENCES movie (id)
  ON DELETE CASCADE,
  FOREIGN KEY (actor_id) REFERENCES actor (id)
  ON DELETE CASCADE,
  FOREIGN KEY (character_id) REFERENCES character (id)
  ON DELETE CASCADE,
  UNIQUE (movie_id, actor_id, character_id)
);

CREATE TABLE muff_likes_actor (
  muff_id  INT,
  actor_id INT,
  PRIMARY KEY (muff_id, actor_id),
  FOREIGN KEY (muff_id) REFERENCES muff (id)
  ON DELETE CASCADE,
  FOREIGN KEY (actor_id) REFERENCES actor (id)
  ON DELETE CASCADE
);

CREATE TABLE muff_likes_character (
  muff_id      INT,
  character_id INT,
  PRIMARY KEY (muff_id, character_id),
  FOREIGN KEY (muff_id) REFERENCES muff (id)
  ON DELETE CASCADE,
  FOREIGN KEY (character_id) REFERENCES character (id)
  ON DELETE CASCADE
);
