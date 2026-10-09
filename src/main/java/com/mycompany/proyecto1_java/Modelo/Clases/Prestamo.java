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
public class Prestamo {
    private int id;
    private Cliente cliente;
    private Empleado empleado;
    private double monto;
    private double interes;
    private int cuotas;
    private LocalDate fechaInicio;
    private String estado;
    private double montoTotal;
    private double valorCuota;
    private double saldoPendiente;
    private LocalDate fechaVencimiento;
    
    //Constructor

    public Prestamo(int id, Cliente cliente, Empleado empleado, double monto, double interes, int cuotas, 
            LocalDate fechaInicio, String estado, double montoTotal, double valorCuota, double saldoPendiente, 
            LocalDate fechaVencimiento) {
        this.id = id;
        this.cliente = cliente;
        this.empleado = empleado;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
        this.montoTotal = montoTotal;
        this.valorCuota = valorCuota;
        this.saldoPendiente = saldoPendiente;
        this.fechaVencimiento = fechaVencimiento;
    }
    
    //Getters

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public double getMonto() {
        return monto;
    }

    public double getInteres() {
        return interes;
    }

    public int getCuotas() {
        return cuotas;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public String getEstado() {
        return estado;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public double getValorCuota() {
        return valorCuota;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    //Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setValorCuota(double valorCuota) {
        this.valorCuota = valorCuota;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    //ToString

    @Override
    public String toString() {
        return "Prestamo{" + "id=" + id + ", cliente=" + cliente + ", empleado=" + empleado 
                + ", monto=" + monto + ", interes=" + interes + ", cuotas=" + cuotas + ", fechaInicio=" 
                + fechaInicio + ", estado=" + estado + ", montoTotal=" + montoTotal + ", valorCuota=" 
                + valorCuota + ", saldoPendiente=" + saldoPendiente + ", fechaVencimiento=" + fechaVencimiento + '}';
    }
    
    
}
