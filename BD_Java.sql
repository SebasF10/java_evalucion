drop database crediya_db;

CREATE DATABASE crediya_db;

USE crediya_db;


CREATE TABLE empleados (

  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80),
  documento VARCHAR(30) UNIQUE,
  rol VARCHAR(30),
  correo VARCHAR(80),
  salario DECIMAL(10,2)

);



CREATE TABLE clientes (

  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80),
  documento VARCHAR(30) UNIQUE,
  correo VARCHAR(80),
  telefono VARCHAR(20)

);



CREATE TABLE prestamos (

  id INT AUTO_INCREMENT PRIMARY KEY,
  cliente_id INT,
  empleado_id INT,
  monto DECIMAL(12,2),
  interes DECIMAL(5,2),
  cuotas INT,
  fecha_inicio DATE,
  estado VARCHAR(20),
  monto_total DECIMAL(10,2),
  valor_cuota DECIMAL(10,2),
  saldo_pendiente DECIMAL(10,2),
  fecha_vencimiento DATE,
  FOREIGN KEY (cliente_id) REFERENCES clientes(id),
  FOREIGN KEY (empleado_id) REFERENCES empleados(id)

);



CREATE TABLE pagos (

  id INT AUTO_INCREMENT PRIMARY KEY,
  prestamo_id INT,
  fecha_pago DATE,
  monto DECIMAL(10,2),
  FOREIGN KEY (prestamo_id) REFERENCES prestamos(id)

);
