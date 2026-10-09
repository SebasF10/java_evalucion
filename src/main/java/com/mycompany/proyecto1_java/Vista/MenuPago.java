/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Vista;


import com.mycompany.proyecto1_java.Controlador.GestorPagos;
import java.util.Scanner;

public class MenuPago {

    public static void mostrar(Scanner sc) {

        int opcion = -1;

        do {
            System.out.println("---------------------------------");
            System.out.println("          MENU PAGOS");
            System.out.println("1. Registrar pago");
            System.out.println("2. Listar pagos");
            System.out.println("3. Buscar pago");
            System.out.println("4. Ver historial de pagos");
            System.out.println("5. Consultar saldo pendiente");
            System.out.println("6. Eliminar pago");
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
                    GestorPagos.registrarPago(sc);
                    break;

                case 2:
                    GestorPagos.listarPagos();
                    break;

                case 3:
                    GestorPagos.buscarPago(sc);
                    break;
                case 4:
                    GestorPagos.verHistorialPagos(sc);
                    break;

                case 5:
                    GestorPagos.consultarSaldoPendiente(sc);
                    break;

                case 6:
                    GestorPagos.eliminarPago(sc);
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