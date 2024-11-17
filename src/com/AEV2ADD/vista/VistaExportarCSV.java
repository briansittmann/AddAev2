package com.AEV2ADD.vista;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

public class VistaExportarCSV extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNombreCSV;
	private JButton btnAceptar;


	public VistaExportarCSV() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 452, 227);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Seleccione el nombre de su archivo .CSV");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(71, 24, 303, 40);
		contentPane.add(lblNewLabel);
		
		txtNombreCSV = new JTextField();
		txtNombreCSV.setToolTipText("No hace falta escribir el .CSV");
		txtNombreCSV.setBounds(52, 70, 340, 40);
		contentPane.add(txtNombreCSV);
		txtNombreCSV.setColumns(10);
		
		btnAceptar = new JButton("Aceptar"); // Cambiado aquí
		btnAceptar.setBounds(141, 122, 141, 40);
		contentPane.add(btnAceptar);
	}
	
    // Método para obtener el nombre del archivo CSV
    public String getNombreArchivoCSV() {
        return txtNombreCSV.getText();
    }

    // Método para agregar un ActionListener al botón Aceptar
    public void addAceptarListener(ActionListener listener) {
        btnAceptar.addActionListener(listener);
    }
}
