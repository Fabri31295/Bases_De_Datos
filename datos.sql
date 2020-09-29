# Autores: Almaraz Fabricio, Pacione Luciano

# Carga de datos para la base de datos parquimetros

USE parquimetros;

SET NAMES latin1;


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
VALUES (61891198,'Ronaldo','Nazario','Alem','1234','123456789',419053)
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (52217535,'Lionel','Messi','Don Bosco','789','148656789',491881);
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (74185268,'Cristiano','Ronaldo','Salta','95','951256789',643722);
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (13245879,'Sergio','Aguero','Sgo del estero','985','175316789',388754);
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (21528879,'Carlos','Tevez','Trelew','345','123459632',804159);
INSERT INTO conductores(dni,nombre,apellido,direccion,telefono,registro)
VALUES (31544998,'Nestor','Ortigoza','12 de Octubre','134','123587489',697870);

# -----------------------------------------------------------------------------
# AUTOMOVILES

INSERT INTO automoviles(patente,marca,modelo,color)
VALUES('abc123','Fiat','600','amarillo');
INSERT INTO automoviles(patente,marca,modelo,color)
VALUES('brw793','Ford','Focus','gris');
INSERT INTO automoviles(patente,marca,modelo,color)
VALUES('jod800','Alfa Romeo','Mito','rojo');
INSERT INTO automoviles(patente,marca,modelo,color)
VALUES('sie098','Lamborghini','Aventador','verde');
INSERT INTO automoviles(patente,marca,modelo,color)
VALUES('jhg890','Pagani','Zonda','azul');
INSERT INTO automoviles(patente,marca,modelo,color)
VALUES('rft543','Renault','12','violeta');

# -----------------------------------------------------------------------------
# TIPOS DE TARJETAS

INSERT INTO tipos_tarjeta(tipo,descuento)
VALUES('tipo1',0.10);
INSERT INTO tipos_tarjeta(tipo,descuento)
VALUES('tipo2',0.20);
INSERT INTO tipos_tarjeta(tipo,descuento)
VALUES('tipo3',0.30);
INSERT INTO tipos_tarjeta(tipo,descuento)
VALUES('tipo4',0.40);

# -----------------------------------------------------------------------------
# TARJETA

INSERT INTO tarjeta(id_tarjeta,saldo)
VALUES(9876,115.25);
INSERT INTO tarjeta(id_tarjeta,saldo)
VALUES(1234,130.85);
INSERT INTO tarjeta(id_tarjeta,saldo)
VALUES(4567,1.50);
INSERT INTO tarjeta(id_tarjeta,saldo)
VALUES(8745,78.05);
INSERT INTO tarjeta(id_tarjeta,saldo)
VALUES(2354,206.35);
INSERT INTO tarjeta(id_tarjeta,saldo)
VALUES(9658,989.99);
INSERT INTO tarjeta(id_tarjeta,saldo)
VALUES(1576,0.00);

# -----------------------------------------------------------------------------
# INSPECTORES

INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(101,101,'Nombre_insp101','Apellido_insp101',md5('insp101'));
INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(102,102,'Nombre_insp102','Apellido_insp102',md5('insp101'));
INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(103,103,'Nombre_insp103','Apellido_insp103',md5('insp101'));
INSERT INTO inspectores(legajo,dni,nombre,apellido,password)
VALUES(104,104,'Nombre_insp104','Apellido_insp104',md5('insp101'));

# -----------------------------------------------------------------------------
# UBICACIONES

INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle1',789,10.35),
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle2',89,3.75),
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle3',149,2.78),
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle4',1009,1.50),
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle5',298,5.05),
INSERT INTO ubicaciones(calle,altura,tarifa)
VALUES('calle6',875,7.00),

# -----------------------------------------------------------------------------
# PARQUIMETROS

INSERT INTO parquimetros(id_parq,numero)
VALUES(11,1),
INSERT INTO parquimetros(id_parq,numero)
VALUES(12,2),
INSERT INTO parquimetros(id_parq,numero)
VALUES(13,3),
INSERT INTO parquimetros(id_parq,numero)
VALUES(14,4),
INSERT INTO parquimetros(id_parq,numero)
VALUES(15,5),
INSERT INTO parquimetros(id_parq,numero)
VALUES(16,6),
INSERT INTO parquimetros(id_parq,numero)
VALUES(17,7),

# -----------------------------------------------------------------------------
# ESTACIONAMIENTOS

INSERT INTO estacionamientos(fecha_ent,hora_ent,fecha_sal,hora_sal)
VALUES()


# Falta ACCEDE, ASOCIADO_CON y MULTA, hay que ver como se pone la fecha y hora
