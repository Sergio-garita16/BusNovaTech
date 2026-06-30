/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

/**
 *
 * @author dani1
 */
public class NodoTiquete {
   
    private String nombre;
    private String id;
    private int edad;
    private String monedaCuenta;
    private String horaCompra;
    private String horaAbordaje;
    private String servicio;
    private char tipoBus;
    private NodoTiquete siguiente; 

   
    public NodoTiquete(String nombre, String id, int edad, String monedaCuenta, String horaCompra, char tipoBus) {
        this.nombre = nombre;
        this.id = id;
        this.edad = edad;
        this.monedaCuenta = monedaCuenta;
        this.horaCompra = horaCompra;
        this.horaAbordaje = "-1";
        this.servicio = "NA";
        this.tipoBus = tipoBus;
        this.siguiente = null;
    }

 
    public NodoTiquete getSiguiente() { return siguiente; }
    public void setSiguiente(NodoTiquete siguiente) { this.siguiente = siguiente; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getMonedaCuenta() {
        return monedaCuenta;
    }

    public void setMonedaCuenta(String monedaCuenta) {
        this.monedaCuenta = monedaCuenta;
    }

    public String getHoraCompra() {
        return horaCompra;
    }

    public void setHoraCompra(String horaCompra) {
        this.horaCompra = horaCompra;
    }

    public String getHoraAbordaje() {
        return horaAbordaje;
    }

    public void setHoraAbordaje(String horaAbordaje) {
        this.horaAbordaje = horaAbordaje;
    }

    public String getService() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public char getTipoBus() {
        return tipoBus;
    }

    public void setTipoBus(char tipoBus) {
        this.tipoBus = tipoBus;
    }

}

