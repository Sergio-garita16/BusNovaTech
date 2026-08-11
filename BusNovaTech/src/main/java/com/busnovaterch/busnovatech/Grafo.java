/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author XPC
 */
public class Grafo {

    private static final String ARCHIVO = "grafo.json";
    private Nodolocalidad frente;

    public Nodolocalidad getFrente() {
        return frente;
    }

    public Nodolocalidad buscarLocalidad(String nombre) {
        Nodolocalidad actual = frente;
        while (actual != null) {
            if (actual.getNombre().equalsIgnoreCase(nombre)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public void agregarLocalidadInterno(String nombre) {
        Nodolocalidad nueva = new Nodolocalidad(nombre);
        if (frente == null) {
            frente = nueva;
        } else {
            Nodolocalidad actual = frente;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nueva);
        }
    }

    public void agregarRutaInterno(String origen, String destino, double peso) {
        Nodolocalidad locOrigen = buscarLocalidad(origen);
        Nodolocalidad locDestino = buscarLocalidad(destino);
        if (locOrigen != null && locDestino != null) {
            locOrigen.agregarArista(new NodoRuta(locDestino, peso));
        }
    }

    public String obtenerGrafoTexto() {
        if (frente == null) {
            return "Todavía no hay localidades registradas.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo de localidades y rutas:\n\n");

        Nodolocalidad loc = frente;
        while (loc != null) {
            sb.append("• ").append(loc.getNombre()).append(":\n");
            NodoRuta arista = loc.getPrimeraArista();
            if (arista == null) {
                sb.append("   (sin rutas salientes)\n");
            } else {
                while (arista != null) {
                    sb.append("   -> ").append(arista.getDestino().getNombre())
                            .append(" (").append(arista.getPeso()).append(" km)\n");
                    arista = arista.getSiguiente();
                }
            }
            sb.append("\n");
            loc = loc.getSiguiente();
        }
        return sb.toString();
    }

    public void guardarGrafo() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"localidades\": [\n");
        Nodolocalidad loc = frente;
        while (loc != null) {
            sb.append("    {\"nombre\": \"").append(Jsonutils.escapar(loc.getNombre())).append("\"}");
            loc = loc.getSiguiente();
            if (loc != null) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ],\n  \"rutas\": [\n");

        boolean primero = true;
        loc = frente;
        while (loc != null) {
            NodoRuta r = loc.getPrimeraArista();
            while (r != null) {
                if (!primero) {
                    sb.append(",\n");
                }
                sb.append("    {\"origen\": \"").append(Jsonutils.escapar(loc.getNombre()))
                        .append("\", \"destino\": \"").append(Jsonutils.escapar(r.getDestino().getNombre()))
                        .append("\", \"peso\": ").append(r.getPeso()).append("}");
                primero = false;
                r = r.getSiguiente();
            }
            loc = loc.getSiguiente();
        }
        sb.append("\n  ]\n}");

        try (Writer w = new OutputStreamWriter(new FileOutputStream(ARCHIVO), StandardCharsets.UTF_8)) {
            w.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
