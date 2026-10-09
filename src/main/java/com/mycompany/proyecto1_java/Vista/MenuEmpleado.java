/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Vista;

import com.mycompany.proyecto1_java.Controlador.EmpleadoControlador;
import java.util.Scanner;

public class MenuEmpleado {

    public static void mostrar(Scanner sc) {

        int opcion = -1;

        do {

            System.out.println("================================");
            System.out.println("        MENU EMPLEADOS");
            System.out.println("1. Registrar empleado");
            System.out.println("2. Listar empleados");
            System.out.println("3. Buscar empleado");
            System.out.println("4. Eliminar empleado");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            if (!sc.hasNextInt()) {
                System.out.println("Ingrese un numero de opcion valido.");
                sc.next();
                continue;
            }
            opcion = sc.nextInt();

            switch (opcion) {

                case 1:
                    EmpleadoControlador.registrarEmpleado(sc);
                    break;

                case 2:
                    EmpleadoControlador.listarEmpleados();
                    break;

                case 3:
                    EmpleadoControlador.buscarEmpleado(sc);
                    break;

                case 4:
                    EmpleadoControlador.eliminarEmpleado(sc);
                    break;

                case 0:
                    System.out.println("Volviendo...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);
    }
}
