/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Controlador;

import com.mycompany.proyecto1_java.Modelo.Persistencia.ConexionBD;
import com.mycompany.proyecto1_java.Modelo.Persistencia.Operaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

public class PrestamoControlador {

    // CREAR PRESTAMO
    public static void crearPrestamo(Scanner sc) {

        sc.nextLine();

        System.out.println("-------- CREAR PRESTAMO --------");

        System.out.print("ID del cliente: ");
        if (!sc.hasNextInt()) {
            System.out.println("El ID del cliente debe ser un numero.");
            sc.next();
            return;
        }
        int clienteId = sc.nextInt();
        if (clienteId <= 0) {
            System.out.println("El ID del cliente debe ser positivo.");
            return;
        }

        System.out.print("ID del empleado: ");
        if (!sc.hasNextInt()) {
            System.out.println("El ID del empleado debe ser un numero.");
            sc.next();
            return;
        }
        int empleadoId = sc.nextInt();
        if (empleadoId <= 0) {
            System.out.println("El ID del empleado debe ser positivo.");
            return;
        }

        System.out.print("Monto del prestamo: ");
        if (!sc.hasNextDouble()) {
            System.out.println("El monto debe ser un numero.");
            sc.next();
            return;
        }
        double monto = sc.nextDouble();
        if (!Double.isFinite(monto) || monto <= 0) {
            System.out.println("El monto debe ser un numero valido mayor que 0.");
            return;
        }

        System.out.print("Interes (%): ");
        if (!sc.hasNextDouble()) {
            System.out.println("El interes debe ser un numero.");
            sc.next();
            return;
        }
        double interes = sc.nextDouble();
        if (!Double.isFinite(interes) || interes < 0) {
            System.out.println("El interes debe ser un numero valido y no negativo.");
            return;
        }

        System.out.print("Numero de cuotas: ");
        if (!sc.hasNextInt()) {
            System.out.println("El numero de cuotas debe ser un numero entero.");
            sc.next();
            return;
        }
        int cuotas = sc.nextInt();
        if (cuotas <= 0) {
            System.out.println("El numero de cuotas debe ser mayor que 0.");
            return;
        }

        sc.nextLine();

        LocalDate fechaInicio;
        LocalDate fechaVencimiento;
        try {
            System.out.print("Fecha de inicio (AAAA-MM-DD): ");
            fechaInicio = LocalDate.parse(sc.nextLine().trim());

            System.out.print("Fecha de vencimiento (AAAA-MM-DD): ");
            fechaVencimiento = LocalDate.parse(sc.nextLine().trim());
        } catch (DateTimeParseException ex) {
            System.out.println("La fecha no es valida. Use el formato AAAA-MM-DD.");
            return;
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            System.out.println("La fecha de inicio no puede estar en el pasado.");
            return;
        }
        if (!fechaVencimiento.isAfter(fechaInicio)) {
            System.out.println("La fecha de vencimiento debe ser posterior al inicio.");
            return;
        }

        // Calculo del prestamo
        
        double montoInteres = monto * interes / 100;
        double montoTotal = monto + montoInteres;
        double valorCuota = montoTotal / cuotas;
        double saldoPendiente = montoTotal;
        if (!Double.isFinite(montoTotal) || !Double.isFinite(valorCuota)) {
            System.out.println("El monto o el interes son demasiado grandes.");
            return;
        }
        String estado = "PENDIENTE";

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql = "INSERT INTO prestamos "
                    + "(cliente_id, empleado_id, monto, interes, cuotas, "
                    + "fecha_inicio, estado, monto_total, valor_cuota, "
                    + "saldo_pendiente, fecha_vencimiento) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    Operaciones.getConnection().prepareStatement(sql);

            ps.setInt(1, clienteId);
            ps.setInt(2, empleadoId);
            ps.setDouble(3, monto);
            ps.setDouble(4, interes);
            ps.setInt(5, cuotas);
            ps.setDate(6, java.sql.Date.valueOf(fechaInicio));
            ps.setString(7, estado);
            ps.setDouble(8, montoTotal);
            ps.setDouble(9, valorCuota);
            ps.setDouble(10, saldoPendiente);
            ps.setDate(11, java.sql.Date.valueOf(fechaVencimiento));

            int filas =
                    Operaciones.insertar_actualizar_borrar_BD(ps);

            if (filas > 0) {
                System.out.println("Prestamo creado correctamente.");
                System.out.println("Monto total: $" + montoTotal);
                System.out.println("Valor de cada cuota: $" + valorCuota);
                System.out.println("Saldo pendiente: $" + saldoPendiente);
                System.out.println("Estado: " + estado);
            } else {
                System.out.println("No se pudo crear el prestamo.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    // LISTAR PRESTAMOS
    public static void listarPrestamos() {
        String sql = "SELECT p.*, c.nombre AS nombre_cliente, "
                + "e.nombre AS nombre_empleado "
                + "FROM prestamos p "
                + "LEFT JOIN clientes c ON p.cliente_id = c.id "
                + "LEFT JOIN empleados e ON p.empleado_id = e.id "
                + "ORDER BY p.id";
        try (Connection connection = ConexionBD.MysConnection()) {
            if (connection == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
            Operaciones.setConnection(connection);
            sincronizarPrestamosVencidos();
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
            System.out.println("--------- PRESTAMOS ----------");
            boolean hayPrestamos = false;
            while (rs.next()) {
                hayPrestamos = true;
                System.out.println("----------------------------");
                System.out.println("ID: "
                        + rs.getInt("id"));
                System.out.println("Cliente: " + rs.getString("nombre_cliente")
                        + " (ID " + rs.getInt("cliente_id") + ")");
                System.out.println("Empleado: " + rs.getString("nombre_empleado")
                        + " (ID " + rs.getInt("empleado_id") + ")");
                System.out.println("Monto: $"
                        + rs.getDouble("monto"));
                System.out.println("Interes: "
                        + rs.getDouble("interes") + "%");
                System.out.println("Cuotas: "
                        + rs.getInt("cuotas"));
                System.out.println("Fecha inicio: "
                        + rs.getDate("fecha_inicio"));
                System.out.println("Fecha vencimiento: "
                        + rs.getDate("fecha_vencimiento"));
                System.out.println("Monto total: $"
                        + rs.getDouble("monto_total"));
                System.out.println("Valor cuota: $"
                        + rs.getDouble("valor_cuota"));
                System.out.println("Saldo pendiente: $"
                        + rs.getDouble("saldo_pendiente"));
                System.out.println("Estado: "
                        + rs.getString("estado"));
            }
            if (!hayPrestamos) {
                System.out.println("No hay prestamos registrados.");
            }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void eliminarPrestamo(Scanner sc) {
        System.out.print("ID del prestamo que desea eliminar: ");
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

        String sql = "DELETE FROM prestamos WHERE id = ?";
        try (Connection connection = ConexionBD.MysConnection()) {
            if (connection == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                int filas = ps.executeUpdate();
                if (filas == 0) {
                    System.out.println("No existe un prestamo con ese ID.");
                } else {
                    System.out.println("Prestamo eliminado correctamente.");
                }
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1451) {
                System.out.println("No se puede eliminar el prestamo porque tiene pagos asociados. Elimine primero sus pagos.");
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    // BUSCAR 
    public static void buscarPrestamo(Scanner sc) {

        System.out.print("\nID del prestamo: ");
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
            sincronizarPrestamosVencidos();

            String sql =
                    "SELECT * FROM prestamos WHERE id = ?";

            PreparedStatement ps =
                    Operaciones.getConnection().prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            if (rs.next()) {

                System.out.println("--------- PRESTAMO ENCONTRADO --------");

                System.out.println("ID: "
                        + rs.getInt("id"));

                System.out.println("Cliente ID: "
                        + rs.getInt("cliente_id"));

                System.out.println("Empleado ID: "
                        + rs.getInt("empleado_id"));

                System.out.println("Monto: $"
                        + rs.getDouble("monto"));

                System.out.println("Interes: "
                        + rs.getDouble("interes") + "%");

                System.out.println("Cuotas: "
                        + rs.getInt("cuotas"));

                System.out.println("Fecha inicio: "
                        + rs.getDate("fecha_inicio"));

                System.out.println("Fecha vencimiento: "
                        + rs.getDate("fecha_vencimiento"));

                System.out.println("Monto total: $"
                        + rs.getDouble("monto_total"));

                System.out.println("Valor cuota: $"
                        + rs.getDouble("valor_cuota"));

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
    
    // VER DETALLE DEL PRESTAMO
    public static void verDetallePrestamo(Scanner sc) {

        System.out.print("ID del prestamo: ");
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
            sincronizarPrestamosVencidos();

            String sql =
                    "SELECT p.*, "
                    + "c.nombre AS nombre_cliente, "
                    + "e.nombre AS nombre_empleado "
                    + "FROM prestamos p "
                    + "INNER JOIN clientes c ON p.cliente_id = c.id "
                    + "INNER JOIN empleados e ON p.empleado_id = e.id "
                    + "WHERE p.id = ?";

            PreparedStatement ps =
                    Operaciones.getConnection().prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            if (rs.next()) {

                System.out.println("------- DETALLE DEL PRESTAMO -------");

                System.out.println("ID prestamo: "
                        + rs.getInt("id"));

                System.out.println("Cliente: "
                        + rs.getString("nombre_cliente"));

                System.out.println("Empleado: "
                        + rs.getString("nombre_empleado"));

                System.out.println("Monto: $"
                        + rs.getDouble("monto"));

                System.out.println("Interes: "
                        + rs.getDouble("interes") + "%");

                System.out.println("Numero de cuotas: "
                        + rs.getInt("cuotas"));

                System.out.println("Monto total: $"
                        + rs.getDouble("monto_total"));

                System.out.println("Valor de cuota: $"
                        + rs.getDouble("valor_cuota"));

                System.out.println("Saldo pendiente: $"
                        + rs.getDouble("saldo_pendiente"));

                System.out.println("Fecha inicio: "
                        + rs.getDate("fecha_inicio"));

                System.out.println("Fecha vencimiento: "
                        + rs.getDate("fecha_vencimiento"));

                System.out.println("Estado: "
                        + rs.getString("estado"));

            } else {

                System.out.println("Prestamo no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    // CAMBIAR ESTADO
    public static void cambiarEstado(Scanner sc) {

        System.out.print("ID del prestamo: ");
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

        sc.nextLine();

        System.out.print("Nuevo estado (PENDIENTE, ACTIVO, VENCIDO, PAGADO): ");
        String estado = sc.nextLine().trim().toUpperCase(Locale.ROOT);
        if (!estado.equals("PENDIENTE") && !estado.equals("ACTIVO")
                && !estado.equals("VENCIDO") && !estado.equals("PAGADO")) {
            System.out.println("Estado no valido.");
            return;
        }

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());
            sincronizarPrestamosVencidos();

            if ("PAGADO".equals(estado)) {
                String sqlSaldo = "SELECT saldo_pendiente FROM prestamos WHERE id = ?";
                PreparedStatement psSaldo =
                        Operaciones.getConnection().prepareStatement(sqlSaldo);
                psSaldo.setInt(1, id);
                ResultSet rsSaldo = Operaciones.consultar_BD(psSaldo);
                if (!rsSaldo.next()) {
                    System.out.println("No se encontro el prestamo.");
                    return;
                }
                if (rsSaldo.getDouble("saldo_pendiente") > 0) {
                    System.out.println("No se puede marcar como pagado si tiene saldo pendiente.");
                    return;
                }
            }
            if ("VENCIDO".equals(estado)) {
                String sqlVencimiento =
                        "SELECT fecha_vencimiento, saldo_pendiente "
                        + "FROM prestamos WHERE id = ?";
                PreparedStatement psVencimiento =
                        Operaciones.getConnection()
                                .prepareStatement(sqlVencimiento);
                psVencimiento.setInt(1, id);
                ResultSet rsVencimiento =
                        Operaciones.consultar_BD(psVencimiento);
                if (!rsVencimiento.next()) {
                    System.out.println("No se encontro el prestamo.");
                    return;
                }
                java.sql.Date fechaVencimiento =
                        rsVencimiento.getDate("fecha_vencimiento");
                if (fechaVencimiento == null
                        || !fechaVencimiento.toLocalDate().isBefore(LocalDate.now())
                        || rsVencimiento.getDouble("saldo_pendiente") <= 0) {
                    System.out.println("Solo puede marcarse como vencido si ya paso la fecha y tiene saldo pendiente.");
                    return;
                }
            }

            String sql =
                    "UPDATE prestamos SET estado = ? WHERE id = ?";

            PreparedStatement ps =
                    Operaciones.getConnection().prepareStatement(sql);

            ps.setString(1, estado);
            ps.setInt(2, id);

            int filas =
                    Operaciones.insertar_actualizar_borrar_BD(ps);

            if (filas > 0) {
                sincronizarPrestamosVencidos();
                System.out.println("Estado actualizado.");
            } else {
                System.out.println("No se encontro el prestamo.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    // PRESTAMOS ACTIVOS
    public static void consultarPrestamosActivos() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());
            sincronizarPrestamosVencidos();

            String sql =
                    "SELECT * FROM prestamos WHERE estado = 'ACTIVO'";

            PreparedStatement ps =
                    Operaciones.getConnection().prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            System.out.println("--------- PRESTAMOS ACTIVOS ---------");

            while (rs.next()) {

                System.out.println("----------------------------");

                System.out.println("ID: "
                        + rs.getInt("id"));

                System.out.println("Cliente ID: "
                        + rs.getInt("cliente_id"));

                System.out.println("Monto total: $"
                        + rs.getDouble("monto_total"));

                System.out.println("Saldo pendiente: $"
                        + rs.getDouble("saldo_pendiente"));

                System.out.println("Estado: "
                        + rs.getString("estado"));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    // PRESTAMOS VENCIDOS
    public static void consultarPrestamosVencidos() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());
            sincronizarPrestamosVencidos();

            String sql =
                    "SELECT * FROM prestamos "
                    + "WHERE fecha_vencimiento < CURDATE() "
                    + "AND saldo_pendiente > 0";

            PreparedStatement ps =
                    Operaciones.getConnection().prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            System.out.println("------- PRESTAMOS VENCIDOS ---------");

            while (rs.next()) {

                System.out.println("----------------------------");

                System.out.println("ID: "
                        + rs.getInt("id"));

                System.out.println("Cliente ID: "
                        + rs.getInt("cliente_id"));

                System.out.println("Monto total: $"
                        + rs.getDouble("monto_total"));

                System.out.println("Saldo pendiente: $"
                        + rs.getDouble("saldo_pendiente"));

                System.out.println("Fecha vencimiento: "
                        + rs.getDate("fecha_vencimiento"));

                System.out.println("Estado: "
                        + rs.getString("estado"));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void sincronizarPrestamosVencidos() throws SQLException {
        String sql = "UPDATE prestamos SET estado = 'VENCIDO' "
                + "WHERE fecha_vencimiento < CURDATE() "
                + "AND saldo_pendiente > 0 "
                + "AND (estado IS NULL OR estado <> 'PAGADO')";
        try (PreparedStatement ps =
                     Operaciones.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }
    
}
