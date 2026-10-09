/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Modelo.Clases;

/**
 *
 * @author Sebas
 */
public class Empleado extends Persona {
    
    private String rol;
    private double salario;
    
    // Constructor
    public Empleado(String rol, double salario, int id, String nombre, int documento, String correo) {
        super(id, nombre, documento, correo);
        this.rol = rol;
        this.salario = salario;
    }
    
    // Getters
    public String getRol() {
        return rol;
    }

    public double getSalario() {
        return salario;
    }
    
    // Setters

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    //ToStrign

    @Override
    public String toString() {
        return "Empleado{" + "rol=" + rol + ", salario=" + salario + '}';
    }

  
    
}
