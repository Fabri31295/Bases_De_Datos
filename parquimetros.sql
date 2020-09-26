# Autores: Almaraz Fabricio, Pacione Luciano



# Creacion de base de datos.
CREATE database parquimetros;

# Base de datos sobre la que se va a trabajar.
USE parquimetros;

# Creacion de las tablas.

CREATE TABLE conductores (
  dni INT unsigned NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  direccion VARCHAR(50) NOT NULL,
  telefono VARCHAR(30) NOT NULL,
  registro VARCHAR(20) NOT NULL,

  CONSTRAINT pk_conductores
  PRIMARY KEY (dni)

) ENGINE=InnoDB;


CREATE TABLE automoviles (
  patente VARCHAR(30) NOT NULL,
  marca VARCHAR(30) NOT NULL,
  modelo VARCHAR(30) NOT NULL,
  color VARCHAR(30) NOT NULL,
  dni INT unsigned NOT NULL,

  CONSTRAINT pk_automoviles
  PRIMARY KEY (patente)
  FOREIGN KEY (dni) REFERENCES conductores(dni)

) ENGINE=InnoDB;


CREATE TABLE tipos_tarjeta
  tipo VARCHAR(30) NOT NULL,
  descuento FLOAT(4,2) NOT NULL,

  CONSTRAINT pk_tipo_ta
  PRIMARY KEY (tipo)

) ENGINE=InnoDB;


CREATE TABLE tarjeta (
  id_tarjeta INT unsigned NOT NULL,
  saldo FLOAT(5,2) NOT NULL
  tipo VARCHAR(30) NOT NULL,
  patente VARCHAR(30) NOT NULL,

  CONSTRAINT pk_tarjeta
  PRIMARY KEY (id_tarjeta)
  FOREIGN KEY (tipo,patente) REFERENCES tipos_tarjeta(tipo), automoviles(patente)

) ENGINE=InnoDB;


CREATE TABLE inspectores (
  legajo INT unsigned NOT NULL,
  dni INT unsigned NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  password VARCHAR(32) NOT NULL,

  CONSTRAINT pk_inspectores
  PRIMARY KEY (legajo)

) ENGINE=InnoDB;


CREATE TABLE ubicaciones (
  calle VARCHAR(50) NOT NULL,
  altura INT unsigned NOT NULL,
  tarifa float(5,2),

  CONSTRAINT pk_ubicaciones
  PRIMARY KEY (calle,altura)

) ENGINE=InnoDB;


CREATE TABLE parquimetros (
  id_parq INT unsigned NOT NULL,
  numero INT unsigned NOT NULL,
  calle VARCHAR(50) NOT NULL,
  altura INT unsigned NOT NULL,

  CONSTRAINT pk_parquimetros
  PRIMARY KEY (id_parq)
  FOREIGN KEY (calle,altura) REFERENCES ubicaciones(calle,altura)

) ENGINE=InnoDB;


CREATE TABLE estacionamientos (
  id_tarjeta INT unsigned NOT NULL,
  id_parq INT unsigned NOT NULL,
  #fecha_ent
  #hora_ent
  #fecha_sal
  #hora_sal

  CONSTRAINT pk_estacionamientos
  PRIMARY KEY (id_parq,fecha_ent,hora_ent)
  FOREIGN KEY (id_parq) REFERENCES parquimetros(id_parq)
  FOREIGN KEY (id_tarjeta) REFERENCES tarjeta(id_tarjeta)

) ENGINE=InnoDB;


CREATE TABLE accede (
  legajo  INT unsigned NOT NULL,
  id_parq INT unsigned NOT NULL,
  #fecha
  #hora

  CONSTRAINT pk_accede
  PRIMARY KEY (id_parq,fecha,hora)
  FOREIGN KEY (legajo) REFERENCES inspectores (legajo)
  FOREIGN KEY (id_parq) REFERENCES parquimetros (id_parq)

) ENGINE=InnoDB;


CREATE TABLE asociado_con (

) ENGINE=InnoDB;


CREATE TABLE multa (

) ENGINE=InnoDB;








}
