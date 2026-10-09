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

/**
 *
 * @author Sebas
 */

public class ClienteControlador {
    
    //Registrat
    public static void registarCliente(Scanner sc) {
        
        sc.nextLine();
        
        System.out.println("===== REGISTRAR CLIENTE  =====");
        
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        
        System.out.print("Documento: ");
        String documento = sc.nextLine().trim();
        
        System.out.print("Correo: ");
        String correo = sc.nextLine().trim();
        
        System.out.print("Telefono: ");
        String telefono = sc.nextLine().trim();

        if (nombre.isBlank() || documento.isBlank() || telefono.isBlank()) {
            System.out.println("Nombre, documento y telefono son obligatorios.");
            return;
        }
        if (!correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            System.out.println("El correo no tiene un formato valido.");
            return;
        }
        
        try {

            Operaciones.setConnection(
                    ConexionBD.MysConnection()
            );

            String validarSql =
                    "SELECT 1 FROM clientes WHERE documento = ? LIMIT 1";

            try (PreparedStatement validar =
                         Operaciones.getConnection().prepareStatement(validarSql)) {
                validar.setString(1, documento);
                try (ResultSet rs = validar.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Ya existe un cliente con ese documento.");
                        return;
                    }
                }
            }

            String sql = "INSERT INTO clientes "
                    + "(nombre, documento, correo, telefono) "
                    + "VALUES (?, ?, ?, ?)";

            int filas;
            try (PreparedStatement ps =
                         Operaciones.getConnection().prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, documento);
                ps.setString(3, correo);
                ps.setString(4, telefono);
                filas = ps.executeUpdate();
            }

            if (filas > 0) {
                System.out.println("Cliente registrado.");
            } else {
                System.out.println("No se pudo registrar.");
            }

        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                System.out.println("Ya existe un cliente con ese documento.");
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }
        
    //Listar
     public static void listarClientes() {

        String sql = "SELECT * FROM clientes ORDER BY id";
        try (Connection connection = ConexionBD.MysConnection()) {
            if (connection == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
            System.out.println("------- CLIENTES --------");
            boolean hayClientes = false;
            while (rs.next()) {
                hayClientes = true;
                System.out.println("------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Documento: " + rs.getString("documento"));
                System.out.println("Correo: " + rs.getString("correo"));
                System.out.println("Telefono: " + rs.getString("telefono"));
            }
            if (!hayClientes) {
                System.out.println("No hay clientes registrados.");
            }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void eliminarCliente(Scanner sc) {
        System.out.print("ID del cliente que desea eliminar: ");
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

        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection connection = ConexionBD.MysConnection()) {
            if (connection == null) {
                throw new SQLException("No se pudo conectar con la base de datos.");
            }
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                int filas = ps.executeUpdate();
                if (filas == 0) {
                    System.out.println("No existe un cliente con ese ID.");
                } else {
                    System.out.println("Cliente eliminado correctamente.");
                }
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1451) {
                System.out.println("No se puede eliminar el cliente porque tiene prestamos asociados.");
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    // BUSCAR
    
    public static void buscarClientes(Scanner sc) {

        System.out.print("ID del Cliente: ");
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
                    "SELECT * FROM clientes WHERE id = ?";

            PreparedStatement ps =
                    Operaciones.getConnection().prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            if (rs.next()) {
                
                System.out.println("--------------------------------");
                
                System.out.println("Empleado encontrado:");

                System.out.println("ID: " +
                        rs.getInt("id"));

                System.out.println("Nombre: " +
                        rs.getString("nombre"));

                System.out.println("Documento: " +
                        rs.getString("documento"));

                System.out.println("Correo: " +
                        rs.getString("correo"));

            } else {
                System.out.println("Cliente no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    // CONSULTAR PRESTAMOS DEL CLIENTE
    public static void consultarPrestamosCliente(Scanner sc) {

        System.out.print("ID del cliente: ");
        if (!sc.hasNextInt()) {
            System.out.println("El ID debe ser un numero.");
            sc.next();
            return;
        }
        int clienteId = sc.nextInt();
        if (clienteId <= 0) {
            System.out.println("El ID debe ser un numero positivo.");
            return;
        }

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());
            PrestamoControlador.sincronizarPrestamosVencidos();

            String sql = "SELECT * FROM prestamos WHERE cliente_id = ?";

            PreparedStatement ps =
                    Operaciones.getConnection().prepareStatement(sql);

            ps.setInt(1, clienteId);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            System.out.println("-------- PRESTAMOS DEL CLIENTE ---------");

            boolean tienePrestamos = false;

            while (rs.next()) {

                tienePrestamos = true;

                System.out.println("----------------------------");

                System.out.println("ID prestamo: "
                        + rs.getInt("id"));

                System.out.println("Monto: $"
                        + rs.getDouble("monto"));

                System.out.println("Interes: "
                        + rs.getDouble("interes") + "%");

                System.out.println("Cuotas: "
                        + rs.getInt("cuotas"));

                System.out.println("Monto total: $"
                        + rs.getDouble("monto_total"));

                System.out.println("Valor cuota: $"
                        + rs.getDouble("valor_cuota"));

                System.out.println("Saldo pendiente: $"
                        + rs.getDouble("saldo_pendiente"));

                System.out.println("Fecha inicio: "
                        + rs.getDate("fecha_inicio"));

                System.out.println("Fecha vencimiento: "
                        + rs.getDate("fecha_vencimiento"));

                System.out.println("Estado: "
                        + rs.getString("estado"));
            }

            if (!tienePrestamos) {
                System.out.println("El cliente no tiene prestamos registrados.");
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
     
        
    }
}
