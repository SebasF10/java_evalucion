/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Vista;


import com.mycompany.proyecto1_java.Controlador.ReporteControlador;
import java.util.Scanner;

public class MenuReporte {

    public static void mostrar(Scanner sc) {

        int opcion = -1;

        do {
            System.out.println("---------------------------------");
            System.out.println("         MENU REPORTES");
            System.out.println("1. Prestamos activos");
            System.out.println("2. Prestamos vencidos");
            System.out.println("3. Clientes morosos");
            System.out.println("4. Prestamos pagados");
            System.out.println("5. Total prestado");
            System.out.println("6. Total recaudado");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");
            System.out.println("---------------------------------");

            if (!sc.hasNextInt()) {
                System.out.println("Ingrese un numero de opcion valido.");
                sc.next();
                continue;
            }
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    ReporteControlador.prestamosActivos();
                    break;

                case 2:
                    ReporteControlador.prestamosVencidos();
                    break;

                case 3:
                    ReporteControlador.clientesMorosos();
                    break;

                case 4:
                    ReporteControlador.prestamosPagados();
                    break;

                case 5:
                    ReporteControlador.totalPrestado();
                    break;

                case 6:
                    ReporteControlador.totalRecaudado();
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);
    }
}