package com.AEV2ADD.controlador;

import com.AEV2ADD.modelo.Modelo;
import com.AEV2ADD.vista.VistaLogin;
import com.AEV2ADD.vista.VistaMainApp;
import com.AEV2ADD.vista.VistaRegistro;
import com.AEV2ADD.vista.VistaExportarCSV;

import javax.swing.*;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlador {
    private Modelo modelo;
    private VistaLogin vistaLogin;
    private VistaMainApp vistaMainApp;
    private VistaRegistro vistaRegistro;
    private VistaExportarCSV vistaExportarCSV;
    private String tipoUsuario;

    /**
     * Constructor que recibe instancias de la vista y del modelo.
     * Inicia la configuración de los event handlers y muestra la ventana de login.
     *
     * @param modelo           La instancia del modelo
     * @param vistaLogin       La instancia de la vista de login
     * @param vistaMainApp     La instancia de la vista principal
     * @param vistaRegistro    La instancia de la vista de registro
     * @param vistaExportarCSV La instancia de la vista de exportación a CSV
     */
    public Controlador(Modelo modelo, VistaLogin vistaLogin, VistaMainApp vistaMainApp, VistaRegistro vistaRegistro,
                       VistaExportarCSV vistaExportarCSV) {
        this.modelo = modelo;
        this.vistaLogin = vistaLogin;
        this.vistaMainApp = vistaMainApp;
        this.vistaRegistro = vistaRegistro;
        this.vistaExportarCSV = vistaExportarCSV;

        initEventHandlers(); // Configura todos los listeners
        iniciarLogin();      // Muestra la vista de login al iniciar el programa
    }

    /**
     * Método para añadir todos los listeners de los componentes de las vistas.
     */
    private void initEventHandlers() {
        // Listener para la vista de Login
        vistaLogin.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        // Listeners para la vista principal (MainApp)
        vistaMainApp.addRegistrarUsuarioListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        vistaMainApp.addLimpiarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarConsola();
            }
        });

        vistaMainApp.addConsultarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultar();
            }
        });

        vistaMainApp.addExportarBusquedaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vistaExportarCSV.setVisible(true);
            }
        });

        vistaMainApp.addMostrarXmlListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarXml();
            }
        });

        vistaMainApp.addCerrarSesionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });

        // Listener para la vista de Registro de Usuario
        vistaRegistro.addRegistrarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarNuevoUsuario();
            }
        });

        // Listener para la vista de Exportar CSV
        vistaExportarCSV.addAceptarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarCSV();
            }
        });
    }

    /**
     * Muestra la ventana de login al iniciar el programa.
     */
    private void iniciarLogin() {
        vistaLogin.setVisible(true);  // Muestra la vista de login al iniciar el programa
    }

    /**
     * Maneja el inicio de sesión al hacer clic en el botón de login.
     */
    private void iniciarSesion() {
        String username = vistaLogin.getUsername();
        String password = vistaLogin.getPassword();

        try {
            if (modelo.conectar(username, password)) {
                tipoUsuario = modelo.obtenerTipoUsuario();
                if ("admin".equals(tipoUsuario)) {
                    String rutaArchivoCSV = "csv/AE02_population.csv";
                    modelo.iniciarImportacionCSV(rutaArchivoCSV);
                    JOptionPane.showMessageDialog(vistaLogin, "Importación del archivo CSV completada con éxito.");
                }

                vistaLogin.setVisible(false);
                vistaMainApp.limpiar();
                vistaMainApp.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(vistaLogin, "Usuario o contraseña incorrectos.");
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(vistaLogin, "Error inesperado: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja la acción de registrar un usuario.
     */
    private void registrarUsuario() {
        if ("admin".equals(tipoUsuario)) {
            iniciarRegistro();
        } else {
            JOptionPane.showMessageDialog(vistaMainApp, "No se puede utilizar esta función porque no es admin.",
                    "Acceso denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Limpia el área de consola en la vista principal.
     */
    private void limpiarConsola() {
        vistaMainApp.limpiar();
    }

    /**
     * Ejecuta una consulta y muestra el resultado en el área de consola de la vista principal.
     */
    private void consultar() {
        String query = vistaMainApp.getQuery();
        String resultado = modelo.ejecutarConsulta(query, tipoUsuario);
        vistaMainApp.setConsoleText(resultado);
    }

    /**
     * Muestra el contenido XML en el área de consola de la vista principal.
     */
    private void mostrarXml() {
        String contenidoXML = modelo.obtenerContenidoXMLConcatenado();
        vistaMainApp.setConsoleText(contenidoXML);
    }

    /**
     * Cierra la sesión actual y regresa a la vista de login.
     */
    private void cerrarSesion() {
        modelo.desconectar();
        vistaMainApp.setVisible(false);
        vistaMainApp.limpiarBuscador();
        vistaLogin.setVisible(true);
        vistaLogin.limpiarCampos();
    }

    /**
     * Muestra la vista de registro de usuario.
     */
    private void iniciarRegistro() {
        vistaRegistro.setVisible(true);
    }

    /**
     * Maneja el registro de un nuevo usuario desde la vista de registro.
     */
    private void registrarNuevoUsuario() {
        String username = vistaRegistro.getNewUsername();
        String password = vistaRegistro.getNewPassword();
        String confirmPassword = vistaRegistro.getConfirmPassword();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(vistaRegistro, "Por favor, complete todos los campos.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(vistaRegistro, "Las contraseñas no coinciden. Intente nuevamente.");
            return;
        }

        if (modelo.registrarUsuario(username, password)) {
            JOptionPane.showMessageDialog(vistaRegistro, "Usuario registrado exitosamente.");
            vistaRegistro.limpiarCamposRegistro();
        } else {
            JOptionPane.showMessageDialog(vistaRegistro, "Error al registrar el usuario.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exporta los datos de la consola a un archivo CSV.
     */
    private void exportarCSV() {
        String nombreArchivo = vistaExportarCSV.getNombreArchivoCSV();
        if (nombreArchivo.isEmpty()) {
            JOptionPane.showMessageDialog(vistaExportarCSV, "Por favor, ingrese un nombre de archivo.");
            return;
        }

        String contenido = vistaMainApp.getConsolaText();
        modelo.exportarVistaCSV(contenido, nombreArchivo);

        JOptionPane.showMessageDialog(vistaExportarCSV, "Exportación a CSV completada exitosamente.");
        vistaExportarCSV.setVisible(false);
    }
}
