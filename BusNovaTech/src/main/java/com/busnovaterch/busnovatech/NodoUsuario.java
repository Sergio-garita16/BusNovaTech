/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

/**
 *
 * @author XPC
 */
public class NodoUsuario {
 private String nombre;
    private String usuario;
    private String clave;
    private NodoUsuario siguiente;
 
    public NodoUsuario(String nombre, String usuario, String clave) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.clave = clave;
        this.siguiente = null;
    }
 
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
 
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
 
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
 
    public NodoUsuario getSiguiente() { return siguiente; }
    public void setSiguiente(NodoUsuario siguiente) { this.siguiente = siguiente; }   
}
