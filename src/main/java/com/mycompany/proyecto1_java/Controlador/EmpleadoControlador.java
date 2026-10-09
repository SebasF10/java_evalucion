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
import java.util.Scanner;

public class EmpleadoControlador {

    // REGISTRAR
    public static void registrarEmpleado(Scanner sc) {

        sc.nextLine();

        System.out.println("------ REGISTRAR EMPLEADO -------");

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Documento: ");
        String documento = sc.nextLine().trim();

        System.out.print("Rol: ");
        String rol = sc.nextLine().trim();

        System.out.print("Correo: ");
        String correo = sc.nextLine().trim();

        System.out.print("Salario: ");
        if (!sc.hasNextDouble()) {
            System.out.println("El salario debe ser un numero.");
            sc.next();
            return;
        }
        double salario = sc.nextDouble();

        if (nombre.isBlank() || documento.isBlank() || rol.isBlank()) {
            System.out.println("Nombre, documento y rol son obligatorios.");
            return;
        }
        if (!correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            System.out.println("El correo no tiene un formato valido.");
            return;
        }
        if (!Double.isFinite(salario) || salario < 0) {
            System.out.println("El salario debe ser un numero valido y no negativo.");
            return;
        }

        try {

            Operaciones.setConnection(
                    ConexionBD.MysConnection()
            );

            String validarSql =
                    "SELECT 1 FROM empleados WHERE documento = ? LIMIT 1";

            try (PreparedStatement validar =
                         Operaciones.getConnection().prepareStatement(validarSql)) {
                validar.setString(1, documento);
                try (ResultSet rs = validar.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Ya existe un empleado con ese documento.");
                        return;
                    }
                }
            }

            String sql = "INSERT INTO empleados "
                    + "(nombre, documento, rol, correo, salario) "
                    + "VALUES (?, ?, ?, ?, ?)";

            int filas;
            try (PreparedStatement ps =
                         Operaciones.getConnection().prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, documento);
                ps.setString(3, rol);
                ps.setString(4, correo);
                ps.setDouble(5, salario);
                filas = ps.executeUpdate();
            }

            if (filas > 0) {
                System.out.println("Empleado registrado.");
            } else {
                System.out.println("No se pudo registrar.");
            }

        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                System.out.println("Ya existe un empleado con ese documento.");
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }

    // LISTAR
    public static void listarEmpleados() {
        String sql = "SELECT * FROM empleados ORDER BY id";
        try (Connection connection = ConexionBD.MysConnection()) {
            if (connection == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
            System.out.println("---- EMPLEADOS ------");
            boolean hayEmpleados = false;
            while (rs.next()) {
                hayEmpleados = true;
                System.out.println("------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Documento: " + rs.getString("documento"));
                System.out.println("Rol: " + rs.getString("rol"));
                System.out.println("Correo: " + rs.getString("correo"));
                System.out.println("Salario: " + rs.getDouble("salario"));
            }
            if (!hayEmpleados) {
                System.out.println("No hay empleados registrados.");
            }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void eliminarEmpleado(Scanner sc) {
        System.out.print("ID del empleado que desea eliminar: ");
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

        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection connection = ConexionBD.MysConnection()) {
            if (connection == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                int filas = ps.executeUpdate();
                if (filas == 0) {
                    System.out.println("No existe un empleado con ese ID.");
                } else {
                    System.out.println("Empleado eliminado correctamente.");
                }
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1451) {
                System.out.println("No se puede eliminar el empleado porque tiene prestamos asociados.");
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }

    // BUSCAR
    public static void buscarEmpleado(Scanner sc) {

        System.out.print("ID del empleado: ");
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

            Operaciones.setConnection(
                    ConexionBD.MysConnection()
            );

            String sql =
                    "SELECT * FROM empleados WHERE id = ?";

            PreparedStatement ps =
                    Operaciones.getConnection().prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            if (rs.next()) {

                System.out.println("Empleado encontrado:");

                System.out.println("ID: " +
                        rs.getInt("id"));

                System.out.println("Nombre: " +
                        rs.getString("nombre"));

                System.out.println("Documento: " +
                        rs.getString("documento"));

                System.out.println("Rol: " +
                        rs.getString("rol"));

                System.out.println("Correo: " +
                        rs.getString("correo"));

                System.out.println("Salario: " +
                        rs.getDouble("salario"));

            } else {
                System.out.println("Empleado no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
