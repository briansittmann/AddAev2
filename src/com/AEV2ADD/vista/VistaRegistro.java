package com.AEV2ADD.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionListener;

public class VistaRegistro extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textNewUser;
    private JPasswordField newPassword1;
    private JPasswordField newPassword2;
    private JButton btnRegistrar;
    private JLabel lblContrasea;

    public VistaRegistro() {
        setTitle("Registrar Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 453, 349);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Registrar Usuario");
        lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(117, 20, 205, 31);
        contentPane.add(lblNewLabel);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        lblUsuario.setBounds(21, 82, 85, 31);
        contentPane.add(lblUsuario);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        lblContraseña.setBounds(21, 137, 120, 31);
        contentPane.add(lblContraseña);

        JLabel lblRepetirContraseña = new JLabel("Repetir");
        lblRepetirContraseña.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        lblRepetirContraseña.setBounds(21, 180, 120, 31);
        contentPane.add(lblRepetirContraseña);
        
        lblContrasea = new JLabel("Contraseña:");
        lblContrasea.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        lblContrasea.setBounds(21, 201, 120, 31);
        contentPane.add(lblContrasea);

        textNewUser = new JTextField();
        textNewUser.setBounds(117, 82, 235, 31);
        contentPane.add(textNewUser);
        textNewUser.setColumns(10);

        newPassword1 = new JPasswordField();
        newPassword1.setBounds(117, 137, 235, 31);
        contentPane.add(newPassword1);

        newPassword2 = new JPasswordField();
        newPassword2.setBounds(117, 191, 235, 31);
        contentPane.add(newPassword2);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(148, 234, 152, 42);
        contentPane.add(btnRegistrar);
    }

    public String getNewUsername() {
        return textNewUser.getText();
    }

    public String getNewPassword() {
        return new String(newPassword1.getPassword());
    }

    public String getConfirmPassword() {
        return new String(newPassword2.getPassword());
    }

    public void addRegistrarListener(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
    }
    
    public void limpiarCamposRegistro() {
        textNewUser.setText("");
        newPassword1.setText("");
        newPassword2.setText("");
    }

}
