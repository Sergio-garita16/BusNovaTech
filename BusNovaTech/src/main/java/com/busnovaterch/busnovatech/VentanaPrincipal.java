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

    // Paleta de Colores Corporativa
    private final Color COLOR_PRIMARIO = new Color(26, 54, 93);      // Azul Marino
    private final Color COLOR_BOTON = new Color(43, 108, 176);        // Azul Acero
    private final Color COLOR_ACCION = new Color(39, 103, 73);        // Verde Éxito
    private final Color COLOR_FONDO = new Color(247, 250, 252);       // Fondo Claro
    private final Color COLOR_TEXTO = Color.WHITE;

    // Tipografías Escaladas para Pantalla Completa
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
        
        // Iniciar maximizado en pantalla completa
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
        scrollSalida.setPreferredSize(new Dimension(800, 230));
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
            StringBuilder sb = new StringBuilder("BUSES REGISTRADOS EN LA TERMINAL:\n\n");
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
                JOptionPane.showMessageDialog(this, "Error al registrar tiquete: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(this, "Primero debe crear al menos un bus en la pestaña 'Gestión de Buses'.", "Sin Buses", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                DefaultComboBoxModel<String> modelBuses = new DefaultComboBoxModel<>();
                NodoBus actual = busFrente;
                boolean hayGenteEnColas = false;

                while (actual != null) {
                    int tamanoFila = actual.getFilaClientes().getTamaño();
                    modelBuses.addElement("Bus #" + actual.getIdBus() + " (" + GestionTiquetes.describirTipo(actual.getTipoBus()) + ") - Pasajeros en fila: " + tamanoFila);
                    if (tamanoFila > 0) hayGenteEnColas = true;
                    actual = actual.getSiguiente();
                }

                if (!hayGenteEnColas) {
                    JOptionPane.showMessageDialog(this, "No hay personas esperando en la fila de ningún bus.", "Colas Vacías", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                JComboBox<String> cbBusesDisponibles = new JComboBox<>(modelBuses);
                cbBusesDisponibles.setFont(FONT_CAMPO);

                int opcion = JOptionPane.showConfirmDialog(this, cbBusesDisponibles, "Seleccione el Bus para Abordar", JOptionPane.OK_CANCEL_OPTION);

                if (opcion == JOptionPane.OK_OPTION) {
                    String seleccion = (String) cbBusesDisponibles.getSelectedItem();
                    if (seleccion == null) return;

                    // Extracción ultra segura del ID del Bus
                    String soloPrimerosDigitos = seleccion.split("-")[0].replaceAll("[^0-9]", "").trim();
                    if (soloPrimerosDigitos.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "No se pudo identificar el ID del bus seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int idBus = Integer.parseInt(soloPrimerosDigitos);
                    NodoBus bus = config.getBuses().buscarPorId(idBus);

                    if (bus == null || bus.getFilaClientes().getTamaño() == 0) {
                        JOptionPane.showMessageDialog(this, "El bus seleccionado no tiene clientes en fila.", "Cola Vacía", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String[] opcionesServicio = {"Regular", "VIP", "Ejecutivo", "Carga"};
                    String serv = (String) JOptionPane.showInputDialog(
                        this, "Seleccione el servicio a cobrar:", "Servicio del Cliente",
                        JOptionPane.QUESTION_MESSAGE, null, opcionesServicio, opcionesServicio[0]
                    );

                    if (serv == null) return; // Presionó cancelar

                    double libras = 0;
                    if ("Carga".equalsIgnoreCase(serv)) {
                        String lbsStr = JOptionPane.showInputDialog(this, "Ingrese el peso en libras (lbs):");
                        if (lbsStr == null || lbsStr.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Debe ingresar un peso en libras para el servicio de carga.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        try {
                            libras = Double.parseDouble(lbsStr.trim());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "El valor ingresado para libras debe ser numérico.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    String res = GestionTiquetes.confirmarAbordaje(bus, config.getBuses(), config.getNombreTerminal(), serv, libras, true);
                    txtAreaSalida.setText(res);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al procesar el abordaje: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            String loc = txtLocalidad.getText().trim();
            if (!loc.isEmpty()) {
                grafo.agregarLocalidadInterno(loc);
                txtAreaSalida.setText("Localidad '" + loc + "' agregada con éxito.");
                txtLocalidad.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Escriba el nombre de la localidad.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnAgregarRutaAccion.addActionListener(e -> {
            String orig = txtOrigen.getText().trim();
            String dest = txtDestino.getText().trim();
            String pesoStr = txtPeso.getText().trim();

            if (orig.isEmpty() || dest.isEmpty() || pesoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar los tres campos: Origen, Destino y Distancia (Peso).", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double peso = Double.parseDouble(pesoStr);
                if (peso <= 0) {
                    JOptionPane.showMessageDialog(this, "La distancia debe ser mayor a 0.", "Valor Inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                grafo.agregarRutaInterno(orig, dest, peso);
                txtAreaSalida.setText("Ruta conectada: " + orig + " -> " + dest + " (" + peso + " km)");
                txtOrigen.setText(""); txtDestino.setText(""); txtPeso.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El peso/distancia debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar la ruta: " + ex.getMessage(), "Error en Grafo", JOptionPane.ERROR_MESSAGE);
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
                txtAreaSalida.setText("Error al consultar el servicio web del BCCR: " + ex.getMessage());
            }
        });

        return panel;
    }

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
        btn.setPreferredSize(new Dimension(280, 50));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return btn;
    }

    private void limpiarCamposTiquete() {
        txtNombre.setText("");
        txtId.setText("");
        txtEdad.setText("");
    }
}