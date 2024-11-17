package com.AEV2ADD.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;

public class VistaMainApp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel Consultar;
    private JTextField txtSelect;
    private JTextArea txtConsola;
    private JButton btnRegistrarUsuario;
    private JButton btnExportarBusqueda;
    private JButton btnCerrarSesion;
    private JButton btnMostrarXml;
    private JButton btnConsultar;
    private JButton btnLimpiar;
    private JScrollPane scrollConsola;

    public VistaMainApp() {
        setTitle("Population Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 756, 531);
        Consultar = new JPanel();
        Consultar.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(Consultar);
        Consultar.setLayout(null);

        btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(513, 34, 202, 51);
        Consultar.add(btnConsultar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(515, 108, 202, 51);
        Consultar.add(btnLimpiar);

        btnExportarBusqueda = new JButton("Exportar Busqueda como CSV");
        btnExportarBusqueda.setBounds(513, 328, 202, 51);
        Consultar.add(btnExportarBusqueda);

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(513, 397, 201, 51);
        Consultar.add(btnCerrarSesion);

        btnMostrarXml = new JButton("Mostrar informacion XML");
        btnMostrarXml.setBounds(515, 179, 202, 51);
        Consultar.add(btnMostrarXml);

        btnRegistrarUsuario = new JButton("Registrar Usuario");
        btnRegistrarUsuario.setBounds(514, 253, 202, 51);
        Consultar.add(btnRegistrarUsuario);

        txtSelect = new JTextField();
        txtSelect.setToolTipText("Utiliza esto para realizar busquedas en la base de datos");
        txtSelect.setText("SELECT * FROM POPULATION.population");
        txtSelect.setBounds(33, 41, 457, 36);
        Consultar.add(txtSelect);
        txtSelect.setColumns(10);

        JLabel lblTitle = new JLabel("Population Manager");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(254, 6, 236, 23);
        Consultar.add(lblTitle);

        txtConsola = new JTextArea();
        txtConsola.setEditable(false);

        // Envolver txtConsola en un JScrollPane para habilitar el scroll
        scrollConsola = new JScrollPane(txtConsola);
        scrollConsola.setBounds(33, 91, 457, 369);
        scrollConsola.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollConsola.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Consultar.add(scrollConsola);
    }

    // Método para obtener el texto de la consulta SQL ingresada
    public String getQuery() {
        return txtSelect.getText();
    }

    // Método para establecer el texto en el área de consola
    public void setConsoleText(String text) {
        txtConsola.setText(text);
    }

    // Método para agregar ActionListener al botón Consultar
    public void addConsultarListener(ActionListener listener) {
        btnConsultar.addActionListener(listener);
    }
    
    // Método para obtener el texto de la consulta SQL ingresada
    public String getConsolaText() {
        return txtConsola.getText();
    }

    // Método para agregar ActionListener al botón Mostrar XML
    public void addMostrarXmlListener(ActionListener listener) {
        btnMostrarXml.addActionListener(listener);
    }

    // Método para agregar ActionListener al botón Registrar Usuario
    public void addRegistrarUsuarioListener(ActionListener listener) {
        btnRegistrarUsuario.addActionListener(listener);
    }

    // Método para agregar ActionListener al botón Exportar a CSV
    public void addExportarBusquedaListener(ActionListener listener) {
        btnExportarBusqueda.addActionListener(listener);
    }

    // Método para agregar ActionListener al botón Cerrar Sesión
    public void addCerrarSesionListener(ActionListener listener) {
        btnCerrarSesion.addActionListener(listener);
    }
    
    // Poner Buscador en default
    public void limpiarBuscador() {
        txtSelect.setText("SELECT * FROM POPULATION.population"); // Limpia el área de texto de la consola(JtextArea)
    }
    // Limpiar consola
    public void limpiar() {
        txtConsola.setText(""); // Limpia el área de texto de la consola(JtextArea)
    }

    // Método para agregar ActionListener al botón Limpiar
    public void addLimpiarListener(ActionListener listener) {
        btnLimpiar.addActionListener(listener);
    }
}
