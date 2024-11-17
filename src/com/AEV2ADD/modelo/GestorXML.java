package com.AEV2ADD.modelo;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Clase GestorXML que permite la creación de archivos XML con los datos proporcionados desde el archivo CSV. Usa una clase interna para modelar los datos y facilitar la
 * concatenacion y exportación en formato XML.
 */
public class GestorXML {

	 /**
     * Clase interna para representar los datos de población de un país.
     */
    public static class RegistroPoblacion {
        private String country;
        private String population;
        private String density;
        private String area;
        private String fertility;
        private String age;
        private String urban;
        private String share;
        /**
         * Constructor para inicializar un registro de población con los datos específicos de un país.
         *
         * @param country   Nombre del pais.
         * @param population Población del pais.
         * @param density   Densidad poblacional.
         * @param area      Área geográfica.
         * @param fertility Tasa de fertilidad.
         * @param age       Edad media de la población.
         * @param urban     Porcentaje de población urbana.
         * @param share     Participación en población mundial.
         */
        public RegistroPoblacion(String country, String population, String density, String area, String fertility, String age, String urban, String share) {
            this.country = country;
            this.population = population;
            this.density = density;
            this.area = area;
            this.fertility = fertility;
            this.age = age;
            this.urban = urban;
            this.share = share;
        }

        public String getCountry() {
            return country;
        }

        public String getPopulation() {
            return population;
        }

        public String getDensity() {
            return density;
        }

        public String getArea() {
            return area;
        }

        public String getFertility() {
            return fertility;
        }

        public String getAge() {
            return age;
        }

        public String getUrban() {
            return urban;
        }

        public String getShare() {
            return share;
        }
    }

    // Método para crear un archivo XML a partir de un registro de población
    public void crearArchivoXML(RegistroPoblacion registro) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("population");
            document.appendChild(root);

            // Create elements with trimmed values
            crearElemento(document, root, "country", registro.getCountry().trim());
            crearElemento(document, root, "population", registro.getPopulation().trim());
            crearElemento(document, root, "density", registro.getDensity().trim());
            crearElemento(document, root, "area", registro.getArea().trim());
            crearElemento(document, root, "fertility", registro.getFertility().trim());
            crearElemento(document, root, "age", registro.getAge().trim());
            crearElemento(document, root, "urban", registro.getUrban().trim());
            crearElemento(document, root, "share", registro.getShare().trim());

            // Overwrite the file if it exists
            String nombreArchivo = "xml/" + registro.getCountry() + ".xml";
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);

            // Overwrite the file each time
            try (FileOutputStream fos = new FileOutputStream(nombreArchivo, false)) {
                StreamResult result = new StreamResult(fos);
                transformer.transform(source, result);
            }

            System.out.println("Archivo XML creado: " + registro.getCountry());

        } catch (Exception e) {
            System.out.println("Error al crear XML para " + registro.getCountry() + ": " + e.getMessage());
        }
    }

    // Método auxiliar para crear y añadir elementos XML
    private void crearElemento(Document document, Element root, String nombreElemento, String valor) {
        Element elemento = document.createElement(nombreElemento);
        elemento.appendChild(document.createTextNode(valor.trim())); // Usa .trim() para limpiar el valor
        root.appendChild(elemento);
    }

    /**
     * Método para concatenar el contenido de todos los archivos XML en la carpeta 'xml'
     * @return el contenido del XML concatenado como string.
     */
    public String concatenarArchivosXML() {
        StringBuilder contenidoConcatenado = new StringBuilder();
        File carpetaXML = new File("xml");
        File[] archivos = carpetaXML.listFiles((dir, name) -> name.endsWith(".xml"));

        if (archivos == null || archivos.length == 0) {
            return "No se encontraron archivos XML en la carpeta.";
        }

        for (File archivo : archivos) {
            try {
                System.out.println("Leyendo y concatenando el archivo: " + archivo.getName());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(archivo);

                contenidoConcatenado.append("------------------\n");
                contenidoConcatenado.append("Country: ").append(limpiarTexto(obtenerTextoElemento(document, "country"))).append("\n");
                contenidoConcatenado.append("Population: ").append(limpiarTexto(obtenerTextoElemento(document, "population"))).append("\n");
                contenidoConcatenado.append("Density: ").append(limpiarTexto(obtenerTextoElemento(document, "density"))).append("\n");
                contenidoConcatenado.append("Area: ").append(limpiarTexto(obtenerTextoElemento(document, "area"))).append("\n");
                contenidoConcatenado.append("Fertility: ").append(limpiarTexto(obtenerTextoElemento(document, "fertility"))).append("\n");
                contenidoConcatenado.append("Age: ").append(limpiarTexto(obtenerTextoElemento(document, "age"))).append("\n");
                contenidoConcatenado.append("Urban: ").append(limpiarTexto(obtenerTextoElemento(document, "urban"))).append("\n");
                contenidoConcatenado.append("Share: ").append(limpiarTexto(obtenerTextoElemento(document, "share"))).append("\n");
                contenidoConcatenado.append("------------------\n\n");

            } catch (Exception e) {
                System.out.println("Error al leer y procesar el archivo XML: " + archivo.getName() + ". Error: " + e.getMessage());
            }
        }
        return contenidoConcatenado.toString();
    }
    
    /**
     * Método auxiliar para obtener el texto de un elemento XML por nombre.
     *
     * @param document       Documento XML donde se busca el elemento.
     * @param nombreElemento Nombre del elemento del que se obtiene el texto.
     * @return Texto del elemento o "N/A" si no existe.
     */
    private String obtenerTextoElemento(Document document, String nombreElemento) {
        NodeList nodeList = document.getElementsByTagName(nombreElemento);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent().trim(); // Trim spaces
        }
        return "N/A";
    }
    
    /**
     * Método auxiliar para limpiar el texto eliminando espacios y saltos de línea adicionales.
     *
     * @param texto Texto a limpiar.
     * @return Texto limpio con espacios y saltos de línea reducidos.
     */
    private String limpiarTexto(String texto) {
        if (texto == null) {
            return "N/A";
        }
        return texto.replaceAll("\\s+", " ").trim(); // Reemplaza múltiples espacios y saltos de línea por un solo espacio
    }



}
