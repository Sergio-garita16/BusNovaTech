/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dani1
 */
package com.busnovaterch.busnovatech;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private Config config;
    private Grafo grafo;

    private JTextArea txtAreaSalida;
    private JTextField txtNombre, txtId, txtEdad, txtMoneda;
    private JComboBox<String> cbTipoBus;
    
    private JTextField txtLocalidad, txtOrigen, txtDestino, txtPeso;

    public VentanaPrincipal(Config config, Grafo grafo) {
        this.config = config;
        this.grafo = grafo;

        setTitle("BusNovaTech - Terminal: " + config.getNombreTerminal());
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponentes();
    }

    private void initComponentes() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Creación de Tiquetes", crearPanelTiquetes());
        tabbedPane.addTab("Atención y Colas", crearPanelAtencion());
        tabbedPane.addTab("Rutas y Grafo", crearPanelGrafo());
        tabbedPane.addTab("Consulta BCCR", crearPanelBCCR());

        txtAreaSalida = new JTextArea();
        txtAreaSalida.setEditable(false);
        JScrollPane scrollSalida = new JScrollPane(txtAreaSalida);
        scrollSalida.setPreferredSize(new Dimension(800, 150));

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(scrollSalida, BorderLayout.SOUTH);
    }

    private JPanel crearPanelTiquetes() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtNombre = new JTextField();
        txtId = new JTextField();
        txtEdad = new JTextField();
        txtMoneda = new JTextField("Colones");
        cbTipoBus = new JComboBox<>(new String[]{"P", "D", "N"});

        JButton btnRegistrar = new JButton("Registrar Tiquete");

        panel.add(new JLabel("Nombre Completo:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Identificación (ID):"));
        panel.add(txtId);
        panel.add(new JLabel("Edad:"));
        panel.add(txtEdad);
        panel.add(new JLabel("Moneda (Colones/Dólares):"));
        panel.add(txtMoneda);
        panel.add(new JLabel("Tipo de Bus (P: Preferencial, D: Directo, N: Normal):"));
        panel.add(cbTipoBus);
        panel.add(new JLabel(""));
        panel.add(btnRegistrar);

        btnRegistrar.addActionListener(e -> {
            try {
                String nom = txtNombre.getText();
                String id = txtId.getText();
                int edad = Integer.parseInt(txtEdad.getText());
                String mon = txtMoneda.getText();
                char tipo = cbTipoBus.getSelectedItem().toString().charAt(0);

                String res = GestionTiquetes.registrarTiquete(config.getBuses(), config.getNombreTerminal(), nom, id, edad, mon, tipo);
                txtAreaSalida.setText(res);
                limpiarCamposTiquete();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel crearPanelAtencion() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton btnVerColas = new JButton("Ver Estado de Colas");
        JButton btnVerAtendidos = new JButton("Ver Historial Atendidos");
        JButton btnAbordar = new JButton("Abordar Cliente");

        panel.add(btnVerColas);
        panel.add(btnVerAtendidos);
        panel.add(btnAbordar);

        btnVerColas.addActionListener(e -> {
            txtAreaSalida.setText(GestionTiquetes.obtenerEstadoColasTexto(config.getBuses()));
        });

        btnVerAtendidos.addActionListener(e -> {
            txtAreaSalida.setText(GestionTiquetes.obtenerAtendidosTexto());
        });

        btnAbordar.addActionListener(e -> {
            try {
                String idBusStr = JOptionPane.showInputDialog(this, "Ingrese el ID del bus a abordar:");
                if (idBusStr != null && !idBusStr.trim().isEmpty()) {
                    int idBus = Integer.parseInt(idBusStr.trim());
                    NodoBus bus = config.getBuses().buscarPorId(idBus);
                    if (bus != null) {
                        String serv = JOptionPane.showInputDialog(this, "Servicio (Regular, VIP, Ejecutivo, Carga):");
                        double libras = 0;
                        if ("Carga".equalsIgnoreCase(serv)) {
                            libras = Double.parseDouble(JOptionPane.showInputDialog(this, "Libras de carga:"));
                        }
                        String res = GestionTiquetes.confirmarAbordaje(bus, config.getBuses(), config.getNombreTerminal(), serv, libras, true);
                        txtAreaSalida.setText(res);
                    } else {
                        txtAreaSalida.setText("Bus no encontrado.");
                    }
                }
            } catch (Exception ex) {
                txtAreaSalida.setText("Error al abordar: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel crearPanelGrafo() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 10, 10));

        txtLocalidad = new JTextField();
        txtOrigen = new JTextField();
        txtDestino = new JTextField();
        txtPeso = new JTextField();

        JButton btnAgregarLocalidad = new JButton("1. Agregar Localidad");
        JButton btnAgregarRuta = new JButton("2. Agregar Ruta");

        panelCampos.add(new JLabel("Nombre Localidad:"));
        panelCampos.add(txtLocalidad);
        panelCampos.add(new JLabel(""));
        panelCampos.add(btnAgregarLocalidad);

        panelCampos.add(new JLabel("Localidad Origen:"));
        panelCampos.add(txtOrigen);
        panelCampos.add(new JLabel("Localidad Destino:"));
        panelCampos.add(txtDestino);
        panelCampos.add(new JLabel("Peso / Distancia (km):"));
        panelCampos.add(txtPeso);

        JPanel panelBotonesAcciones = new JPanel(new FlowLayout());
        JButton btnAgregarRutaAccion = new JButton("Agregar Ruta");
        JButton btnVerGrafo = new JButton("Ver Grafo");

        panelBotonesAcciones.add(btnAgregarRutaAccion);
        panelBotonesAcciones.add(btnVerGrafo);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);
        panelPrincipal.add(panelBotonesAcciones, BorderLayout.SOUTH);

        btnAgregarLocalidad.addActionListener(e -> {
            String loc = txtLocalidad.getText();
            if (loc != null && !loc.trim().isEmpty()) {
                grafo.agregarLocalidadInterno(loc.trim());
                txtAreaSalida.setText("Localidad '" + loc + "' agregada con éxito.");
                txtLocalidad.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre de la localidad.");
            }
        });

        btnAgregarRutaAccion.addActionListener(e -> {
            try {
                String orig = txtOrigen.getText();
                String dest = txtDestino.getText();
                double peso = Double.parseDouble(txtPeso.getText());

                if (orig != null && dest != null && !orig.isEmpty() && !dest.isEmpty()) {
                    grafo.agregarRutaInterno(orig.trim(), dest.trim(), peso);
                    txtAreaSalida.setText("Ruta agregada: " + orig + " -> " + dest + " (" + peso + " km)");
                    txtOrigen.setText("");
                    txtDestino.setText("");
                    txtPeso.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Verifique los campos de origen, destino y peso.");
            }
        });

        btnVerGrafo.addActionListener(e -> {
            txtAreaSalida.setText(grafo.obtenerGrafoTexto());
        });

        return panelPrincipal;
    }

    private JPanel crearPanelBCCR() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton btnConsultar = new JButton("Consultar Tipo Cambio BCCR");
        panel.add(btnConsultar);

        btnConsultar.addActionListener(e -> {
            try {
                double tc = Bancocentral.obtenerTipoCambioVentaDolar();
                txtAreaSalida.setText("Tipo de cambio del Dólar (BCCR): ₡" + tc);
            } catch (Exception ex) {
                txtAreaSalida.setText("Error al consultar BCCR: " + ex.getMessage());
            }
        });

        return panel;
    }

    private void limpiarCamposTiquete() {
        txtNombre.setText("");
        txtId.setText("");
        txtEdad.setText("");
    }
}