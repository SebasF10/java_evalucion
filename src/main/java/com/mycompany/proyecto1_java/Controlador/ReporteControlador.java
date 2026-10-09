/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Controlador;

import com.mycompany.proyecto1_java.Modelo.Persistencia.ConexionBD;
import com.mycompany.proyecto1_java.Modelo.Persistencia.Operaciones;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ReporteControlador {

    // PRESTAMOS ACTIVOS
    public static void prestamosActivos() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());
            PrestamoControlador.sincronizarPrestamosVencidos();

            String sql = "SELECT * FROM prestamos WHERE estado = 'ACTIVO'";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            List<Integer> prestamos = new ArrayList<>();

            while (rs.next()) {
                prestamos.add(rs.getInt("id"));
            }

            System.out.println("------ PRESTAMOS ACTIVOS -------");

            if (prestamos.isEmpty()) {
                System.out.println("No hay prestamos activos.");
            } else {

                prestamos.stream()
                        .forEach(id ->
                                System.out.println("Prestamo ID: " + id)
                        );
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }


    // PRESTAMOS VENCIDOS
    public static void prestamosVencidos() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());
            PrestamoControlador.sincronizarPrestamosVencidos();

            String sql =
                    "SELECT * FROM prestamos "
                    + "WHERE fecha_vencimiento < CURDATE() "
                    + "AND saldo_pendiente > 0";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            System.out.println("------PRESTAMOS VENCIDOS ------");

            boolean encontrado = false;

            while (rs.next()) {

                encontrado = true;

                System.out.println("----------------------------");

                System.out.println("ID prestamo: "
                        + rs.getInt("id"));

                System.out.println("Cliente ID: "
                        + rs.getInt("cliente_id"));

                System.out.println("Saldo pendiente: $"
                        + rs.getDouble("saldo_pendiente"));

                System.out.println("Fecha vencimiento: "
                        + rs.getDate("fecha_vencimiento"));
            }

            if (!encontrado) {
                System.out.println("No hay prestamos vencidos.");
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }


    // CLIENTES MOROSOS
    public static void clientesMorosos() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());
            PrestamoControlador.sincronizarPrestamosVencidos();

            String sql =
                    "SELECT c.id, c.nombre, c.documento, "
                    + "p.id AS prestamo_id, "
                    + "p.saldo_pendiente "
                    + "FROM clientes c "
                    + "INNER JOIN prestamos p "
                    + "ON c.id = p.cliente_id "
                    + "WHERE p.fecha_vencimiento < CURDATE() "
                    + "AND p.saldo_pendiente > 0";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            System.out.println("------ CLIENTES MOROSOS ---------");

            boolean encontrado = false;

            while (rs.next()) {

                encontrado = true;

                System.out.println("----------------------------");

                System.out.println("ID cliente: "
                        + rs.getInt("id"));

                System.out.println("Nombre: "
                        + rs.getString("nombre"));

                System.out.println("Documento: "
                        + rs.getString("documento"));

                System.out.println("Prestamo: "
                        + rs.getInt("prestamo_id"));

                System.out.println("Saldo pendiente: $"
                        + rs.getDouble("saldo_pendiente"));
            }

            if (!encontrado) {
                System.out.println("No hay clientes morosos.");
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }


    // PRESTAMOS PAGADOS
    public static void prestamosPagados() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql =
                    "SELECT * FROM prestamos WHERE estado = 'PAGADO'";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            List<Double> montos = new ArrayList<>();

            while (rs.next()) {
                montos.add(rs.getDouble("monto_total"));
            }

            System.out.println("------- PRESTAMOS PAGADOS --------");

            if (montos.isEmpty()) {

                System.out.println("No hay prestamos pagados.");

            } else {

                montos.stream()
                        .forEach(monto ->
                                System.out.println(
                                        "Monto pagado: $" + monto
                                )
                        );
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }


    // TOTAL PRESTADO
    public static void totalPrestado() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql = "SELECT monto FROM prestamos";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            List<Double> montos = new ArrayList<>();

            while (rs.next()) {
                montos.add(rs.getDouble("monto"));
            }

            double total = montos.stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

            System.out.println("-------- TOTAL PRESTADO --------");
            System.out.println("Total prestado: $" + total);

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }


    // TOTAL RECAUDADO
    public static void totalRecaudado() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql = "SELECT monto FROM pagos";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            List<Double> pagos = new ArrayList<>();

            while (rs.next()) {
                pagos.add(rs.getDouble("monto"));
            }

            double total = pagos.stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

            System.out.println("------- TOTAL RECAUDADO -------");
            System.out.println("Total recaudado: $" + total);

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }
}