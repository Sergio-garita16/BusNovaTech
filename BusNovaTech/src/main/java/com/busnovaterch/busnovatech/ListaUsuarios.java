/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

/**
 *
 * @author XPC
 */
public class ListaUsuarios {
 private NodoUsuario frente;
    private int tamano;
 
    public ListaUsuarios() {
        this.frente = null;
        this.tamano = 0;
    }
 
    public void insertar(NodoUsuario nuevo) {
        if (frente == null) {
            frente = nuevo;
        } else {
            NodoUsuario actual = frente;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }
 
    public NodoUsuario buscar(String usuario) {
        NodoUsuario actual = frente;
        while (actual != null) {
            if (actual.getUsuario().equalsIgnoreCase(usuario)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }
 
    public NodoUsuario getFrente() { 
        return frente; 
    }
    public int getTamano() { 
        return tamano; 
    }
    public boolean estaVacia() { 
        return frente == null; 
    }    
}
