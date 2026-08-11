/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Cliente del Web Service de Indicadores Económicos del Banco Central de Costa
 * Rica (BCCR). Se usa para obtener el tipo de cambio de referencia de venta del
 * dólar (indicador 317) y así poder cobrar en colones a los clientes cuya
 * cuenta está en esa moneda.
 *
 * IMPORTANTE: el servicio del BCCR exige un correo y un token de suscriptor
 * gratuitos. Se obtienen registrándose en:
 * https://www.bccr.fi.cr/indicadores-economicos/indicadores-economicos/servicio-web
 * Sustituya CORREO y TOKEN por los datos entregados al registrarse.
 *
 * @author XPC
 */
public class Bancocentral {

    private static final String INDICADOR_VENTA_DOLAR = "317";
    private static final String CORREO = "correo@ejemplo.com";
    private static final String TOKEN = "SU_TOKEN_AQUI";
    private static final String NOMBRE = "BusNovaTech";
    private static final String URL_BASE = "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicos";

    public static double consultarServicio() throws Exception {
        return obtenerTipoCambioVentaDolar();
    }

    public static double obtenerTipoCambioVentaDolar() throws Exception {
        String hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String parametros = "Indicador=" + INDICADOR_VENTA_DOLAR
                + "&FechaInicio=" + hoy
                + "&FechaFinal=" + hoy
                + "&Nombre=" + NOMBRE
                + "&SubNiveles=N"
                + "&CorreoElectronico=" + CORREO
                + "&Token=" + TOKEN;

        URL url = new URL(URL_BASE + "?" + parametros);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        StringBuilder respuesta = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea);
            }
        } finally {
            con.disconnect();
        }

        String xml = respuesta.toString();
        int inicio = xml.indexOf("<NUM_VALOR>");
        int fin = xml.indexOf("</NUM_VALOR>");

        if (inicio == -1 || fin == -1) {
            throw new Exception("El BCCR no devolvió el indicador solicitado.");
        }

        String valor = xml.substring(inicio + "<NUM_VALOR>".length(), fin).trim();
        return Double.parseDouble(valor);
    }
}
