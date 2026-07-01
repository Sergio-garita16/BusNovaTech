
package com.busnovaterch.busnovatech;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;

public class Login {

    private static final Map<String, String> USUARIOS = new HashMap<>();

    static {
        USUARIOS.put("admin", "admin123");
        USUARIOS.put("juan",  "juan2024");
        USUARIOS.put("maria", "maria456");
    }

    public void iniciar() {
        int intentos = 3;

        while (intentos > 0) {
            String usuario = JOptionPane.showInputDialog(null,
                "Ingrese su usuario:",
                "Inicio de Sesión",
                JOptionPane.PLAIN_MESSAGE);

            if (usuario == null) {
                JOptionPane.showMessageDialog(null, "Inicio de sesión cancelado.");
                return;
            }

            String contraseña = JOptionPane.showInputDialog(null,
                "Ingrese su contraseña:",
                "Inicio de Sesión",
                JOptionPane.PLAIN_MESSAGE);

            if (contraseña == null) {
                JOptionPane.showMessageDialog(null, "Inicio de sesión cancelado.");
                return;
            }

            if (autenticar(usuario.trim(), contraseña.trim())) {
                JOptionPane.showMessageDialog(null,
                    "¡Bienvenido, " + usuario + "!",
                    "Acceso Concedido",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                intentos--;
                if (intentos > 0) {
                    JOptionPane.showMessageDialog(null,
                        "Usuario o contraseña incorrectos.\nIntentos restantes: " + intentos,
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Demasiados intentos fallidos. Acceso bloqueado.",
                        "Bloqueado",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean autenticar(String usuario, String contraseña) {
        String contraseñaCorrecta = USUARIOS.get(usuario);
        return contraseñaCorrecta != null && contraseñaCorrecta.equals(contraseña);
    }

    public static void main(String[] args) {
        new Login().iniciar();
    }
}