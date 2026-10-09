/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Controlador;

import com.mycompany.proyecto1_java.Modelo.Clases.Pago;
import com.mycompany.proyecto1_java.Modelo.Clases.Prestamo;
import com.mycompany.proyecto1_java.Modelo.Persistencia.ConexionBD;
import com.mycompany.proyecto1_java.Modelo.Persistencia.Operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Sebas
 */
public class GestorPagos {

    private static final ArrayList<Pago> pagos = new ArrayList<>();

    public static void registrarPago(Pago pago) {
        try {
            validarPago(pago);
            pagos.add(pago);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error al registrar pago: " + ex.getMessage());
        }
    }

    public static void guardarPagoEnDB(Pago pago) throws SQLException {
        validarPago(pago);

        Connection connection = Operaciones.getConnection();
        if (connection == null) {
            throw new SQLException("No hay una conexion activa con la base de datos.");
        }

        String sql = "INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, pago.getPrestamo().getId());
            statement.setDate(2, java.sql.Date.valueOf(pago.getFechaPago()));
            statement.setDouble(3, pago.getMonto());

            if (statement.executeUpdate() != 1) {
                throw new SQLException("No se pudo guardar el pago en la base de datos.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("No se pudo obtener el ID del pago registrado.");
                }
                pago.setId(generatedKeys.getInt(1));
            }
        }
    }

    private static void validarPago(Pago pago) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago no puede ser nulo.");
        }
        if (pago.getPrestamo() == null || pago.getPrestamo().getId() <= 0) {
            throw new IllegalArgumentException("El pago debe tener un prestamo valido.");
        }
        if (pago.getFechaPago() == null) {
            throw new IllegalArgumentException("La fecha del pago no puede ser nula.");
        }
        if (!Double.isFinite(pago.getMonto()) || pago.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor que cero.");
        }
    }

    public static void listarPagos() {
        String sql = "SELECT pa.id, pa.prestamo_id, pa.fecha_pago, pa.monto, "
                + "c.nombre AS nombre_cliente "
                + "FROM pagos pa "
                + "LEFT JOIN prestamos p ON pa.prestamo_id = p.id "
                + "LEFT JOIN clientes c ON p.cliente_id = c.id "
                + "ORDER BY pa.id";
        Map<Integer, String> nombresClientes = new HashMap<>();
        try (Connection connection = ConexionBD.MysConnection()) {
            if (connection == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {
                pagos.clear();
                while (rs.next()) {
                    java.sql.Date fecha = rs.getDate("fecha_pago");
                    int pagoId = rs.getInt("id");
                    Prestamo prestamo = new Prestamo(
                            rs.getInt("prestamo_id"), null, null, 0, 0, 0,
                            null, null, 0, 0, 0, null);
                    pagos.add(new Pago(
                            pagoId,
                            prestamo,
                            fecha == null ? null : fecha.toLocalDate(),
                            rs.getDouble("monto")));
                    nombresClientes.put(pagoId, rs.getString("nombre_cliente"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pagos: " + ex.getMessage());
            return;
        }

        System.out.println("------ PAGOS -------");
        if (pagos.isEmpty()) {
            System.out.println("No hay pagos registrados.");
            return;
        }
        pagos.forEach(pago -> System.out.println(
                "ID pago: " + pago.getId()
                + " - ID prestamo: " + pago.getPrestamo().getId()
                + " - Cliente: " + nombresClientes.get(pago.getId())
                + " - Fecha: " + pago.getFechaPago()
                + " - Monto: $" + pago.getMonto()));
    }

    public static void mostrarPagosMayoresA(double valor) {
        System.out.println("=== PAGOS MAYORES A " + valor + " ===");
        pagos.stream()
                .filter(pago -> pago.getMonto() > valor)
                .forEach(pago -> System.out.println(
                        "Pago ID " + pago.getId() + " - Monto: " + pago.getMonto()));
    }
    
    // REGISTRAR PAGO
    public static void registrarPago(Scanner sc) {

        System.out.println("------- REGISTRAR PAGO -------- ");

        System.out.print("ID del prestamo: ");
        if (!sc.hasNextInt()) {
            System.out.println("El ID debe ser un numero.");
            sc.next();
            return;
        }
        int prestamoId = sc.nextInt();
        if (prestamoId <= 0) {
            System.out.println("El ID del prestamo debe ser positivo.");
            return;
        }

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());
            PrestamoControlador.sincronizarPrestamosVencidos();

            // Consultar los datos del prestamo antes de solicitar el pago
            String sqlConsulta =
                    "SELECT monto, monto_total, cuotas, valor_cuota, "
                    + "saldo_pendiente, fecha_vencimiento, estado "
                    + "FROM prestamos WHERE id = ?";

            PreparedStatement psConsulta =
                    Operaciones.getConnection()
                            .prepareStatement(sqlConsulta);

            psConsulta.setInt(1, prestamoId);

            ResultSet rs =
                    Operaciones.consultar_BD(psConsulta);

            if (!rs.next()) {
                System.out.println("El prestamo no existe.");
                return;
            }

            double montoOriginal = rs.getDouble("monto");
            double montoTotal = rs.getDouble("monto_total");
            int cuotas = rs.getInt("cuotas");
            double valorCuota = rs.getDouble("valor_cuota");
            double saldoActual =
                    rs.getDouble("saldo_pendiente");
            if (saldoActual <= 0 || "PAGADO".equalsIgnoreCase(rs.getString("estado"))) {
                System.out.println("El prestamo ya esta pagado.");
                return;
            }

            java.sql.Date fechaVencimiento = rs.getDate("fecha_vencimiento");
            System.out.println("------ DATOS DEL PRESTAMO ------");
            System.out.println("Monto prestado: $" + montoOriginal);
            System.out.println("Monto total con intereses: $" + montoTotal);
            System.out.println("Cuotas acordadas: " + cuotas);
            System.out.println("Valor de cada cuota: $" + valorCuota);
            System.out.println("Saldo pendiente: $" + saldoActual);
            System.out.println("Fecha limite de pago: "
                    + (fechaVencimiento == null ? "No registrada" : fechaVencimiento));
            if (fechaVencimiento != null
                    && fechaVencimiento.toLocalDate().isBefore(java.time.LocalDate.now())) {
                System.out.println("AVISO: El prestamo esta vencido.");
            }

            System.out.print("Monto del pago: ");
            if (!sc.hasNextDouble()) {
                System.out.println("El monto debe ser un numero.");
                sc.next();
                return;
            }
            double montoPago = sc.nextDouble();
            if (!Double.isFinite(montoPago) || montoPago <= 0) {
                System.out.println("El monto del pago debe ser mayor que 0.");
                return;
            }

            // Verificar que el pago no sea mayor al saldo
            if (montoPago - saldoActual > 0.000001) {
                System.out.println("El pago no puede ser mayor al saldo pendiente.");
                return;
            }

            java.time.LocalDate fechaPago = LocalDate.now();
            Pago pago = new Pago(
                    0,
                    new Prestamo(prestamoId, null, null, montoOriginal, 0, cuotas,
                            null, rs.getString("estado"), montoTotal, valorCuota,
                            saldoActual, fechaVencimiento == null
                                    ? null : fechaVencimiento.toLocalDate()),
                    fechaPago,
                    montoPago);
            guardarPagoEnDB(pago);

            // Calcular nuevo saldo
            double nuevoSaldo = Math.max(0, saldoActual - montoPago);
            if (nuevoSaldo < 0.005) {
                nuevoSaldo = 0;
            }

            // Actualizar saldo
            String sqlActualizar =
                    "UPDATE prestamos "
                    + "SET saldo_pendiente = ? "
                    + "WHERE id = ?";

            PreparedStatement psActualizar =
                    Operaciones.getConnection()
                            .prepareStatement(sqlActualizar);

            psActualizar.setDouble(1, nuevoSaldo);
            psActualizar.setInt(2, prestamoId);

            Operaciones.insertar_actualizar_borrar_BD(psActualizar);

            // Si el saldo llega a 0, marcar como pagado
            if (nuevoSaldo < 0.005) {
                String sqlEstado =
                        "UPDATE prestamos "
                        + "SET estado = 'PAGADO' "
                        + "WHERE id = ?";

                PreparedStatement psEstado =
                        Operaciones.getConnection()
                                .prepareStatement(sqlEstado);

                psEstado.setInt(1, prestamoId);
                Operaciones.insertar_actualizar_borrar_BD(psEstado);

                System.out.println("Prestamo pagado completamente.");
            } else {
                System.out.println("Pago registrado correctamente.");
            }

            registrarPago(pago);
            System.out.println("Saldo pendiente: $" + nuevoSaldo);

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }
    
    public static void eliminarPago(Scanner sc) {
        System.out.print("ID del pago que desea eliminar: ");
        if (!sc.hasNextInt()) {
            System.out.println("El ID debe ser un numero entero.");
            sc.next();
            return;
        }
        int id = sc.nextInt();
        if (id <= 0) {
            System.out.println("El ID debe ser un numero positivo.");
            return;
        }

        try (Connection connection = ConexionBD.MysConnection()) {
            if (connection == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
            connection.setAutoCommit(false);
            try {
                int prestamoId;
                BigDecimal montoPago;
                String pagoSql =
                        "SELECT prestamo_id, monto FROM pagos WHERE id = ? FOR UPDATE";
                try (PreparedStatement ps = connection.prepareStatement(pagoSql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            System.out.println("No existe un pago con ese ID.");
                            return;
                        }
                        prestamoId = rs.getInt("prestamo_id");
                        montoPago = rs.getBigDecimal("monto");
                    }
                }
                if (montoPago == null) {
                    throw new SQLException("El pago no tiene un monto valido.");
                }

                BigDecimal montoTotal;
                BigDecimal saldoActual;
                LocalDate fechaInicio;
                LocalDate fechaVencimiento;
                String prestamoSql =
                        "SELECT monto_total, saldo_pendiente, fecha_inicio, "
                        + "fecha_vencimiento FROM prestamos WHERE id = ? FOR UPDATE";
                try (PreparedStatement ps = connection.prepareStatement(prestamoSql)) {
                    ps.setInt(1, prestamoId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "No existe el prestamo asociado al pago.");
                        }
                        montoTotal = rs.getBigDecimal("monto_total");
                        saldoActual = rs.getBigDecimal("saldo_pendiente");
                        java.sql.Date inicio = rs.getDate("fecha_inicio");
                        java.sql.Date vencimiento = rs.getDate("fecha_vencimiento");
                        fechaInicio = inicio == null ? null : inicio.toLocalDate();
                        fechaVencimiento = vencimiento == null
                                ? null : vencimiento.toLocalDate();
                    }
                }
                if (montoTotal == null || saldoActual == null) {
                    throw new SQLException("El prestamo tiene un saldo no valido.");
                }
                if (montoPago.signum() <= 0 || montoTotal.signum() <= 0
                        || saldoActual.signum() < 0) {
                    throw new SQLException("El pago o el saldo del prestamo no son validos.");
                }

                BigDecimal nuevoSaldo = saldoActual.add(montoPago);
                if (nuevoSaldo.compareTo(montoTotal) > 0) {
                    throw new SQLException(
                            "El saldo del prestamo es inconsistente; no se elimino el pago.");
                }

                LocalDate hoy = LocalDate.now();
                String nuevoEstado;
                if (nuevoSaldo.signum() == 0) {
                    nuevoEstado = "PAGADO";
                } else if (fechaInicio != null && hoy.isBefore(fechaInicio)) {
                    nuevoEstado = "PENDIENTE";
                } else if (fechaVencimiento != null
                        && hoy.isAfter(fechaVencimiento)) {
                    nuevoEstado = "VENCIDO";
                } else {
                    nuevoEstado = "ACTIVO";
                }

                String actualizarSql =
                        "UPDATE prestamos SET saldo_pendiente = ?, estado = ? WHERE id = ?";
                try (PreparedStatement ps =
                             connection.prepareStatement(actualizarSql)) {
                    ps.setBigDecimal(1, nuevoSaldo);
                    ps.setString(2, nuevoEstado);
                    ps.setInt(3, prestamoId);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se pudo actualizar el saldo del prestamo.");
                    }
                }

                try (PreparedStatement ps =
                             connection.prepareStatement("DELETE FROM pagos WHERE id = ?")) {
                    ps.setInt(1, id);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se pudo eliminar el pago.");
                    }
                }

                connection.commit();
                pagos.removeIf(pago -> pago.getId() == id);
                System.out.println("Pago eliminado y saldo del prestamo actualizado.");
                System.out.println("Nuevo saldo pendiente: $" + nuevoSaldo);
            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    ex.addSuppressed(rollbackEx);
                }
                throw ex;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // BUSCAR PAGO
    public static void buscarPago(Scanner sc) {

        System.out.print("ID del pago: ");
        if (!sc.hasNextInt()) {
            System.out.println("El ID debe ser un numero.");
            sc.next();
            return;
        }
        int id = sc.nextInt();
        if (id <= 0) {
            System.out.println("El ID debe ser un numero positivo.");
            return;
        }

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql =
                    "SELECT * FROM pagos WHERE id = ?";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            if (rs.next()) {

                System.out.println("------ PAGO ENCONTRADO ------");

                System.out.println("ID pago: "
                        + rs.getInt("id"));

                System.out.println("ID prestamo: "
                        + rs.getInt("prestamo_id"));

                System.out.println("Fecha: "
                        + rs.getDate("fecha_pago"));

                System.out.println("Monto: $"
                        + rs.getDouble("monto"));

            } else {

                System.out.println("Pago no encontrado.");
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }


    // HISTORIAL DE PAGOS
    public static void verHistorialPagos(Scanner sc) {

        System.out.print("ID del prestamo: ");
        if (!sc.hasNextInt()) {
            System.out.println("El ID debe ser un numero.");
            sc.next();
            return;
        }
        int prestamoId = sc.nextInt();
        if (prestamoId <= 0) {
            System.out.println("El ID del prestamo debe ser positivo.");
            return;
        }

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql =
                    "SELECT * FROM pagos "
                    + "WHERE prestamo_id = ? "
                    + "ORDER BY fecha_pago";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ps.setInt(1, prestamoId);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            System.out.println("------ HISTORIAL DE PAGOS ------");

            boolean tienePagos = false;

            while (rs.next()) {

                tienePagos = true;

                System.out.println("----------------------------");

                System.out.println("ID pago: "
                        + rs.getInt("id"));

                System.out.println("Fecha: "
                        + rs.getDate("fecha_pago"));

                System.out.println("Monto: $"
                        + rs.getDouble("monto"));
            }

            if (!tienePagos) {
                System.out.println("Este prestamo no tiene pagos registrados.");
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }


    // CONSULTAR SALDO PENDIENTE
    public static void consultarSaldoPendiente(Scanner sc) {

        System.out.print("ID del prestamo: ");
        if (!sc.hasNextInt()) {
            System.out.println("El ID debe ser un numero.");
            sc.next();
            return;
        }
        int prestamoId = sc.nextInt();
        if (prestamoId <= 0) {
            System.out.println("El ID del prestamo debe ser positivo.");
            return;
        }

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());
            PrestamoControlador.sincronizarPrestamosVencidos();

            String sql =
                    "SELECT saldo_pendiente, estado "
                    + "FROM prestamos "
                    + "WHERE id = ?";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ps.setInt(1, prestamoId);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            if (rs.next()) {

                System.out.println("-------- SALDO PENDIENTE -------");

                System.out.println("Saldo pendiente: $"
                        + rs.getDouble("saldo_pendiente"));

                System.out.println("Estado: "
                        + rs.getString("estado"));

            } else {

                System.out.println("Prestamo no encontrado.");
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }
}
