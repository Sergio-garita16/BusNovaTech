package com.busnovaterch.busnovatech;

public class Login {

    private ListaUsuarios usuarios;

    public Login(ListaUsuarios usuarios) {
        this.usuarios = usuarios;
    }

    public boolean autenticar(String usuario, String contrasena) {
        if (usuario == null || contrasena == null) {
            return false;
        }

        String usrLimpio = usuario.trim();
        String pwdLimpia = contrasena.trim();

        if (usrLimpio.isEmpty() || pwdLimpia.isEmpty()) {
            return false;
        }

        NodoUsuario encontrado = usuarios.buscar(usrLimpio);
        if (encontrado != null && encontrado.getClave().equals(pwdLimpia)) {
            return true;
        }

        return false;
    }

    public String obtenerMensajeError(int intentosRestantes) {
        if (intentosRestantes > 0) {
            return "Usuario o contraseña incorrectos.\nIntentos restantes: " + intentosRestantes;
        } else {
            return "Demasiados intentos fallidos. Acceso bloqueado.";
        }
    }

    public ListaUsuarios getUsuarios() {
        return usuarios;
    }
}
