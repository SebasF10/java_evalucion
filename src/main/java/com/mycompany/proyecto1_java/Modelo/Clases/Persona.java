/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Modelo.Clases;

/**
 *
 * @author Sebas
 */
public abstract class Persona {
    
    private int id;
    private String nombre;
    private int documento;
    private String correo;
    
    //Constructor

    public Persona(int id, String nombre, int documento, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }
    
    // Getters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDocumento() {
        return documento;
    }

    public String getCorreo() {
        return correo;
    }
    
    //Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    //ToStrign

    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", nombre=" + nombre + ", documento=" + documento + ", correo=" + correo + '}';
    }
    
}
