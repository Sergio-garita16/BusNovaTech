/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author XPC
 */
public class Config {

    private static final String ARCHIVO = "config.json";
    private String nombreTerminal;
    private ListaBuses buses;
    private ListaUsuarios usuarios;

    public Config() {
        this.buses = new ListaBuses();
        this.usuarios = new ListaUsuarios();
    }

    public String getNombreTerminal() {
        return nombreTerminal;
    }

    public void setNombreTerminal(String nombreTerminal) {
        this.nombreTerminal = nombreTerminal;
    }

    public ListaBuses getBuses() {
        return buses;
    }

    public ListaUsuarios getUsuarios() {
        return usuarios;
    }

    public boolean cargarDesdeJSON() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            String json = sb.toString();
            this.nombreTerminal = Jsonutils.valorTexto(json, "terminal");

            String bloqueBuses = Jsonutils.bloqueArreglo(json, "buses");
            String[] objsBuses = Jsonutils.separarObjetos(bloqueBuses);
            for (String obj : objsBuses) {
                int id = Integer.parseInt(Jsonutils.valorNumerico(obj, "id").trim());
                String tipoStr = Jsonutils.valorTexto(obj, "tipo");
                if (id > 0 && tipoStr != null && !tipoStr.isEmpty()) {
                    buses.insertar(new NodoBus(id, tipoStr.charAt(0)));
                }
            }

            String bloqueUsers = Jsonutils.bloqueArreglo(json, "usuarios");
            String[] objsUsers = Jsonutils.separarObjetos(bloqueUsers);
            for (String obj : objsUsers) {
                String nom = Jsonutils.valorTexto(obj, "nombre");
                String usr = Jsonutils.valorTexto(obj, "usuario");
                String clv = Jsonutils.valorTexto(obj, "clave");
                if (nom != null && usr != null && clv != null) {
                    usuarios.insertar(new NodoUsuario(nom, usr, clv));
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void guardarEnJSON() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"terminal\": \"").append(Jsonutils.escapar(nombreTerminal)).append("\",\n");
        sb.append("  \"buses\": [\n");

        NodoBus b = buses.getFrente();
        while (b != null) {
            sb.append("    {\"id\": ").append(b.getIdBus())
                    .append(", \"tipo\": \"").append(b.getTipoBus()).append("\"}");
            b = b.getSiguiente();
            if (b != null) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ],\n");
        sb.append("  \"usuarios\": [\n");

        NodoUsuario u = usuarios.getFrente();
        while (u != null) {
            sb.append("    {\"nombre\": \"").append(Jsonutils.escapar(u.getNombre())).append("\"")
                    .append(", \"usuario\": \"").append(Jsonutils.escapar(u.getUsuario())).append("\"")
                    .append(", \"clave\": \"").append(Jsonutils.escapar(u.getClave())).append("\"}");
            u = u.getSiguiente();
            if (u != null) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ]\n}");

        try (Writer w = new OutputStreamWriter(new FileOutputStream(ARCHIVO), StandardCharsets.UTF_8)) {
            w.write(sb.toString());
        }
    }
}
