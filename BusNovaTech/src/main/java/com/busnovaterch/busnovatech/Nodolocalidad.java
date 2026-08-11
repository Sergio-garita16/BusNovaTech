/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

/**
 *
 * @author XPC
 */
public class Nodolocalidad {
    
    private String nombre;
    private NodoRuta primeraArista;
    private Nodolocalidad siguiente;
 
    public Nodolocalidad(String nombre) {
        this.nombre = nombre;
        this.primeraArista = null;
        this.siguiente = null;
    }
 
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
 
    public NodoRuta getPrimeraArista() { return primeraArista; }
    public void setPrimeraArista(NodoRuta primeraArista) { this.primeraArista = primeraArista; }
 
    public Nodolocalidad getSiguiente() { return siguiente; }
    public void setSiguiente(Nodolocalidad siguiente) { this.siguiente = siguiente; }
 
    /** Agrega una ruta saliente (arista) al final de la lista de esta localidad. */
    public void agregarArista(NodoRuta nueva) {
        if (primeraArista == null) {
            primeraArista = nueva;
        } else {
            NodoRuta actual = primeraArista;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nueva);
        }
    }   
}
