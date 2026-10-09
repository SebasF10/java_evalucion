/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecto1_java.Vista;

import com.mycompany.proyecto1_java.Controlador.ArchivosControlador;
import java.util.Scanner;

public class Proyecto1_Java {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        do {
            System.out.println("================================");
            System.out.println("1. Empleados");
            System.out.println("2. Clientes");
            System.out.println("3. Prestamos");
            System.out.println("4. Pagos");
            System.out.println("5. Reportes");
            System.out.println("6 Archivos TXT");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            if (!sc.hasNextInt()) {
                System.out.println("Ingrese un numero de opcion valido.");
                sc.next();
                continue;
            }
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    MenuEmpleado.mostrar(sc);
                    break;

                case 2:
                    MenuCliente.mostrar(sc);
                    break;

                case 3:
                    MenuPrestamo.mostrar(sc);
                    break;

                case 4:
                    MenuPago.mostrar(sc);
                    break;

                case 5:
                    MenuReporte.mostrar(sc);
                    break;
                
                case 6:
                    ArchivosControlador.generarArchivos();
                    break;
                    
                case 0:
                    System.out.println("Gracias por utilizar CrediYa.");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);

        sc.close();
    }
}