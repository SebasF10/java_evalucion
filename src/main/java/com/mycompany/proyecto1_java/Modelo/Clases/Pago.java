/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1_java.Modelo.Clases;

import java.time.LocalDate;

/**
 *
 * @author Sebas
 */
public class Pago {
    
    private int id;
    private Prestamo prestamo;
    private LocalDate fechaPago;
    private double monto;

    public Pago(int id, Prestamo prestamo, LocalDate fechaPago, double monto) {
        this.id = id;
        this.prestamo = prestamo;
        this.fechaPago = fechaPago;
        this.monto = monto;
    }
    
    //Getters

    public int getId() {
        return id;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public double getMonto() {
        return monto;
    }
    
    //Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
    
    //ToString

    @Override
    public String toString() {
        return "Pago{" + "id=" + id + ", prestamo=" + prestamo + ", fechaPago=" + fechaPago + ", monto=" + monto + '}';
    }
    
}
