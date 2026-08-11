/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

/**
 *
 * @author XPC
 */
public class NodoRuta {

    private Nodolocalidad destino;
    private double peso;
    private NodoRuta siguiente;
 
    public NodoRuta(Nodolocalidad destino, double peso) {
        this.destino = destino;
        this.peso = peso;
        this.siguiente = null;
    }
 
    public Nodolocalidad getDestino() { return destino; }
    public void setDestino(Nodolocalidad destino) { this.destino = destino; }
 
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
 
    public NodoRuta getSiguiente() { return siguiente; }
    public void setSiguiente(NodoRuta siguiente) { this.siguiente = siguiente; 
    }    
}
