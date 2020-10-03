# Autores: Almaraz Fabricio, Pacione Luciano


# HAY QUE CORREGIR LO DE FECHAS Y HORAS PORQUE NO SE COMO VA, EN EL PRÁCTICO AL FINAL DICE


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
  patente VARCHAR(6) NOT NULL,
  marca VARCHAR(30) NOT NULL,
  modelo VARCHAR(30) NOT NULL,
  color VARCHAR(30) NOT NULL,
  dni INT unsigned NOT NULL,

  CONSTRAINT pk_automoviles
  PRIMARY KEY (patente),

  FOREIGN KEY (dni) REFERENCES conductores(dni)
  ON DELETE RESTRICT ON UPDATE CASCADE,

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
  patente VARCHAR(6) NOT NULL,

  CONSTRAINT pk_tarjeta
  PRIMARY KEY (id_tarjeta),

  FOREIGN KEY (tipo) REFERENCES tipos_tarjeta(tipo)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (patente) REFERENCES automoviles(patente)
  ON DELETE RESTRICT ON UPDATE CASCADE,

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
  PRIMARY KEY (id_parq),

  FOREIGN KEY (calle,altura) REFERENCES ubicaciones(calle,altura)
  ON DELETE RESTRICT ON UPDATE CASCADE,

) ENGINE=InnoDB;


CREATE TABLE estacionamientos (
  id_tarjeta INT unsigned NOT NULL,
  id_parq INT unsigned NOT NULL,
  fecha_ent = DATE,
  hora_ent = TIME,
  fecha_sal = DATE,
  hora_sal = TIME,

  CONSTRAINT pk_estacionamientos
  PRIMARY KEY (id_parq,fecha_ent,hora_ent),

  FOREIGN KEY (id_parq) REFERENCES parquimetros(id_parq)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_tarjeta) REFERENCES tarjeta(id_tarjeta)
  ON DELETE RESTRICT ON UPDATE CASCADE,

) ENGINE=InnoDB;


CREATE TABLE accede (
  legajo  INT unsigned NOT NULL,
  id_parq INT unsigned NOT NULL,
  fecha = DATE NOT NULL,
  hora = TIME NOT NULL,

  CONSTRAINT pk_accede
  PRIMARY KEY (id_parq,fecha,hora),

  FOREIGN KEY (legajo) REFERENCES inspectores (legajo)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_parq) REFERENCES parquimetros (id_parq)
  ON DELETE RESTRICT ON UPDATE CASCADE,

) ENGINE=InnoDB;


CREATE TABLE asociado_con (
  id_asociado_con INT unsigned NOT NULL,
  legajo INT unsigned NOT NULL,
  calle VARCHAR(50) NOT NULL,
  altura INT unsigned NOT NULL,
  dia VARCHAR(20) NOT NULL,
  turno VARCHAR(1) NOT NULL,

  CONSTRAINT pk_asociado_con
  PRIMARY KEY (id_asociado_con),

  FOREIGN KEY (legajo) REFERENCES inspectores (legajo)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (calle,altura) REFERENCES ubicaciones (calle,altura)
  ON DELETE RESTRICT ON UPDATE CASCADE,

) ENGINE=InnoDB;


CREATE TABLE multa (
  numero INT unsigned NOT NULL,
  fecha = DATE NOT NULL,
  hora = TIME NOT NULL,
  patente VARCHAR(30) NOT NULL,
  id_asociado_con INT unsigned NOT NULL,

  CONSTRAINT pk_multa
  PRIMARY KEY (numero),

  FOREIGN KEY (patente) REFERENCES automoviles (patente)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_asociado_con) REFERENCES asociado_con (id_asociado_con)
  ON DELETE RESTRICT ON UPDATE CASCADE,

) ENGINE=InnoDB;

# -----------------------------------------------------------------------------
# Creacion de vistas

#BUSCAR Estacionamientos abiertos de una ubicacion

CREATE VIEW estacionados AS
SELECT DISTINCT ub.calle, ub.altura, au.patente
FROM ubicaciones AS ub JOIN parquimetros AS pq JOIN estacionamientos AS es JOIN tarjeta AS tj JOIN automoviles AS au ON
     ub.calle = pq.calle AND ub.altura = pq.altura AND pq.id_parq = es.id_parq AND es.id_tarjeta = tj.id_tarjeta AND tj.patente = au.patente

WHERE es.hora_ent != NULL AND es.fecha_ent != NULL AND es.hora_sal = NULL AND es.fecha_sal = NULL;


# -----------------------------------------------------------------------------
  # Funcion hash para nombre de usuario y password
  #insert into inspectores values('u1', md5('pw1'))



#------------------------------------------------------------------------------
  #Creacion de procedimiento validarLegajoPassword
  #create procedure validarLegajoPassword (IN legajo INT, IN password VARCHAR(32), OUT resultado VARCHAR(50))


# -----------------------------------------------------------------------------
# Creacio de usuario administrador

CREATE USER admin@localhost IDENTIFIED BY 'admin'

GRANT ALL PRIVILEGES ON parquimetros.* TO admin@localhost WITH GRANT OPTION;

# -----------------------------------------------------------------------------
# Creacion de usuario venta

CREATE USER venta@'%' IDENTIFIED BY 'venta'

#GRANT SELECT ON parquimetros.tipos_tarjeta TO venta@'%';

GRANT UPDATE ON parquimetros.tarjeta TO venta@'%';

# -----------------------------------------------------------------------------
# Creacion de usuario inspector

CREATE USER inspector@'%' IDENTIFIED BY 'inspector'

GRANT SELECT ON parquimetros.estacionados TO inspector@'%'

GRANT SELECT ON parquimetros.multa TO inspector@'%'

GRANT SELECT ON parquimetros.accede TO inspector@'%'

GRANT EXECUTE ON PROCEDURE parquimetros.validarLegajoPassword TO 'inspector'@'%'




}
