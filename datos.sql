# Autores: Almaraz Fabricio, Pacione Luciano
#
# Carga de datos de la BD parquimetros

USE parquimetros;

SET NAMES latin1;

DELETE FROM conductores;
DELETE FROM automoviles;
DELETE FROM tipos_tarjeta;
DELETE FROM tarjetas;
DELETE FROM inspectores;
DELETE FROM ubicaciones;
DELETE FROM parquimetros;
DELETE FROM estacionamientos;
DELETE FROM accede;
DELETE FROM asociado_con;
DELETE FROM multa;

DELIMITER !


CREATE FUNCTION dia(fecha DATE) RETURNS CHAR(2)
DETERMINISTIC
BEGIN
  DECLARE i INT;
  SELECT DAYOFWEEK(fecha) INTO i;
  CASE i
   WHEN 1 THEN RETURN 'Do';
   WHEN 2 THEN RETURN 'Lu';
   WHEN 3 THEN RETURN 'Ma';
   WHEN 4 THEN RETURN 'Mi';
   WHEN 5 THEN RETURN 'Ju';
   WHEN 6 THEN RETURN 'Vi';
   WHEN 7 THEN RETURN 'Sa';
 END CASE;
END; !

DELIMITER ;


# -----------------------------------------------------------------------------
# CONDUCTORES

INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (101,'Ronaldo','Nazario','Alem 1234','123456789',1001);
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (102,'Lionel','Messi','Don Bosco 789','148656789',1002);
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (103,'Cristiano','Ronaldo','Salta 95','951256789',1003);
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (104,'Sergio','Aguero','Sgo del estero 985','175316789',1004);
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (105,'Carlos','Tevez','Trelew 345','123459632',1005);
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (106,'Nestor','Ortigoza','12 de Octubre 134','123587489',1006);

# -----------------------------------------------------------------------------
# AUTOMOVILES

INSERT INTO automoviles(patente,marca,modelo,color,dni)
VALUES('abc123','Fiat','600','amarillo',101);
INSERT INTO automoviles(patente,marca,modelo,color,dni)
VALUES('brw793','Ford','Focus','gris',102);
INSERT INTO automoviles(patente,marca,modelo,color,dni)
VALUES('jod800','Alfa Romeo','Mito','rojo',103);
INSERT INTO automoviles(patente,marca,modelo,color,dni)
VALUES('sie098','Lamborghini','Aventador','verde',104);
INSERT INTO automoviles(patente,marca,modelo,color,dni)
VALUES('jhg890','Pagani','Zonda','azul',105);
INSERT INTO automoviles(patente,marca,modelo,color,dni)
VALUES('rft543','Renault','12','violeta',106);

# -----------------------------------------------------------------------------
# TIPOS DE TARJETAS

INSERT INTO tipos_tarjeta(tipo,descuento)
VALUES('t1',0.10);
INSERT INTO tipos_tarjeta(tipo,descuento)
VALUES('t2',0.20);
INSERT INTO tipos_tarjeta(tipo,descuento)
VALUES('t3',0.30);
INSERT INTO tipos_tarjeta(tipo,descuento)
VALUES('t4',0.40);

# -----------------------------------------------------------------------------
# TARJETAS

INSERT INTO tarjetas(id_tarjeta,saldo,tipo,patente)
VALUES(9876,115.25,'t1','abc123');
INSERT INTO tarjetas(id_tarjeta,saldo,tipo,patente)
VALUES(1234,130.85,'t1','brw793');
INSERT INTO tarjetas(id_tarjeta,saldo,tipo,patente)
VALUES(4567,1.50,'t2','jod800');
INSERT INTO tarjetas(id_tarjeta,saldo,tipo,patente)
VALUES(8745,78.05,'t3','sie098');
INSERT INTO tarjetas(id_tarjeta,saldo,tipo,patente)
VALUES(2354,206.35,'t4','jhg890');
INSERT INTO tarjetas(id_tarjeta,saldo,tipo,patente)
VALUES(9658,989.99,'t3','rft543');

# -----------------------------------------------------------------------------
# INSPECTORES

INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(101,107,'Nombre_insp101','Apellido_insp101',md5('insp101'));
INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(102,108,'Nombre_insp102','Apellido_insp102',md5('insp102'));
INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(103,109,'Nombre_insp103','Apellido_insp103',md5('insp103'));
INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(104,110,'Nombre_insp104','Apellido_insp104',md5('insp104'));
INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(105,111,'Nombre_insp105','Apellido_insp105',md5('insp105'));
INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(106,112,'Nombre_insp106','Apellido_insp106',md5('insp106'));

# -----------------------------------------------------------------------------
# UBICACIONES

INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle1',789,10.35);
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle2',89,3.75);
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle3',149,2.78);
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle4',1009,1.50);
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle5',298,5.05);
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle6',875,7.00);

# -----------------------------------------------------------------------------
# PARQUIMETROS

INSERT INTO parquimetros(id_parq,numero,calle,altura)
VALUES(111,1,'calle1',789);
INSERT INTO parquimetros(id_parq,numero,calle,altura)
VALUES(222,2,'calle2',89);
INSERT INTO parquimetros(id_parq,numero,calle,altura)
VALUES(333,3,'calle3',149);
INSERT INTO parquimetros(id_parq,numero,calle,altura)
VALUES(444,4,'calle4',1009);
INSERT INTO parquimetros(id_parq,numero,calle,altura)
VALUES(555,5,'calle5',298);
INSERT INTO parquimetros(id_parq,numero,calle,altura)
VALUES(666,6,'calle6',875);

# -----------------------------------------------------------------------------
# ESTACIONAMIENTOS

INSERT INTO estacionamientos(fecha_ent,hora_ent,fecha_sal,hora_sal,id_tarjeta,id_parq)
VALUES('02-09-20','17:00:00',NULL,NULL,9876,111);
INSERT INTO estacionamientos(fecha_ent,hora_ent,fecha_sal,hora_sal,id_tarjeta,id_parq)
VALUES('03-12-12','12:00:00',NULL,NULL,1234,222);
INSERT INTO estacionamientos(fecha_ent,hora_ent,fecha_sal,hora_sal,id_tarjeta,id_parq)
VALUES('06-10-10','09:00:00',NULL,NULL,4567,333);
INSERT INTO estacionamientos(fecha_ent,hora_ent,fecha_sal,hora_sal,id_tarjeta,id_parq)
VALUES('02-08-20','18:00:00',NULL,NULL,8745,444);
INSERT INTO estacionamientos(fecha_ent,hora_ent,fecha_sal,hora_sal,id_tarjeta,id_parq)
VALUES('03-04-20','22:00:00',NULL,NULL,2354,555);
INSERT INTO estacionamientos(fecha_ent,hora_ent,fecha_sal,hora_sal,id_tarjeta,id_parq)
VALUES('02-08-18','10:00:00',NULL,NULL,9658,666);

# -----------------------------------------------------------------------------
# ACCEDE

INSERT INTO accede(legajo,id_parq,fecha,hora)
VALUES(101,111,'02-08-20','19:00:00');
INSERT INTO accede(legajo,id_parq,fecha,hora)
VALUES(102,222,'01-07-15','20:00:00');
INSERT INTO accede(legajo,id_parq,fecha,hora)
VALUES(103,333,'02-03-16','22:00:00');
INSERT INTO accede(legajo,id_parq,fecha,hora)
VALUES(104,444,'09-04-17','07:00:00');
INSERT INTO accede(legajo,id_parq,fecha,hora)
VALUES(105,555,'23-02-18','09:00:00');
INSERT INTO accede(legajo,id_parq,fecha,hora)
VALUES(106,666,'21-05-20','11:00:00');

# -----------------------------------------------------------------------------
# ASOCIADO_CON

INSERT INTO asociado_con(legajo,id_asociado_con,dia,turno,calle,altura)
VALUES(101,1001,dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-01-01')),'M','calle1',789);
INSERT INTO asociado_con(legajo,id_asociado_con,dia,turno,calle,altura)
VALUES(102,1002,dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-07-04')),'M','calle2',89);
INSERT INTO asociado_con(legajo,id_asociado_con,dia,turno,calle,altura)
VALUES(103,1003,dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-05-05')),'M','calle3',149);
INSERT INTO asociado_con(legajo,id_asociado_con,dia,turno,calle,altura)
VALUES(104,1004,dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-04-06')),'T','calle4',1009);
INSERT INTO asociado_con(legajo,id_asociado_con,dia,turno,calle,altura)
VALUES(105,1005,dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-03-07')),'T','calle5',298);
INSERT INTO asociado_con(legajo,id_asociado_con,dia,turno,calle,altura)
VALUES(106,1006,dia(CONCAT(YEAR(DATE_ADD(NOW(), INTERVAL 1 YEAR)),'-02-08')),'T','calle6',875);

# -----------------------------------------------------------------------------
# MULTA

INSERT INTO multa(numero,fecha,hora,patente,id_asociado_con)
VALUES(1,'02-08-20','19:00:00','abc123',1001);
INSERT INTO multa(numero,fecha,hora,patente,id_asociado_con)
VALUES(2,'03-09-10','11:00:00','brw793',1002);
INSERT INTO multa(numero,fecha,hora,patente,id_asociado_con)
VALUES(3,'22-08-12','20:00:00','jod800',1003);
INSERT INTO multa(numero,fecha,hora,patente,id_asociado_con)
VALUES(4,'23-08-17','22:00:00','sie098',1004);

#-----------------------------------------------------------------------------

DROP FUNCTION dia;
