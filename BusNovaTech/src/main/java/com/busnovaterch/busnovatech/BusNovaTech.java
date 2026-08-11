package com.busnovaterch.busnovatech;

import javax.swing.SwingUtilities;

public class BusNovaTech {  
  public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Config configuracion = new Config();
            if (configuracion.cargarDesdeJSON()) {
                System.out.println("Configuración cargada correctamente.");
            } else {
                configuracion.setNombreTerminal("Terminal Central BusNovaTech");
            }

            GestionTiquetes.recalcularPasajerosAbordo(configuracion.getBuses());

            Grafo grafo = new Grafo();

            VentanaPrincipal ventana = new VentanaPrincipal(configuracion, grafo);
            ventana.setVisible(true);
        });
    }
}