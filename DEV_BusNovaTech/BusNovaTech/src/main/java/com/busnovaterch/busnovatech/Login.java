package com.busnovaterch.busnovatech;

import javax.swing.JOptionPane;

public class Login {
    //Arreglo con los nombres de usuario
    private static final String[] USUARIOS = {"admin", "juan", "maria"};
    //aareglos con las contraseñas correpondientes
    private static final String[] CONTRASEÑAS = {"admin123", "juan2024", "maria456"};
    /**
     * Inicia el proceso de login.
     * Permite un máximo de 3 intentos antes de bloquear el acceso.
     */
   
    public void iniciar() {
        int intentos = 3; // numero de intentos
        // bucle que se repite mientras queden intentos 
        while (intentos > 0) {
            String usuario = JOptionPane.showInputDialog(null, //pide el usuario
                "Ingrese su usuario:",
                "Inicio de Sesión",
                JOptionPane.PLAIN_MESSAGE);

            if (usuario == null) {
                JOptionPane.showMessageDialog(null, "Inicio de sesión cancelado.");
                return;
            }
            // solicita la contraseña
            String contraseña = JOptionPane.showInputDialog(null,
                "Ingrese su contraseña:",
                "Inicio de Sesión",
                JOptionPane.PLAIN_MESSAGE);
            // Si el usuario cancela el diálogo de contraseña, se termina el proceso
            if (contraseña == null) {
                JOptionPane.showMessageDialog(null, "Inicio de sesión cancelado.");
                return;
            }
            // Verifica las credenciales ingresadas 
            if (autenticar(usuario.trim(), contraseña.trim())) {
                //// Si la autenticación es correcta, se muestra mensaje de bienvenida
                JOptionPane.showMessageDialog(null,
                    "¡Bienvenido, " + usuario + "!",
                    "Acceso Concedido",
                    JOptionPane.INFORMATION_MESSAGE);
                return;// Se sale del método porque el login fue exitoso
            } else {
                intentos--;
                if (intentos > 0) { //// Aún quedan intentos disponibles: se informa al usuario
                    JOptionPane.showMessageDialog(null,
                        "Usuario o contraseña incorrectos.\nIntentos restantes: " + intentos,
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                } else {
                     // Se acabaron los intentos: se bloquea el acceso
                    JOptionPane.showMessageDialog(null,
                        "Demasiados intentos fallidos. Acceso bloqueado.",
                        "Bloqueado",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean autenticar(String usuario, String contraseña) {
        // Recorre el arreglo de usuarios buscando una coincidencia
        for (int i = 0; i < USUARIOS.length; i++) {
            if (USUARIOS[i].equals(usuario)) {
                // // Si encuentra el usuario, compara la contraseña correspondiente
                return CONTRASEÑAS[i].equals(contraseña);
            }
        }
        // Si no se encontró el usuario, la autenticación falla
        return false;
    }

    public static void main(String[] args) {
        new Login().iniciar();
    }
}
