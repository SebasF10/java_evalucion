/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Modelo.Clases;

/**
 *
 * @author Sebas
 */
public class Cliente extends Persona {
    
    private String telefono;
    
    //Constructor
    public Cliente(String telefono, int id, String nombre, int documento, String correo) {
        super(id, nombre, documento, correo);
        this.telefono = telefono;
    }
    
    //Getters

    public String getTelefono() {
        return telefono;
    }
    
    //Setters

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    //ToString

    @Override
    public String toString() {
        return "Cliente{" + "telefono=" + telefono + '}';
    }
    
    
}
