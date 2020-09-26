# Autores: Almaraz Fabricio, Pacione Luciano



# Creacion de base de datos.
CREATE database parquimetros;

# Base de datos sobre la que se va a trabajar.
USE parquimetros;

# Creacion de las tablas.

CREATE TABLE conductores (
  nombre VARCHAR(50) NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  direccion VARCHAR(50) NOT NULL,
  telefono VARCHAR(30) NOT NULL,
  dni VARCHAR(20) NOT NULL,
  registro VARCHAR(20) NOT NULL,

  CONSTRAINT pk_conductores
  PRIMARY KEY (dni)

) ENGINE=InnoDB;
