/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busnovaterch.busnovatech;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author XPC
 */
public class GestionTiquetes {

    private static final String ARCHIVO = "tiquetes.json";
    private static final String ARCHIVO_ATENDIDOS = "atendidos.json";
    private static final String ARCHIVO_COLAS = "colas.json";
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String describirTipo(char tipo) {
        char t = Character.toUpperCase(tipo);
        if (t == 'P') {
            return "Preferencial";
        }
        if (t == 'D') {
            return "Directo";
        }
        return "Normal";
    }

    public static NodoBus elegirBus(ListaBuses buses, char tipo) {
        char tipoUpper = Character.toUpperCase(tipo);
        if (tipoUpper == 'P' || tipoUpper == 'D') {
            return buses.buscarPorTipo(tipoUpper);
        } else {
            return buses.buscarBusNormalConMenosClientes();
        }
    }

    public static String registrarTiquete(ListaBuses buses, String terminal, String nombre, String id, int edad, String moneda, char tipoBus) throws Exception {
        NodoBus busDestino = elegirBus(buses, tipoBus);
        if (busDestino == null) {
            throw new Exception("No hay un bus disponible para el tipo seleccionado.");
        }

        NodoTiquete tiquete = new NodoTiquete(nombre, id, edad, moneda, LocalTime.now().format(FORMATO_HORA), tipoBus);
        tiquete.setTerminalCompra(terminal);

        busDestino.getFilaClientes().insertarPorPrioridad(tiquete);
        guardarTiquetes(buses);
        guardarColas(buses);

        return "Tiquete creado para " + nombre + " (ID: " + id + "). Asignado al bus #" + busDestino.getIdBus() + " (" + describirTipo(busDestino.getTipoBus()) + ").";
    }

    public static double calcularMontoUSD(String servicio, double libras) {
        if ("CARGA".equalsIgnoreCase(servicio)) {
            return libras * 1.5;
        } else if ("VIP".equalsIgnoreCase(servicio)) {
            return 25.0;
        } else if ("EJECUTIVO".equalsIgnoreCase(servicio)) {
            return 15.0;
        } else {
            return 10.0;
        }
    }

    public static String[] obtenerDatosParaCobro(NodoBus bus, String servicio, double libras) throws Exception {
        NodoTiquete cliente = bus.getFilaClientes().getFrente();
        if (cliente == null) {
            throw new Exception("No hay tiquetes en espera para este bus.");
        }

        double montoUSD = calcularMontoUSD(servicio, libras);
        double montoFinal = montoUSD;
        String monedaCliente = cliente.getMonedaCuenta();

        if ("Colones".equalsIgnoreCase(monedaCliente) || "CRC".equalsIgnoreCase(monedaCliente)) {
            double tipoCambio = Bancocentral.obtenerTipoCambioVentaDolar();
            montoFinal = montoUSD * tipoCambio;
        }

        return new String[]{
            cliente.getNombre(),
            String.format("%.2f", montoFinal),
            monedaCliente,
            String.format("%.2f", montoUSD)
        };
    }

    public static String confirmarAbordaje(NodoBus bus, ListaBuses buses, String terminalActual, String servicio, double libras, boolean aceptaPagar) throws Exception {
        if (bus.isInspectorOcupado()) {
            throw new Exception("El inspector de este bus ya está atendiendo a alguien.");
        }

        bus.setInspectorOcupado(true);
        NodoTiquete cliente = bus.getFilaClientes().desencolar();

        if (cliente == null) {
            bus.setInspectorOcupado(false);
            throw new Exception("No hay tiquetes en espera para este bus.");
        }

        if (!aceptaPagar) {
            bus.getFilaClientes().insertarPorPrioridad(cliente);
            bus.setInspectorOcupado(false);
            return "El cliente " + cliente.getNombre() + " no aceptó pagar y fue reinsertado en la fila.";
        }

        double montoUSD = calcularMontoUSD(servicio, libras);
        cliente.setServicio(servicio);
        cliente.setLibras(libras);
        cliente.setMontoCobrado(montoUSD);
        cliente.setEstado("ATENDIDO");
        cliente.setHoraAbordaje(LocalTime.now().format(FORMATO_HORA));

        bus.incrementarPasajerosAbordo();
        guardarAtendido(cliente, bus, terminalActual);
        guardarColas(buses);
        guardarTiquetes(buses);

        bus.setInspectorOcupado(false);

        return "El cliente " + cliente.getNombre() + " abordó el bus #" + bus.getIdBus() + " a las " + cliente.getHoraAbordaje() + ".\nPasajeros a bordo de este bus: " + bus.getCantidadPasajerosAbordo();
    }

    public static String obtenerEstadoColasTexto(ListaBuses buses) {
        StringBuilder sb = new StringBuilder();
        sb.append("Estado actual de las colas:\n\n");

        NodoBus actual = buses.getFrente();
        while (actual != null) {
            sb.append("Bus #").append(actual.getIdBus())
                    .append(" (").append(describirTipo(actual.getTipoBus())).append("): ")
                    .append(actual.getFilaClientes().getTamaño()).append(" persona(s) en fila");

            if (actual.isInspectorOcupado()) {
                sb.append(" — inspector ocupado\n");
            } else {
                sb.append(" — inspector libre\n");
            }
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }

    public static String obtenerAtendidosTexto() {
        File archivo = new File(ARCHIVO_ATENDIDOS);
        if (!archivo.exists()) {
            return "Todavía no hay clientes atendidos.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Historial de Atendidos:\n\n");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
        } catch (Exception e) {
            return "Error al leer " + ARCHIVO_ATENDIDOS;
        }

        return sb.toString();
    }

    public static void recalcularPasajerosAbordo(ListaBuses buses) {
        File archivo = new File(ARCHIVO_ATENDIDOS);
        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            String json = sb.toString();
            String bloque = Jsonutils.bloqueArreglo(json, "atendidos");
            String[] objs = Jsonutils.separarObjetos(bloque);

            NodoBus b = buses.getFrente();
            while (b != null) {
                b.setCantidadPasajerosAbordo(0);
                b = b.getSiguiente();
            }

            for (String obj : objs) {
                int idBus = Integer.parseInt(Jsonutils.valorNumerico(obj, "idBus").trim());
                NodoBus busEncontrado = buses.buscarPorId(idBus);
                if (busEncontrado != null) {
                    busEncontrado.incrementarPasajerosAbordo();
                }
            }
        } catch (Exception e) {
        }
    }

    public static void guardarAtendido(NodoTiquete tiquete, NodoBus bus, String terminal) {
        File archivo = new File(ARCHIVO_ATENDIDOS);
        StringBuilder sb = new StringBuilder();

        sb.append("{\n  \"atendidos\": [\n");
        boolean primero = true;

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
                StringBuilder existente = new StringBuilder();
                String linea;
                while ((linea = br.readLine()) != null) {
                    existente.append(linea);
                }
                String json = existente.toString();
                String bloque = Jsonutils.bloqueArreglo(json, "atendidos");
                String[] objs = Jsonutils.separarObjetos(bloque);

                for (String obj : objs) {
                    if (!obj.trim().isEmpty()) {
                        if (!primero) {
                            sb.append(",\n");
                        }
                        sb.append("    ").append(obj.trim());
                        primero = false;
                    }
                }
            } catch (Exception e) {
            }
        }

        if (!primero) {
            sb.append(",\n");
        }
        sb.append("    {\"nombre\": \"").append(Jsonutils.escapar(tiquete.getNombre()))
                .append("\", \"id\": \"").append(Jsonutils.escapar(tiquete.getId()))
                .append("\", \"edad\": ").append(tiquete.getEdad())
                .append(", \"monedaCuenta\": \"").append(tiquete.getMonedaCuenta())
                .append("\", \"horaCompra\": \"").append(tiquete.getHoraCompra())
                .append("\", \"horaAbordaje\": \"").append(tiquete.getHoraAbordaje())
                .append("\", \"servicio\": \"").append(tiquete.getService())
                .append("\", \"libras\": ").append(tiquete.getLibras())
                .append(", \"montoCobradoUSD\": ").append(tiquete.getMontoCobrado())
                .append(", \"tipoBus\": \"").append(tiquete.getTipoBus())
                .append("\", \"idBus\": ").append(bus.getIdBus())
                .append(", \"terminal\": \"").append(Jsonutils.escapar(terminal)).append("\"}");

        sb.append("\n  ]\n}");

        try (Writer w = new OutputStreamWriter(new FileOutputStream(ARCHIVO_ATENDIDOS), StandardCharsets.UTF_8)) {
            w.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guardarTiquetes(ListaBuses buses) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"tiquetes\": [\n");
        boolean primero = true;

        NodoBus bus = buses.getFrente();
        while (bus != null) {
            NodoTiquete t = bus.getFilaClientes().getFrente();
            while (t != null) {
                if (!primero) {
                    sb.append(",\n");
                }
                sb.append("    {\"nombre\": \"").append(Jsonutils.escapar(t.getNombre()))
                        .append("\", \"tipoBus\": \"").append(t.getTipoBus()).append("\"}");
                primero = false;
                t = t.getSiguiente();
            }
            bus = bus.getSiguiente();
        }
        sb.append("\n  ]\n}");

        try (Writer w = new OutputStreamWriter(new FileOutputStream(ARCHIVO), StandardCharsets.UTF_8)) {
            w.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guardarColas(ListaBuses buses) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"colas\": [\n");
        NodoBus bus = buses.getFrente();
        while (bus != null) {
            sb.append("    {\"idBus\": ").append(bus.getIdBus())
                    .append(", \"cantidad\": ").append(bus.getFilaClientes().getTamaño())
                    .append(", \"tiquetes\": [\n");

            NodoTiquete t = bus.getFilaClientes().getFrente();
            while (t != null) {
                sb.append("        {\"nombre\": \"").append(Jsonutils.escapar(t.getNombre())).append("\"}");
                t = t.getSiguiente();
                if (t != null) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append("    ]}");
            bus = bus.getSiguiente();
            if (bus != null) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("\n  ]\n}");

        try (Writer w = new OutputStreamWriter(new FileOutputStream(ARCHIVO_COLAS), StandardCharsets.UTF_8)) {
            w.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
