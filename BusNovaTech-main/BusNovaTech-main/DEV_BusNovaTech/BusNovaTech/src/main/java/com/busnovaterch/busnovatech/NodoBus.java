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
    
  
    private ColaPrioridad filaClientes; 
    
  
    private NodoBus siguiente; 
    
    public NodoBus(int idBus, char tipoBus) {
        this.idBus = idBus;
        this.tipoBus = tipoBus;
        this.inspectorOcupado = false; 
        this.filaClientes = new ColaPrioridad(); 
        this.siguiente = null; 
    }

    
    public int getIdBus() { return idBus; }
    public void setIdBus(int idBus) { this.idBus = idBus; }

    public char getTipoBus() { return tipoBus; }
    public void setTipoBus(char tipoBus) { this.tipoBus = tipoBus; }

    public boolean isInspectorOcupado() { return inspectorOcupado; }
    public void setInspectorOcupado(boolean inspectorOcupado) { this.inspectorOcupado = inspectorOcupado; }

    public ColaPrioridad getFilaClientes() { return filaClientes; }
    public void setFilaClientes(ColaPrioridad filaClientes) { this.filaClientes = filaClientes; }

    public NodoBus getSiguiente() { return siguiente; }
    public void setSiguiente(NodoBus siguiente) { this.siguiente = siguiente; }
}


