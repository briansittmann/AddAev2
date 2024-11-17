package com.AEV2ADD;

import com.AEV2ADD.controlador.Controlador;
import com.AEV2ADD.modelo.Modelo;
import com.AEV2ADD.vista.VistaLogin;
import com.AEV2ADD.vista.VistaMainApp;
import com.AEV2ADD.vista.VistaRegistro;
import com.AEV2ADD.vista.VistaExportarCSV;

public class Main {

    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        VistaLogin vistaLogin = new VistaLogin();
        VistaMainApp vistaMainApp = new VistaMainApp();
        VistaRegistro vistaRegistro = new VistaRegistro();
        VistaExportarCSV vistaExportarCSV = new VistaExportarCSV();
        
     // Inicia el controlador con el modelo y las vistas configuradas
        new Controlador(modelo, vistaLogin, vistaMainApp, vistaRegistro, vistaExportarCSV);
    }
}
