/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Vista;

import com.mycompany.proyecto1_java.Controlador.ClienteControlador;
import java.util.Scanner;

public class MenuCliente {

    public static void mostrar(Scanner sc) {

        int opcion = -1;

        do {
            System.out.println("================================");
            System.out.println("         MENU CLIENTES");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente");
            System.out.println("4. Consultar prestamos del cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");

            if (!sc.hasNextInt()) {
                System.out.println("Ingrese un numero de opcion valido.");
                sc.next();
                continue;
            }
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    ClienteControlador.registarCliente(sc);
                    break;

                case 2:
                    ClienteControlador.listarClientes();
                    break;

                case 3:
                    ClienteControlador.buscarClientes(sc);
                    break;

                case 4:
                    ClienteControlador.consultarPrestamosCliente(sc);
                    break;

                case 5:
                    ClienteControlador.eliminarCliente(sc);
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