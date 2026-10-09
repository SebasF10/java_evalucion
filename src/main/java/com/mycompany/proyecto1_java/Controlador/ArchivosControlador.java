/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Controlador;

import com.mycompany.proyecto1_java.Modelo.Persistencia.ConexionBD;
import com.mycompany.proyecto1_java.Modelo.Persistencia.Operaciones;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArchivosControlador {

    public static void generarArchivos() {

        // Crear carpeta para los archivos
        File carpeta = new File("archivos");

        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        generarEmpleados();
        generarClientes();
        generarPrestamos();
        generarPagos();

        System.out.println("--------------------------------");
        System.out.println("ARCHIVOS GENERADOS CORRECTAMENTE");
        System.out.println("Carpeta: archivos");
        System.out.println("- empleados.txt");
        System.out.println("- clientes.txt");
        System.out.println("- prestamos.txt");
        System.out.println("- pagos.txt");
    }


    // GENERAR EMPLEADOS
    public static void generarEmpleados() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql = "SELECT * FROM empleados";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            PrintWriter archivo =
                    new PrintWriter(
                            new FileWriter("archivos/empleados.txt")
                    );

            archivo.println("------- EMPLEADOS CREDIYA -------");
            archivo.println();

            while (rs.next()) {

                archivo.println("ID: "
                        + rs.getInt("id"));

                archivo.println("Nombre: "
                        + rs.getString("nombre"));

                archivo.println("Documento: "
                        + rs.getString("documento"));

                archivo.println("Rol: "
                        + rs.getString("rol"));

                archivo.println("Correo: "
                        + rs.getString("correo"));

                archivo.println("Salario: $"
                        + rs.getDouble("salario"));

                archivo.println("----------------------------");
            }

            archivo.close();

        } catch (SQLException | IOException ex) {

            System.out.println(
                    "Error al generar empleados.txt: "
                    + ex.getMessage()
            );
        }
    }


    // GENERAR CLIENTES
    public static void generarClientes() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql = "SELECT * FROM clientes";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            PrintWriter archivo =
                    new PrintWriter(
                            new FileWriter("archivos/clientes.txt")
                    );

            archivo.println("--------- CLIENTES CREDIYA ----------");
            archivo.println();

            while (rs.next()) {

                archivo.println("ID: "
                        + rs.getInt("id"));

                archivo.println("Nombre: "
                        + rs.getString("nombre"));

                archivo.println("Documento: "
                        + rs.getString("documento"));

                archivo.println("Correo: "
                        + rs.getString("correo"));

                archivo.println("Teléfono: "
                        + rs.getString("telefono"));

                archivo.println("----------------------------");
            }

            archivo.close();

        } catch (SQLException | IOException ex) {

            System.out.println(
                    "Error al generar clientes.txt: "
                    + ex.getMessage()
            );
        }
    }


    // GENERAR PRÉSTAMOS
    public static void generarPrestamos() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql = "SELECT * FROM prestamos";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            PrintWriter archivo =
                    new PrintWriter(
                            new FileWriter("archivos/prestamos.txt")
                    );

            archivo.println("-------- PRESTAMOS CREDIYA --------");
            archivo.println();

            while (rs.next()) {

                archivo.println("ID prestamo: "
                        + rs.getInt("id"));

                archivo.println("ID cliente: "
                        + rs.getInt("cliente_id"));

                archivo.println("ID empleado: "
                        + rs.getInt("empleado_id"));

                archivo.println("Monto: $"
                        + rs.getDouble("monto"));

                archivo.println("Interes: "
                        + rs.getDouble("interes") + "%");

                archivo.println("Cuotas: "
                        + rs.getInt("cuotas"));

                archivo.println("Fecha inicio: "
                        + rs.getDate("fecha_inicio"));

                archivo.println("Estado: "
                        + rs.getString("estado"));

                archivo.println("Monto total: $"
                        + rs.getDouble("monto_total"));

                archivo.println("Valor cuota: $"
                        + rs.getDouble("valor_cuota"));

                archivo.println("Saldo pendiente: $"
                        + rs.getDouble("saldo_pendiente"));

                archivo.println("Fecha vencimiento: "
                        + rs.getDate("fecha_vencimiento"));

                archivo.println("----------------------------");
            }

            archivo.close();

        } catch (SQLException | IOException ex) {

            System.out.println(
                    "Error al generar prestamos.txt: "
                    + ex.getMessage()
            );
        }
    }


    // GENERAR PAGOS
    public static void generarPagos() {

        try {

            Operaciones.setConnection(ConexionBD.MysConnection());

            String sql = "SELECT * FROM pagos";

            PreparedStatement ps =
                    Operaciones.getConnection()
                            .prepareStatement(sql);

            ResultSet rs =
                    Operaciones.consultar_BD(ps);

            PrintWriter archivo =
                    new PrintWriter(
                            new FileWriter("archivos/pagos.txt")
                    );

            archivo.println("------- PAGOS CREDIYA --------");
            archivo.println();

            while (rs.next()) {

                archivo.println("ID pago: "
                        + rs.getInt("id"));

                archivo.println("ID préstamo: "
                        + rs.getInt("prestamo_id"));

                archivo.println("Fecha de pago: "
                        + rs.getDate("fecha_pago"));

                archivo.println("Monto: $"
                        + rs.getDouble("monto"));

                archivo.println("----------------------------");
            }

            archivo.close();

        } catch (SQLException | IOException ex) {

            System.out.println(
                    "Error al generar pagos.txt: "
                    + ex.getMessage()
            );
        }
    }
}