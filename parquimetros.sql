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


CREATE TABLE ventas (
  id_tarjeta INT UNSIGNED NOT NULL,
  tipo_tarjeta VARCHAR(30) NOT NULL,
  saldo DECIMAL(5,2) NOT NULL,
  fecha DATE NOT NULL,
  hora TIME NOT NULL,

  CONSTRAINT pk_ventas
  PRIMARY KEY (id_tarjeta,fecha,hora),

  FOREIGN KEY (id_tarjeta) REFERENCES tarjetas(id_tarjeta)
  ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (tipo_tarjeta) REFERENCES tipos_tarjeta(tipo)
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
/* Stored Procedure */

delimiter !
CREATE PROCEDURE conectar (IN id_tarjeta INTEGER, IN id_parq INTEGER)
BEGIN

DECLARE saldo DECIMAL(5,2);
DECLARE tarifa DECIMAL(5, 2);
DECLARE descuento DECIMAL(3,2);
DECLARE minutos int;
DECLARE parq int;
DECLARE new_saldo int;
DECLARE cod_sql  CHAR(5) DEFAULT '00000';	 
DECLARE cod_mysql INT DEFAULT 0;
DECLARE msj_error TEXT;

DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
BEGIN #En caso de una excepción SQLEXCEPTION retrocede la transacción y devuelve el código de error especifico de MYSQL (MYSQL_ERRNO), el código de error SQL  (SQLSTATE) y el mensaje de error  
	GET DIAGNOSTICS CONDITION 1 cod_mysql= MYSQL_ERRNO, cod_sql= RETURNED_SQLSTATE, msj_error= MESSAGE_TEXT;
	SELECT 'SQLEXCEPTION!, Transaccion abortada' AS Resultado, cod_mysql, cod_sql,  msj_error;		
	ROLLBACK;
END;

Start TRANSACTION;
	/* Verificamos que exita el parquimetro o la tarjeta */
	IF EXISTS (SELECT p.id_parq FROM parquimetros as p WHERE id_parq=p.id_parq) THEN
		BEGIN
			IF EXISTS (SELECT t.id_tarjeta FROM tarjetas as t WHERE id_tarjeta=t.id_tarjeta) THEN
				BEGIN
					SELECT c.descuento INTO descuento FROM tarjetas t NATURAL JOIN tipos_tarjeta as c WHERE id_tarjeta=t.id_tarjeta LOCK IN SHARE MODE;	
					
          /* Si tiene estacionamiento abierto ---> Cierre */
					IF EXISTS (SELECT * FROM estacionamientos as e WHERE id_tarjeta = e.id_tarjeta AND e.fecha_sal is NULL AND e.hora_sal is NULL ORDER BY fecha_ent,hora_ent DESC LIMIT 1 FOR UPDATE) THEN
						BEGIN 

							SELECT t.saldo INTO saldo FROM tarjetas as t WHERE id_tarjeta=t.id_tarjeta FOR UPDATE;
							SELECT e.id_parq INTO parq FROM estacionamientos as e WHERE e.id_tarjeta = id_tarjeta AND e.fecha_sal IS NULL AND e.hora_sal IS NULL LOCK IN SHARE MODE;
              SELECT u.tarifa INTO tarifa FROM ubicaciones as u NATURAL JOIN parquimetros as p WHERE parq = p.id_parq;

              /* Minutos transcurridos durante el estacionamiento */
							SELECT (TIMESTAMPDIFF(MINUTE,CONCAT(e.fecha_ent,' ',e.hora_ent), now())) INTO minutos 
							FROM estacionamientos as e WHERE id_tarjeta=e.id_tarjeta AND parq=e.id_parq AND e.fecha_sal is NULL AND e.hora_sal is NULL;		
							
              SET new_saldo = (saldo-(minutos*tarifa*(1-descuento)));					
							
							UPDATE estacionamientos as e SET e.fecha_sal = CURDATE(), e.hora_sal = CURTIME() WHERE id_tarjeta=e.id_tarjeta AND parq=e.id_parq AND e.fecha_sal is NULL AND e.hora_sal is NULL; 
							
							/* Minimo valor de saldo permitido */
							IF (new_saldo < -999.99) THEN
								BEGIN
									UPDATE tarjetas as t SET t.saldo = "-999.99" WHERE t.id_tarjeta = id_tarjeta;
									SELECT 'Cierre' as Operacion, TRUNCATE (minutos,2) as 'Tiempo Transcurrido (min)', '-999.99' as 'Saldo Actualizado';
								END;
							ELSE
								BEGIN
									UPDATE tarjetas AS t SET t.saldo = new_saldo WHERE t.id_tarjeta=id_tarjeta;
									SELECT 'Cierre' AS Operacion, TRUNCATE (minutos,2) as 'Tiempo transcurrido (min)', new_saldo as 'Saldo Actualizado';
								END;
							END IF;
						END;
					ELSE /* Si no tiene estacionamiento abierto ---> Apertura (Si tiene saldo > 0)*/
						BEGIN

							SELECT t.saldo INTO saldo FROM tarjetas as t WHERE id_tarjeta = t.id_tarjeta;
							SELECT u.tarifa INTO tarifa FROM ubicaciones as u NATURAL JOIN parquimetros p WHERE id_parq = p.id_parq LOCK IN SHARE MODE;

							IF (saldo > 0) THEN
								BEGIN
									INSERT INTO estacionamientos (id_tarjeta,id_parq,fecha_ent,hora_ent,fecha_sal,hora_sal) VALUES (id_tarjeta, id_parq,CURDATE(),CURTIME(),NULL,NULL);
									SELECT 'Apertura' as Operacion, 'Exito' as Resultado, TRUNCATE ((saldo/(tarifa*(1-descuento))),2) AS 'Tiempo disponible (min)';	
								END;
							ELSE
								BEGIN
									SELECT 'Apertura' as Operacion, 'Error' as Resultado, 'Saldo insuficiente' as Motivo;
								END;
							END IF;
							
						END;
					END IF;	
				END;
			ELSE
				BEGIN
					SELECT 'Error' as Resultado, 'no existe el id_tarjeta' as Motivo;
				END;				
			END IF;
		END;
	ELSE
		BEGIN
			SELECT 'Error' as Resultado, 'no existe el id_parq' as Motivo;
		END;
	END IF;
COMMIT;
END;!

delimiter ;


# -----------------------------------------------------------------------------
# Creacion de trigger

delimiter !

CREATE TRIGGER triggerVentas 
AFTER INSERT ON tarjetas 
FOR EACH ROW

BEGIN

	INSERT INTO ventas VALUES (NEW.id_tarjeta,NEW.tipo,NEW.saldo,CURDATE(),CURTIME());

END; !

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

# Creacion de usuario parquimetro

CREATE USER 'parquimetro'@'%' IDENTIFIED BY 'parq';

GRANT EXECUTE ON PROCEDURE parquimetros.conectar TO parquimetro@'%';

GRANT SELECT, INSERT ON parquimetros.parquimetros TO 'parquimetro'@'%';

GRANT SELECT, INSERT ON parquimetros.estacionamientos TO 'parquimetro'@'%';

GRANT SELECT, INSERT ON parquimetros.tarjetas TO 'parquimetro'@'%';

GRANT SELECT ON parquimetros.tarjetas TO 'parquimetro'@'%';

