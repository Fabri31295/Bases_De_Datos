# Bases de Datos, Segundo cuatrimestre 2020
# Universidad Nacional del Sur
#
# Autores: Almaraz Fabricio, Pacione Luciano
#
# Script para la creacion de la base de datos, usuarios y vistas.

# -----------------------------------------------------------------------------
# Creacion de base de datos.
CREATE DATABASE parquimetros;

# Base de datos sobre la que se va a trabajar.
USE parquimetros;

# -----------------------------------------------------------------------------
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
# Creacion de vista estacionados

#BUSCAR Estacionamientos abiertos de una ubicacion

CREATE VIEW estacionados AS
SELECT DISTINCT ub.calle, ub.altura, au.patente
FROM ubicaciones AS ub JOIN parquimetros AS pq JOIN estacionamientos AS es JOIN tarjetas AS tj JOIN automoviles AS au ON
     ub.calle = pq.calle AND ub.altura = pq.altura AND pq.id_parq = es.id_parq AND es.id_tarjeta = tj.id_tarjeta AND tj.patente = au.patente

WHERE es.hora_ent is not NULL AND es.fecha_ent is not NULL AND es.hora_sal is NULL AND es.fecha_sal is NULL;

# -----------------------------------------------------------------------------
# Creacion de Stored Procedures

delimiter !

#  Apertura y cierre de estacionamiento
create procedure conectar(IN id_tarjeta INT, IN id_parq INT)
begin

  # variables
  DECLARE fechaIN DATE;
  DECLARE horaIN TIME;
  DECLARE fechaOUT DATE;
  DECLARE horaOUT TIME;
  DECLARE fechaENT DATETIME;
  DECLARE fechaSAL DATETIME;
  DECLARE tarifaX INT;
  DECLARE descuentoX INT;
  DECLARE saldoX INT;
  DECLARE minutosDisp INT;
  DECLARE tiempo INT;
  DECLARE tipo CHAR(10);
  DECLARE exito CHAR(1);  #------------> Y or N

  # recupero datos de estacionamiento
  SELECT fecha_ent INTO fechaIN FROM estacionados AS es WHERE es.id_tarjeta = id_tarjeta AND es.id_parq = id_parq;
  SELECT hora_ent INTO horaIN FROM estacionados AS es WHERE es.id_tarjeta = id_tarjeta AND es.id_parq = id_parq;
  SELECT fechaENT = CAST(fechaIN AS DATETIME) + CAST(horaIN AS DATETIME);

  SELECT fecha_sal INTO fechaOUT FROM estacionados AS es WHERE es.id_tarjeta = id_tarjeta AND es.id_parq = id_parq;
  SELECT hora_sal INTO horaOUT FROM estacionados AS es WHERE es.id_tarjeta = id_tarjeta AND es.id_parq = id_parq;
  SELECT fechaSAL = CAST(fechaOUT AS DATETIME) + CAST(horaOUT AS DATETIME);

  SELECT saldo INTO saldoX FROM tarjetas AS t WHERE t.id_tarjeta = id_tarjeta;
  SELECT tarifa INTO tarifaX FROM ubicaciones AS u NATURAL JOIN parquimetros AS p WHERE p.id_parq = id_parq;
  SELECT descuento INTO descuentoX FROM tipos_tarjeta NATURAL JOIN tarjetas AS t WHERE t.id_tarjeta = id_tarjeta;
/*
  if(fechaOUT == NULL AND horaOUT == NULL) THEN
    SET tipo = "Apertura";
    if(saldoX > 0) THEN
      SET exito = "Y";
    else
      SET exito = "N";
    SET
    end if;
    SET minutosDisp = saldo/(tarifaX*(1-descuentoX));

  else
    SET tipo = "Cierre";
  end if;*/

end;!
delimiter ;


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

GRANT SELECT ON parquimetros.asociado_con TO 'inspector'@'%';

GRANT SELECT ON parquimetros.parquimetros TO 'inspector'@'%';

GRANT SELECT ON parquimetros.automoviles TO 'inspector'@'%';

# -----------------------------------------------------------------------------
