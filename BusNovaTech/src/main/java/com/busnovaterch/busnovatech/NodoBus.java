/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

/**
 *
 * @author dani1
 */
public class NodoBus {
    private int idBus;
    private char tipoBus;
    private boolean inspectorOcupado;
    private int cantidadPasajerosAbordo;
    
  
    private ColaPrioridad filaClientes; 
    
  
    private NodoBus siguiente; 
    
    public NodoBus(int idBus, char tipoBus) {
        this.idBus = idBus;
        this.tipoBus = tipoBus;
        this.inspectorOcupado = false; 
        this.cantidadPasajerosAbordo = 0;
        this.filaClientes = new ColaPrioridad(); 
        this.siguiente = null; 
    }

    
    public int getIdBus() { return idBus; }
    public void setIdBus(int idBus) { this.idBus = idBus; }

    public char getTipoBus() { return tipoBus; }
    public void setTipoBus(char tipoBus) { this.tipoBus = tipoBus; }

    public boolean isInspectorOcupado() { return inspectorOcupado; }
    public void setInspectorOcupado(boolean inspectorOcupado) { this.inspectorOcupado = inspectorOcupado; }

    /** Cantidad de pasajeros que ya abordaron este bus (Módulo 1.2: llenado de buses según atención). */
    public int getCantidadPasajerosAbordo() { return cantidadPasajerosAbordo; }
    public void setCantidadPasajerosAbordo(int cantidadPasajerosAbordo) { this.cantidadPasajerosAbordo = cantidadPasajerosAbordo; }
    public void incrementarPasajerosAbordo() { this.cantidadPasajerosAbordo++; }

    public ColaPrioridad getFilaClientes() { return filaClientes; }
    public void setFilaClientes(ColaPrioridad filaClientes) { this.filaClientes = filaClientes; }

    public NodoBus getSiguiente() { return siguiente; }
    public void setSiguiente(NodoBus siguiente) { this.siguiente = siguiente; }
}


