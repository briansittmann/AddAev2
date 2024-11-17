package com.AEV2ADD.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionListener;

public class VistaLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUser;
    private JPasswordField passwordField;
    private JButton btnIngresar;

    public VistaLogin() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 355, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
        lblNewLabel.setBounds(111, 22, 164, 26);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Usuario:");
        lblNewLabel_1.setBounds(26, 84, 61, 16);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Contraseña:");
        lblNewLabel_2.setBounds(26, 137, 85, 16);
        contentPane.add(lblNewLabel_2);

        txtUser = new JTextField();
        txtUser.setBounds(111, 79, 164, 26);
        contentPane.add(txtUser);
        txtUser.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBounds(111, 132, 164, 26);
        contentPane.add(passwordField);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(131, 170, 130, 40);
        contentPane.add(btnIngresar);
    }
    

    /**
     * Método para limpiar los campos de usuario y contraseña
     */
    public void limpiarCampos() {
        txtUser.setText("");
        passwordField.setText("");
    }
    
    
    /**
     * Método getter para obtener el nombre de usuario 
     * @return nombre del usuario
     */
    public String getUsername() {
        return txtUser.getText();
    }
    
    /**
     * Método getter para obtener la contraseña del usuario
     * @return contraseña del usuario
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    /**
     * Método para agregar un ActionListener al botón Ingresar
     * @param listener
     */
    public void addLoginListener(ActionListener listener) {
        btnIngresar.addActionListener(listener);
    }
}
