package com.AEV2ADD.modelo;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase GestorArchivo que gestiona las operaciones de lectura y escritura de archivos
 * CSV y su posterior almacenamiento en una base de datos. También permite la generación
 * de archivos XML mediante la integración con la clase GestorXML.
 */
public class GestorArchivo {

    private Connection conexion;
    private GestorXML gestorXML;
    
    /**
     * Constructor que inicializa el gestor de archivos con una conexión a la base de datos.
     *
     * @param conexion Conexión activa a la base de datos.
     */
    public GestorArchivo(Connection conexion) {
        this.conexion = conexion;
        this.gestorXML = new GestorXML(); 
    }
    
    /**
     * Importa datos desde un archivo CSV, almacenándolos en la base de datos y 
     * generando registros XML para cada entrada.
     *
     * @param rutaArchivo Ruta del archivo CSV que se desea importar.
     */
    public void importarCSV(String rutaArchivo) {
        try {
            // Crear o limpiar la tabla
            crearTablaPopulation(rutaArchivo); 
            List<String[]> registros = cargarDatosDesdeCSV(rutaArchivo);

            for (String[] registro : registros) {
                insertarRegistroEnBD(registro);

                // Crear una instancia de RegistroPoblacion y usar GestorXML para crear el archivo XML
                GestorXML.RegistroPoblacion registroPoblacion = new GestorXML.RegistroPoblacion(
                    registro[0], registro[1], registro[2], registro[3], registro[4], registro[5], registro[6], registro[7]
                );
                gestorXML.crearArchivoXML(registroPoblacion); 
            }
            System.out.println("Datos importados exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Crea la tabla 'population' en la base de datos usando el encabezado del archivo CSV.
     * Si la tabla ya existe, la elimina y la recrea con las columnas correspondientes.
     *
     * @param rutaArchivo Ruta del archivo CSV que contiene los nombres de columnas en el encabezado.
     * @throws SQLException Si ocurre un error de SQL al crear la tabla.
     * @throws IOException  Si ocurre un error al leer el archivo.
     */
    private void crearTablaPopulation(String rutaArchivo) throws SQLException, IOException {
        Statement stmt = conexion.createStatement();

        // Borrar la tabla "population" si existe
        stmt.execute("DROP TABLE IF EXISTS population");

        // Crear nueva tabla
        BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
        String encabezado = br.readLine();
        String[] columnas = encabezado.split(";");
        
        StringBuilder consultaCrearTabla = new StringBuilder("CREATE TABLE population (");
        for (String columna : columnas) {
            consultaCrearTabla.append(columna.trim()).append(" VARCHAR(30), ");
        }
        // Eliminar la última coma
        consultaCrearTabla.setLength(consultaCrearTabla.length() - 2); 
        consultaCrearTabla.append(")");

        stmt.execute(consultaCrearTabla.toString());
        stmt.close();
        br.close();
    }
    
    /**
     * Carga los datos de un archivo CSV y los almacena en una lista de registros.
     * Cada registro se representa como un arreglo de Strings.
     *
     * @param rutaArchivo Ruta del archivo CSV que contiene los datos.
     * @return Lista de registros, donde cada registro es un arreglo de Strings.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private List<String[]> cargarDatosDesdeCSV(String rutaArchivo) throws IOException {
        List<String[]> registros = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
        // Saltar encabezado
        br.readLine(); 

        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(";");
            registros.add(datos);
        }
        br.close();
        return registros;
    }
    
    /**
     * Inserta un registro en la tabla 'population' de la base de datos.
     *
     * @param registro Array de Strings que representa un registro (una fila) de datos.
     */
    private void insertarRegistroEnBD(String[] registro) {
        String sql = "INSERT INTO population (country, population, density, area, fertility, age, urban, share) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (int i = 0; i < registro.length; i++) {
                ps.setString(i + 1, registro[i]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al insertar registro en la base de datos: " + e.getMessage());
        }
    }
}
