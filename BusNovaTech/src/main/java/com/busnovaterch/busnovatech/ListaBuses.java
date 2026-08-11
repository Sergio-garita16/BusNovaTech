/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

/**
 *
 * @author XPC
 */
public class ListaBuses {
 private NodoBus frente;
    private int tamano;
 
    public ListaBuses() {
        this.frente = null;
        this.tamano = 0;
    }
 
    public void insertar(NodoBus nuevo) {
        if (frente == null) {
            frente = nuevo;
        } else {
            NodoBus actual = frente;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }
 
    /* Busca el primer bus que coincida con el tipo indicado('P' = preferencial, 'D' = directo, 'N' = normal). */
    public NodoBus buscarPorTipo(char tipo) {
        NodoBus actual = frente;
        while (actual != null) {
            if (Character.toUpperCase(actual.getTipoBus()) == Character.toUpperCase(tipo)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }
 
    public NodoBus buscarPorId(int id) {
        NodoBus actual = frente;
        while (actual != null) {
            if (actual.getIdBus() == id) return actual;
            actual = actual.getSiguiente();
        }
        return null;
    }
 
    /*De todos los buses normales ('N'), retorna el que tenga menos personas en su fila, en caso de empate, retorna el primero encontrado.
     */
    
    public NodoBus buscarBusNormalConMenosClientes() {
        NodoBus actual = frente;
        NodoBus mejor = null;
        while (actual != null) {
            if (Character.toUpperCase(actual.getTipoBus()) == 'N') {
                if (mejor == null || actual.getFilaClientes().getTamaño() < mejor.getFilaClientes().getTamaño()) {
                    mejor = actual;
                }
            }
            actual = actual.getSiguiente();
        }
        return mejor;
    }
 
    public NodoBus getFrente() { return frente; }
    public int getTamano() { return tamano; }
    public boolean estaVacia() { return frente == null;
    }
}
