/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

/**
 *
 * @author XPC
 */
public class Jsonutils {

/** Extrae el valor de texto (entre comillas) asociado a una clave dentro de un fragmento JSON. */
    public static String valorTexto(String json, String clave) {
        int indiceClave = json.indexOf("\"" + clave + "\"");
        if (indiceClave == -1) return null;
        int indiceInicio = json.indexOf('"', indiceClave + clave.length() + 2);
        if (indiceInicio == -1) return null;
 
        StringBuilder valor = new StringBuilder();
        for (int posicion = indiceInicio + 1; posicion < json.length(); posicion++) {
            char caracterActual = json.charAt(posicion);
            boolean esEscape = caracterActual == '\\' && posicion + 1 < json.length()
                    && (json.charAt(posicion + 1) == '"' || json.charAt(posicion + 1) == '\\');
            if (esEscape) {
                valor.append(json.charAt(++posicion));
            } else if (caracterActual == '"') {
                break;
            } else {
                valor.append(caracterActual);
            }
        }
        return valor.toString();
    }
 
    /** Extrae el valor numérico (entero, sin comillas) asociado a una clave dentro de un fragmento JSON. */
    public static String valorNumerico(String json, String clave) {
        int indiceClave = json.indexOf("\"" + clave + "\"");
        if (indiceClave == -1) return null;
 
        int posicion = indiceClave + clave.length() + 2;
        while (posicion < json.length() && !Character.isDigit(json.charAt(posicion)) && json.charAt(posicion) != '-') {
            posicion++;
        }
        int indiceInicio = posicion;
        while (posicion < json.length() && (Character.isDigit(json.charAt(posicion)) || json.charAt(posicion) == '-')) {
            posicion++;
        }
        return json.substring(indiceInicio, posicion);
    }

    /** Extrae el valor numérico decimal (ej. 12.5) asociado a una clave dentro de un fragmento JSON. */
    public static String valorDecimal(String json, String clave) {
        int indiceClave = json.indexOf("\"" + clave + "\"");
        if (indiceClave == -1) return null;

        int posicion = indiceClave + clave.length() + 2;
        while (posicion < json.length() && !Character.isDigit(json.charAt(posicion)) && json.charAt(posicion) != '-') {
            posicion++;
        }
        int indiceInicio = posicion;
        while (posicion < json.length() && (Character.isDigit(json.charAt(posicion))
                || json.charAt(posicion) == '-' || json.charAt(posicion) == '.')) {
            posicion++;
        }
        return json.substring(indiceInicio, posicion);
    }
 
    /** Extrae el contenido interno (sin corchetes) del arreglo asociado a una clave. */
    public static String bloqueArreglo(String json, String clave) {
        int indiceClave = json.indexOf("\"" + clave + "\"");
        if (indiceClave == -1) return "";
        int indiceInicio = json.indexOf('[', indiceClave);
        int indiceFin = json.indexOf(']', indiceInicio);
        return (indiceInicio == -1 || indiceFin == -1) ? "" : json.substring(indiceInicio + 1, indiceFin);
    }
 
    /** Separa el contenido de un arreglo JSON en sus objetos individuales (sin llaves). */
    public static String[] separarObjetos(String bloque) {
        StringBuilder resultado = new StringBuilder();
        StringBuilder objetoActual = new StringBuilder();
        int profundidad = 0;
        boolean dentroDeTexto = false;
 
        for (int posicion = 0; posicion < bloque.length(); posicion++) {
            char caracterActual = bloque.charAt(posicion);
 
            if (dentroDeTexto) {
                objetoActual.append(caracterActual);
                if (caracterActual == '\\' && posicion + 1 < bloque.length()) {
                    objetoActual.append(bloque.charAt(++posicion));
                } else if (caracterActual == '"') {
                    dentroDeTexto = false;
                }
                continue;
            }
 
            if (caracterActual == '"') {
                dentroDeTexto = true;
                objetoActual.append(caracterActual);
                continue;
            }
            if (caracterActual == '{') {
                profundidad++;
                if (profundidad == 1) objetoActual.setLength(0);
                continue;
            }
            if (caracterActual == '}') {
                profundidad--;
                if (profundidad == 0) {
                    resultado.append(objetoActual).append('\u0001');
                    objetoActual.setLength(0);
                }
                continue;
            }
            if (profundidad >= 1) {
                objetoActual.append(caracterActual);
            }
        }
 
        return resultado.length() == 0 ? new String[0] : resultado.toString().split("\u0001");
    }
 
    /** Escapa comillas y barras invertidas para poder incluir el texto dentro de un valor JSON. */
    public static String escapar(String texto) {
        return texto == null ? "" : texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }    
}
