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
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private Config config;
    private Grafo grafo;

    // Paleta de Colores de la Empresa de Transportes
    private final Color COLOR_PRIMARIO = new Color(26, 54, 93);      // Azul Marino Corporativo
    private final Color COLOR_BOTON = new Color(43, 108, 176);        // Azul Acero
    private final Color COLOR_ACCION = new Color(39, 103, 73);        // Verde Éxito/Registro
    private final Color COLOR_FONDO = new Color(247, 250, 252);       // Gris Claro/Blanco
    private final Color COLOR_TEXTO = Color.WHITE;

    // Fuentes para Pantalla Completa
    private final Font FONT_TITULO = new Font("SansSerif", Font.BOLD, 18);
    private final Font FONT_ETIQUETA = new Font("SansSerif", Font.BOLD, 16);
    private final Font FONT_CAMPO = new Font("SansSerif", Font.PLAIN, 16);
    private final Font FONT_BOTON = new Font("SansSerif", Font.BOLD, 16);

    private JTextArea txtAreaSalida;
    private JTextField txtNombre, txtId, txtEdad, txtMoneda;
    private JComboBox<String> cbTipoBus;
    
    private JTextField txtIdNuevoBus;
    private JComboBox<String> cbTipoNuevoBus;

    private JTextField txtLocalidad, txtOrigen, txtDestino, txtPeso;

    public VentanaPrincipal(Config config, Grafo grafo) {
        this.config = config;
        this.grafo = grafo;

        setTitle("BusNovaTech - Sistema de Gestión de Transportes | Terminal: " + config.getNombreTerminal());
        
        // Configurar la ventana para abrir en Pantalla Completa Maximizada
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponentes();
    }

    private void initComponentes() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_TITULO);

        tabbedPane.addTab("  Gestión de Buses  ", crearPanelBuses());
        tabbedPane.addTab("  Creación de Tiquetes  ", crearPanelTiquetes());
        tabbedPane.addTab("  Atención y Colas  ", crearPanelAtencion());
        tabbedPane.addTab("  Rutas y Grafo  ", crearPanelGrafo());
        tabbedPane.addTab("  Consulta BCCR  ", crearPanelBCCR());

        txtAreaSalida = new JTextArea();
        txtAreaSalida.setEditable(false);
        txtAreaSalida.setFont(new Font("Monospaced", Font.PLAIN, 16));
        txtAreaSalida.setBackground(new Color(237, 242, 247));
        txtAreaSalida.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollSalida = new JScrollPane(txtAreaSalida);
        scrollSalida.setPreferredSize(new Dimension(800, 220));
        scrollSalida.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARIO, 2), 
            "Consola de Salida / Historial", 
            0, 0, FONT_TITULO, COLOR_PRIMARIO
        ));

        setLayout(new BorderLayout(15, 15));
        add(tabbedPane, BorderLayout.CENTER);
        add(scrollSalida, BorderLayout.SOUTH);
    }

    private JPanel crearPanelBuses() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdNuevoBus = new JTextField();
        txtIdNuevoBus.setFont(FONT_CAMPO);
        txtIdNuevoBus.setPreferredSize(new Dimension(300, 40));

        cbTipoNuevoBus = new JComboBox<>(new String[]{"P - Preferencial", "D - Directo", "N - Normal"});
        cbTipoNuevoBus.setFont(FONT_CAMPO);
        cbTipoNuevoBus.setPreferredSize(new Dimension(300, 40));

        JButton btnCrearBus = crearBotonEstilizado("Crear Bus", COLOR_ACCION);
        JButton btnVerBuses = crearBotonEstilizado("Ver Buses Registrados", COLOR_BOTON);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(crearEtiqueta("Número / ID del Bus:"), gbc);
        gbc.gridx = 1;
        panel.add(txtIdNuevoBus, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(crearEtiqueta("Tipo de Bus:"), gbc);
        gbc.gridx = 1;
        panel.add(cbTipoNuevoBus, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(btnCrearBus, gbc);
        gbc.gridx = 1;
        panel.add(btnVerBuses, gbc);

        btnCrearBus.addActionListener(e -> {
            try {
                int idBus = Integer.parseInt(txtIdNuevoBus.getText().trim());
                char tipoBus = cbTipoNuevoBus.getSelectedItem().toString().charAt(0);

                if (config.getBuses().buscarPorId(idBus) != null) {
                    JOptionPane.showMessageDialog(this, "Ya existe un bus registrado con el ID " + idBus, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                config.getBuses().insertar(new NodoBus(idBus, tipoBus));
                config.guardarEnJSON();

                txtAreaSalida.setText("Bus #" + idBus + " (" + GestionTiquetes.describirTipo(tipoBus) + ") creado exitosamente.");
                txtIdNuevoBus.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número entero válido para el ID del bus.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el bus: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVerBuses.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Buses Registrados en la Terminal:\n\n");
            NodoBus actual = config.getBuses().getFrente();
            if (actual == null) {
                sb.append("No hay buses registrados en la terminal.");
            } else {
                while (actual != null) {
                    sb.append("• Bus #").append(actual.getIdBus())
                      .append(" | Tipo: ").append(GestionTiquetes.describirTipo(actual.getTipoBus()))
                      .append(" | Personas en Fila: ").append(actual.getFilaClientes().getTamaño())
                      .append("\n");
                    actual = actual.getSiguiente();
                }
            }
            txtAreaSalida.setText(sb.toString());
        });

        return panel;
    }

    private JPanel crearPanelTiquetes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(); txtNombre.setFont(FONT_CAMPO); txtNombre.setPreferredSize(new Dimension(300, 40));
        txtId = new JTextField(); txtId.setFont(FONT_CAMPO); txtId.setPreferredSize(new Dimension(300, 40));
        txtEdad = new JTextField(); txtEdad.setFont(FONT_CAMPO); txtEdad.setPreferredSize(new Dimension(300, 40));
        txtMoneda = new JTextField("Colones"); txtMoneda.setFont(FONT_CAMPO); txtMoneda.setPreferredSize(new Dimension(300, 40));
        
        cbTipoBus = new JComboBox<>(new String[]{"P - Preferencial", "D - Directo", "N - Normal"});
        cbTipoBus.setFont(FONT_CAMPO); cbTipoBus.setPreferredSize(new Dimension(300, 40));

        JButton btnRegistrar = crearBotonEstilizado("Registrar Tiquete", COLOR_ACCION);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(crearEtiqueta("Nombre Completo:"), gbc);
        gbc.gridx = 1; panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(crearEtiqueta("Identificación (ID):"), gbc);
        gbc.gridx = 1; panel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(crearEtiqueta("Edad:"), gbc);
        gbc.gridx = 1; panel.add(txtEdad, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(crearEtiqueta("Moneda Cuenta:"), gbc);
        gbc.gridx = 1; panel.add(txtMoneda, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(crearEtiqueta("Tipo de Bus Solicito:"), gbc);
        gbc.gridx = 1; panel.add(cbTipoBus, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(btnRegistrar, gbc);

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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 50));
        panel.setBackground(COLOR_FONDO);

        JButton btnVerColas = crearBotonEstilizado("Ver Estado de Colas", COLOR_BOTON);
        JButton btnVerAtendidos = crearBotonEstilizado("Ver Historial Atendidos", COLOR_BOTON);
        JButton btnAbordar = crearBotonEstilizado("Abordar Cliente a Bus", COLOR_ACCION);

        panel.add(btnVerColas);
        panel.add(btnVerAtendidos);
        panel.add(btnAbordar);

        btnVerColas.addActionListener(e -> txtAreaSalida.setText(GestionTiquetes.obtenerEstadoColasTexto(config.getBuses())));
        btnVerAtendidos.addActionListener(e -> txtAreaSalida.setText(GestionTiquetes.obtenerAtendidosTexto()));

        btnAbordar.addActionListener(e -> {
            try {
                NodoBus busFrente = config.getBuses().getFrente();
                if (busFrente == null) {
                    JOptionPane.showMessageDialog(this, "Primero debe crear al menos un bus en la pestaña 'Gestión de Buses'.");
                    return;
                }

                DefaultComboBoxModel<String> modelBuses = new DefaultComboBoxModel<>();
                NodoBus actual = busFrente;
                while (actual != null) {
                    modelBuses.addElement("Bus #" + actual.getIdBus() + " (" + GestionTiquetes.describirTipo(actual.getTipoBus()) + ")");
                    actual = actual.getSiguiente();
                }

                JComboBox<String> cbBusesDisponibles = new JComboBox<>(modelBuses);
                cbBusesDisponibles.setFont(FONT_CAMPO);
                int opcion = JOptionPane.showConfirmDialog(this, cbBusesDisponibles, "Seleccione el Bus a abordar", JOptionPane.OK_CANCEL_OPTION);

                if (opcion == JOptionPane.OK_OPTION) {
                    String seleccion = (String) cbBusesDisponibles.getSelectedItem();
                    int idBus = Integer.parseInt(seleccion.split(" ")[0].replace("Bus", "").replace("#", "").trim());

                    NodoBus bus = config.getBuses().buscarPorId(idBus);
                    if (bus != null) {
                        String serv = JOptionPane.showInputDialog(this, "Servicio (Regular, VIP, Ejecutivo, Carga):");
                        if (serv == null || serv.trim().isEmpty()) serv = "Regular";

                        double libras = 0;
                        if ("Carga".equalsIgnoreCase(serv.trim())) {
                            String lbsStr = JOptionPane.showInputDialog(this, "Libras de carga:");
                            if (lbsStr != null && !lbsStr.trim().isEmpty()) {
                                libras = Double.parseDouble(lbsStr.trim());
                            }
                        }

                        String res = GestionTiquetes.confirmarAbordaje(bus, config.getBuses(), config.getNombreTerminal(), serv, libras, true);
                        txtAreaSalida.setText(res);
                    }
                }
            } catch (Exception ex) {
                txtAreaSalida.setText("Error al abordar: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel crearPanelGrafo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtLocalidad = new JTextField(); txtLocalidad.setFont(FONT_CAMPO); txtLocalidad.setPreferredSize(new Dimension(250, 40));
        txtOrigen = new JTextField(); txtOrigen.setFont(FONT_CAMPO); txtOrigen.setPreferredSize(new Dimension(250, 40));
        txtDestino = new JTextField(); txtDestino.setFont(FONT_CAMPO); txtDestino.setPreferredSize(new Dimension(250, 40));
        txtPeso = new JTextField(); txtPeso.setFont(FONT_CAMPO); txtPeso.setPreferredSize(new Dimension(250, 40));

        JButton btnAgregarLocalidad = crearBotonEstilizado("1. Agregar Localidad", COLOR_BOTON);
        JButton btnAgregarRutaAccion = crearBotonEstilizado("2. Agregar Ruta", COLOR_ACCION);
        JButton btnVerGrafo = crearBotonEstilizado("Ver Grafo Completo", COLOR_PRIMARIO);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(crearEtiqueta("Nombre Localidad:"), gbc);
        gbc.gridx = 1; panel.add(txtLocalidad, gbc);
        gbc.gridx = 2; panel.add(btnAgregarLocalidad, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(crearEtiqueta("Localidad Origen:"), gbc);
        gbc.gridx = 1; panel.add(txtOrigen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(crearEtiqueta("Localidad Destino:"), gbc);
        gbc.gridx = 1; panel.add(txtDestino, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(crearEtiqueta("Distancia (km):"), gbc);
        gbc.gridx = 1; panel.add(txtPeso, gbc);
        gbc.gridx = 2; panel.add(btnAgregarRutaAccion, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        panel.add(btnVerGrafo, gbc);

        btnAgregarLocalidad.addActionListener(e -> {
            String loc = txtLocalidad.getText();
            if (loc != null && !loc.trim().isEmpty()) {
                grafo.agregarLocalidadInterno(loc.trim());
                txtAreaSalida.setText("Localidad '" + loc + "' agregada con éxito.");
                txtLocalidad.setText("");
            }
        });

        btnAgregarRutaAccion.addActionListener(e -> {
            try {
                String orig = txtOrigen.getText();
                String dest = txtDestino.getText();
                double peso = Double.parseDouble(txtPeso.getText());

                if (orig != null && dest != null && !orig.isEmpty() && !dest.isEmpty()) {
                    grafo.agregarRutaInterno(orig.trim(), dest.trim(), peso);
                    txtAreaSalida.setText("Ruta conectada: " + orig + " -> " + dest + " (" + peso + " km)");
                    txtOrigen.setText(""); txtDestino.setText(""); txtPeso.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Verifique los datos de origen, destino y peso.");
            }
        });

        btnVerGrafo.addActionListener(e -> txtAreaSalida.setText(grafo.obtenerGrafoTexto()));

        return panel;
    }

    private JPanel crearPanelBCCR() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 50));
        panel.setBackground(COLOR_FONDO);

        JButton btnConsultar = crearBotonEstilizado("Consultar Tipo de Cambio (BCCR)", COLOR_PRIMARIO);
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

    // Auxiliares para diseño consistente de UI
    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONT_ETIQUETA);
        lbl.setForeground(COLOR_PRIMARIO);
        return lbl;
    }

    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOTON);
        btn.setBackground(colorFondo);
        btn.setForeground(COLOR_TEXTO);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(260, 50)); // Botones grandes escalados
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return btn;
    }

    private void limpiarCamposTiquete() {
        txtNombre.setText("");
        txtId.setText("");
        txtEdad.setText("");
    }
}