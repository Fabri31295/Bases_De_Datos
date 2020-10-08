# Autores: Almaraz Fabricio, Pacione Luciano

# Creacion de base de datos.
CREATE DATABASE parquimetros;

# Base de datos sobre la que se va a trabajar.
USE parquimetros;

# Creacion de las tablas.

CREATE TABLE conductores (
  dni INT UNSIGNED NOT NULL,
  registro INT UNSIGNED NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  direccion VARCHAR(50) NOT NULL,
  telefono VARCHAR(30),
  

  CONSTRAINT pk_conductores
  PRIMARY KEY (dni)

) ENGINE=InnoDB;


CREATE TABLE automoviles (
  patente VARCHAR(6) NOT NULL,
  marca VARCHAR(30) NOT NULL,
  modelo VARCHAR(30) NOT NULL,
  color VARCHAR(30) NOT NULL,
  dni INT UNSIGNED NOT NULL,

  CONSTRAINT pk_automoviles
  PRIMARY KEY (patente),

  FOREIGN KEY (dni) REFERENCES conductores(dni)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE tipos_tarjeta (
  tipo VARCHAR(30) NOT NULL,
  descuento DECIMAL(3,2) UNSIGNED NOT NULL,

  CONSTRAINT pk_tipo_tarjeta
  PRIMARY KEY (tipo)

) ENGINE=InnoDB;


CREATE TABLE tarjetas (
  id_tarjeta INT UNSIGNED NOT NULL AUTO_INCREMENT,
  saldo DECIMAL(5,2) NOT NULL,
  tipo VARCHAR(30) NOT NULL,
  patente VARCHAR(6) NOT NULL,

  CONSTRAINT pk_tarjeta
  PRIMARY KEY (id_tarjeta),

  FOREIGN KEY (tipo) REFERENCES tipos_tarjeta(tipo)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (patente) REFERENCES automoviles(patente)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE inspectores (
  legajo INT UNSIGNED NOT NULL,
  dni INT UNSIGNED NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  password VARCHAR(32) NOT NULL,

  CONSTRAINT pk_inspectores
  PRIMARY KEY (legajo)

) ENGINE=InnoDB;


CREATE TABLE ubicaciones (
  calle VARCHAR(50) NOT NULL,
  altura INT UNSIGNED NOT NULL,
  tarifa DECIMAL(5,2) UNSIGNED NOT NULL,

  CONSTRAINT pk_ubicaciones
  PRIMARY KEY (calle,altura)

) ENGINE=InnoDB;


CREATE TABLE parquimetros (
  id_parq INT UNSIGNED NOT NULL,
  numero INT UNSIGNED NOT NULL,
  calle VARCHAR(50) NOT NULL,
  altura INT UNSIGNED NOT NULL,

  CONSTRAINT pk_parquimetros
  PRIMARY KEY (id_parq),

  FOREIGN KEY (calle,altura) REFERENCES ubicaciones(calle,altura)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE estacionamientos (
  id_tarjeta INT UNSIGNED NOT NULL,
  id_parq INT UNSIGNED NOT NULL,
  fecha_ent DATE NOT NULL,
  hora_ent TIME NOT NULL,
  fecha_sal DATE,
  hora_sal TIME,

  CONSTRAINT pk_estacionamientos
  PRIMARY KEY (id_parq,fecha_ent,hora_ent),

  FOREIGN KEY (id_parq) REFERENCES parquimetros(id_parq)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_tarjeta) REFERENCES tarjetas(id_tarjeta)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE accede (
  legajo  INT UNSIGNED NOT NULL,
  id_parq INT UNSIGNED NOT NULL,
  fecha DATE NOT NULL,
  hora TIME NOT NULL,

  CONSTRAINT pk_accede
  PRIMARY KEY (id_parq,fecha,hora),

  FOREIGN KEY (legajo) REFERENCES inspectores (legajo)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_parq) REFERENCES parquimetros (id_parq)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE asociado_con (
  id_asociado_con INT UNSIGNED NOT NULL AUTO_INCREMENT,
  legajo INT UNSIGNED NOT NULL,
  calle VARCHAR(50) NOT NULL,
  altura INT UNSIGNED NOT NULL,
  dia ENUM('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
  turno ENUM('M','T') NOT NULL,

  CONSTRAINT pk_asociado_con
  PRIMARY KEY (id_asociado_con),

  FOREIGN KEY (legajo) REFERENCES inspectores (legajo)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (calle,altura) REFERENCES ubicaciones (calle,altura)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE multa (
  numero INT UNSIGNED NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  hora TIME NOT NULL,
  patente CHAR(6) NOT NULL,
  id_asociado_con INT UNSIGNED NOT NULL,

  CONSTRAINT pk_multa
  PRIMARY KEY (numero),

  FOREIGN KEY (patente) REFERENCES automoviles (patente)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_asociado_con) REFERENCES asociado_con (id_asociado_con)
  ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;

# -----------------------------------------------------------------------------
# Creacion de vistas

#BUSCAR Estacionamientos abiertos de una ubicacion

CREATE VIEW estacionados AS
SELECT DISTINCT ub.calle, ub.altura, au.patente
FROM ubicaciones AS ub JOIN parquimetros AS pq JOIN estacionamientos AS es JOIN tarjetas AS tj JOIN automoviles AS au ON
     ub.calle = pq.calle AND ub.altura = pq.altura AND pq.id_parq = es.id_parq AND es.id_tarjeta = tj.id_tarjeta AND tj.patente = au.patente

WHERE es.hora_ent is not NULL AND es.fecha_ent is not NULL AND es.hora_sal is NULL AND es.fecha_sal is NULL;


# -----------------------------------------------------------------------------
  # Funcion hash para nombre de usuario y password
  #insert into inspectores values('u1', md5('pw1'))

# -----------------------------------------------------------------------------
# Creacion de usuario administrador

CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';

GRANT ALL PRIVILEGES ON parquimetros.* TO 'admin'@'localhost' WITH GRANT OPTION;

# -----------------------------------------------------------------------------
# Creacion de usuario venta

CREATE USER 'venta'@'%' IDENTIFIED BY 'venta';

GRANT SELECT ON parquimetros.tipos_tarjeta TO 'venta'@'%';

GRANT SELECT, INSERT ON parquimetros.tarjetas TO 'venta'@'%';

GRANT SELECT ON parquimetros.automoviles TO 'venta'@'%';

# -----------------------------------------------------------------------------
# Creacion de usuario inspector

CREATE USER 'inspector'@'%' IDENTIFIED BY 'inspector';

GRANT SELECT ON parquimetros.inspectores TO 'inspector'@'%';

GRANT SELECT, INSERT ON parquimetros.multa TO 'inspector'@'%';

GRANT SELECT ON parquimetros.estacionados TO 'inspector'@'%';

GRANT SELECT, INSERT ON parquimetros.accede TO 'inspector'@'%';



