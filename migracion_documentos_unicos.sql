USE crediya_db;

ALTER TABLE clientes
  ADD CONSTRAINT uq_clientes_documento UNIQUE (documento);

ALTER TABLE empleados
  ADD CONSTRAINT uq_empleados_documento UNIQUE (documento);
