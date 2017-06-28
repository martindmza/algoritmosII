USE algoritmos;

DROP TABLE IF EXISTS tipo_direccion;
CREATE TABLE tipo_direccion (
  id_tipo_direccion INT(11) NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(255),
  PRIMARY KEY (`id_tipo_direccion`)
);

DROP TABLE IF EXISTS direccion;
CREATE TABLE direccion (
  id_direccion INT(11) NOT NULL AUTO_INCREMENT,
  calle VARCHAR(255),
  numero INTEGER,
  PRIMARY KEY (`id_direccion`)
);
                      
DROP TABLE IF EXISTS tipo_ocupacion;                      
CREATE TABLE tipo_ocupacion(
  id_tipoocupacion INT(11) NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(255),
  PRIMARY KEY (`id_tipoocupacion`)
);
                          
DROP TABLE IF EXISTS ocupacion;
CREATE TABLE ocupacion(
  id_ocupacion INT(11) NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(255),
  id_tipoocupacion INTEGER,
  PRIMARY KEY (`id_ocupacion`)
);

DROP TABLE IF EXISTS persona;
CREATE TABLE persona (
  id_persona INT(11) NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  id_ocupacion INTEGER,
  PRIMARY KEY (`id_persona`)
);

DROP TABLE IF EXISTS persona_direccion;
CREATE TABLE persona_direccion(
  id_persona_direccion INT(11) NOT NULL AUTO_INCREMENT,
  id_persona INTEGER,
  id_direccion INTEGER,
  id_tipo_direccion INTEGER,
  PRIMARY KEY (`id_persona_direccion`)
);
          
-- TABLE: tipo_ocupacion
INSERT INTO tipo_ocupacion (id_tipoocupacion,descripcion) VALUES (1,'Oficio');
INSERT INTO tipo_ocupacion (id_tipoocupacion,descripcion) VALUES (2,'Profesional');
INSERT INTO tipo_ocupacion (id_tipoocupacion,descripcion) VALUES (3,'Estudiante');
INSERT INTO tipo_ocupacion (id_tipoocupacion,descripcion) VALUES (4,'Empleado');

-- TABLE: ocupacion
INSERT INTO ocupacion (id_ocupacion,descripcion,id_tipoocupacion) VALUES (4,'Ingeniero',2);
INSERT INTO ocupacion (id_ocupacion,descripcion,id_tipoocupacion) VALUES (5,'Traductor',2);
INSERT INTO ocupacion (id_ocupacion,descripcion,id_tipoocupacion) VALUES (6,'Abogado',2);
INSERT INTO ocupacion (id_ocupacion,descripcion,id_tipoocupacion) VALUES (7,'Estudiante',3);
INSERT INTO ocupacion (id_ocupacion,descripcion,id_tipoocupacion) VALUES (8,'Empleado',4);
INSERT INTO ocupacion (id_ocupacion,descripcion,id_tipoocupacion) VALUES (9,'Instructor',1);
INSERT INTO ocupacion (id_ocupacion,descripcion,id_tipoocupacion) VALUES (10,'Músico',1);
INSERT INTO ocupacion (id_ocupacion,descripcion,id_tipoocupacion) VALUES (11,'Sereno',1);

-- TABLE: persona
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (10,'Analia',4);
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (11,'Octaviano',7);
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (12,'Pablo',4);
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (13,'Hernan',4);
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (14,'Mavi',5);
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (15,'Ivan',6);
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (16,'Susana',8);
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (17,'Azul',7);
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (18,'Sol',7);
INSERT INTO persona (id_persona,nombre,id_ocupacion) VALUES (19,'Jose',11);

-- TABLE: direccion
INSERT INTO direccion (id_direccion,calle,numero) VALUES (7,'Cramer',2331);
INSERT INTO direccion (id_direccion,calle,numero) VALUES (8,'Remedios',5231);
INSERT INTO direccion (id_direccion,calle,numero) VALUES (9,'Baunes',654);
INSERT INTO direccion (id_direccion,calle,numero) VALUES (10,'Suipacha',634);
INSERT INTO direccion (id_direccion,calle,numero) VALUES (11,'Mozart',4230);
INSERT INTO direccion (id_direccion,calle,numero) VALUES (12,'Cabildo',7122);

-- TABLE: tipo_direccion
INSERT INTO tipo_direccion (id_tipo_direccion,descripcion) VALUES (1,'Particular');
INSERT INTO tipo_direccion (id_tipo_direccion,descripcion) VALUES (2,'Laboral');
INSERT INTO tipo_direccion (id_tipo_direccion,descripcion) VALUES (3,'Postal');

-- TABLE: persona_direccion
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (1,10,8,1);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (2,10,10,2);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (3,11,8,1);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (4,12,8,1);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (5,12,11,2);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (6,15,9,1);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (7,16,9,1);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (8,17,9,1);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (9,18,9,1);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (10,16,12,2);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (11,13,7,1);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (12,14,7,1);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (13,14,12,2);
INSERT INTO persona_direccion (id_persona_direccion,id_persona,id_direccion,id_tipo_direccion) VALUES (14,19,12,1);