/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

/**
 *
 * @author dani1
 */
public class ColaPrioridad {
    private NodoTiquete frente;
    private int tamano;

    public ColaPrioridad() {
        this.frente = null;
        this.tamano = 0;
    }

    public void insertarPorPrioridad(NodoTiquete nuevo) {
        if (frente == null) {
            frente = nuevo;
        } 
        else if (tieneMasPrioridad(nuevo, frente)) {
            nuevo.setSiguiente(frente);
            frente = nuevo;
        } 
        else {
            NodoTiquete actual = frente;
            while (actual.getSiguiente() != null && !tieneMasPrioridad(nuevo, actual.getSiguiente())) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        tamano++;
    }

    private boolean tieneMasPrioridad(NodoTiquete t1, NodoTiquete t2) {
        if (t1.getEdad() >= 65 && t2.getEdad() < 65) {
            return true;
        }
        if (t1.getEdad() < 65 && t2.getEdad() >= 65) {
            return false;
        }

        int p1 = obtenerPuntajeServicio(t1.getService());
        int p2 = obtenerPuntajeServicio(t2.getService());
        
        return p1 > p2;
    }

    private int obtenerPuntajeServicio(String servicio) {
        if (servicio == null) return 0;
        switch (servicio.toUpperCase()) {
            case "EJECUTIVO": return 3;
            case "VIP": return 2;
            case "CARGA": return 1;
            default: return 0;
        }
    }

    public NodoTiquete desencolar() {
        if (estaVacia()) {
            return null;
        }
        NodoTiquete aux = frente;
        frente = frente.getSiguiente();
        tamano--;
        return aux;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamaño() {
        return tamano;
    }
}
