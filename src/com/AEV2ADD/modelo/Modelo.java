package com.AEV2ADD.modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Modelo {
    private Connection con;
    private String tipoUsuario;
    
    /**
     * Método para establecer conexión con la base de datos usando credenciales de usuario.
     *
     * @param user     Nombre de usuario para la conexión.
     * @param password Contraseña del usuario.
     * @return true si la conexión fue exitosa, false en caso contrario.
     */
    public boolean conectar(String user, String password) {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar a la base de datos MySQL usando la contraseña proporcionada
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/population", user, password);
            System.out.println("Conexión correcta como " + user);

            // Configurar el tipo de usuario si es el administrador
            if (user.equals("admin")) {
                tipoUsuario = "admin";
            } else {
                tipoUsuario = "normalUser"; // Asignar tipo normal a usuarios que no adminis
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error en el inicio de sesión: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de MySQL: " + e.getMessage());
            return false;
        }
    }
   
    /**
     * Método para obtener tipo de usuario utilizando una variable interna para no generar conflicto en lo usuarios que no .
     * son del tipo admin.
     * @return String tipoUsuario.
     */
    public String obtenerTipoUsuario() {
        // Ahora, no necesitamos acceder a la base de datos
        return tipoUsuario;
    }
    
    /**
     * Método para iniciar la importacion del archivo CSV y generar con el las instancias en la base de datos y los ficheros XML.
     * @param rutaArchivo para indicar de donde se importa el CSV.
     */
    public void iniciarImportacionCSV(String rutaArchivo) {
        GestorArchivo gestorArchivo = new GestorArchivo(con);
        gestorArchivo.importarCSV(rutaArchivo);
    }
    
    /**
     * Obtiene el contenido de los archivos XML concatenado en un solo String.
     *
     * @return String con el contenido concatenado de los archivos XML.
     */
    public String obtenerContenidoXMLConcatenado() {
    	GestorXML gestorXML = new GestorXML();
        return gestorXML.concatenarArchivosXML();
    }

    /**
     * Método para cerrar la conexión con la base de datos.
     */
    public void desconectar() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Obtiene el tipo de usuario desde la base de datos usando el login.
     *
     * @param user Nombre del usuario.
     * @return Tipo de usuario según la base de datos.
     * @throws SQLException Si la conexión a la base de datos está cerrada o falla.
     */
    public String obtenerTipoUsuario(String user) throws SQLException {
        String tipo = "";
        if (con != null && !con.isClosed()) {
            PreparedStatement ps = con.prepareStatement("SELECT type FROM users WHERE login = ?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tipo = rs.getString("type");
            }
            rs.close();
            ps.close();
        } else {
            throw new SQLException("Conexión a la base de datos cerrada.");
        }
        return tipo;
    }
    
    /**
     * Método para registrar un nuevo usuario en la base de datos con sus permisos.
     *
     * @param username Nombre del usuario.
     * @param password Contraseña del usuario en texto plano.
     * @return true si el usuario fue registrado correctamente, false si hubo algún error.
     */
    public boolean registrarUsuario(String username, String password) {
        PreparedStatement ps = null;
        try {
            // Verificar si el usuario ya existe en la base de datos
            String sqlCheckUser = "SELECT COUNT(*) FROM population.users WHERE login = ?";
            ps = con.prepareStatement(sqlCheckUser);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            ps.close();

            if (count > 0) {
                System.out.println("El usuario ya existe. No se puede crear un usuario duplicado.");
                return false; // Si el usuario ya existe, se detiene el proceso de creación
            }

            // Crear el usuario en MySQL con la contraseña en texto plano
            String sqlCrearUsuario = "CREATE USER ?@'%' IDENTIFIED BY ?";
            ps = con.prepareStatement(sqlCrearUsuario);
            ps.setString(1, username);
            ps.setString(2, password); // texto plano
            ps.executeUpdate();
            ps.close();

            // Otorgar permisos de solo lectura en la tabla population.population
            String sqlGrantPermisos = "GRANT SELECT ON population.population TO ?@'%'";
            ps = con.prepareStatement(sqlGrantPermisos);
            ps.setString(1, username);
            ps.executeUpdate();

            // Confirmar los cambios de permisos
            ps.execute("FLUSH PRIVILEGES");
            ps.close();

            // Generar hash de la contraseña para la tabla interna
            String hashedPassword = generarHashMD5(password);

            // Insertar el usuario en la tabla interna de usuarios
            String sqlInsertUsuario = "INSERT INTO population.users (login, password, type) VALUES (?, ?, 'normalUser')";
            ps = con.prepareStatement(sqlInsertUsuario);
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.executeUpdate();
            ps.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Ejecuta una consulta en la base de datos y devuelve los resultados.
     *
     * @param query  La consulta SQL a ejecutar.
     * @param tipoUsuario El tipo de usuario que ejecuta la consulta(para ver si puede realizar esa consulta).
     * @return String con el resultado de la consulta.
     */
    public String ejecutarConsulta(String query, String tipoUsuario) {
        StringBuilder resultado = new StringBuilder();
        try {
            // Si el usuario no es admin y la consulta no es de tipo SELECT, mostrar error
            if (!"admin".equals(tipoUsuario) && !query.trim().toLowerCase().startsWith("select")) {
                return "Error: Solo se permiten consultas de tipo SELECT para este usuario.";
            }

            // Crear un Statement y ejecutar la consulta
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Agregar encabezado de columnas al resultado
            for (int i = 1; i <= columnCount; i++) {
                resultado.append(rsmd.getColumnName(i)).append("\t");
            }
            resultado.append("\n");

            // Agregar filas de resultados al resultado
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    resultado.append(rs.getString(i)).append("\t");
                }
                resultado.append("\n");
            }

            // Cerrar ResultSet y Statement
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // Manejar el error de permisos o errores en la consulta
            resultado.append("Error en la consulta: ").append(e.getMessage());
        }

        return resultado.toString();
    }
    
    /**
     * Exporta el contenido de una consulta a un archivo CSV.
     *
     * @param contenido     Datos a exportar.
     * @param nombreArchivo Nombre del archivo CSV a crear.
     */
    public void exportarVistaCSV(String contenido, String nombreArchivo) {
        File carpeta = new File("csv");
        if (!carpeta.exists()) {
            carpeta.mkdir(); // Crear la carpeta si no existe
        }
        File archivoCSV = new File(carpeta, nombreArchivo + ".csv");

        try (FileOutputStream os = new FileOutputStream(archivoCSV);
             OutputStreamWriter fw = new OutputStreamWriter(os, "UTF-8");
             BufferedWriter bw = new BufferedWriter(fw)) {

            // Dividir el contenido en líneas
            String[] lineas = contenido.split("\n");

            // Escribir encabezado y datos
            for (String linea : lineas) {
                bw.write(linea.replace("\t", ";")); // Reemplaza tabulaciones por punto y coma
                bw.newLine();
            }
            System.out.println("Exportación completada en " + archivoCSV.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al exportar CSV: " + e.getMessage());
        }
    }

    /**
     * Genera un hash MD5 de la cadena de entrada.
     *
     * @param input Texto a hashear.
     * @return String con el hash MD5.
     */
    private String generarHashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
