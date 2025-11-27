package api;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ApiFetch {
    /**
     * Se realiza una petición GET a la URL de carts y devuelve el JSON parseado.
     * devolvinedo un Object que representa la estructura del JSON. D
     */
    public Object fetchApiData() {
        HttpURLConnection connection = null;

        try {
            // Se crea y configura la conexión HTTP hacia la URL del API
            connection = createConnection("https://dummyjson.com/carts?limit=20");
            String response = readResponse(connection);

            // Se procesa la respuesta JSON y se convierte en un objeto Java para retornarlo
            Gson gson = new Gson();
            return gson.fromJson(response, Object.class);
        } catch (Exception e) {
            /*
            * Si ocurre cualquier excepción durante la creación de la conexión o la
            * lectura, se imprime un mensaje por consola y se lanza un
            * RuntimeException envolviendo la causa original.
             */
            System.out.println("Error al obtener el JSON: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea y configura la conexión HTTP (GET) hacia la URL dada,
     * construyendo una URI a partir de la cadena de entrada.
     * Devuelve una instancia de HttpURLConnection lista para usarse.
     *
     * Excepciones:
     * - Lanza IOException o URISyntaxException si la URL no es válida o si
     *   falla la apertura de la conexión.
     */
    private HttpURLConnection createConnection(String urlString) throws IOException, URISyntaxException {
        URI uri = new URI(urlString);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        return connection;
    }

    /**
     * Lee la respuesta de la conexión HTTP como una cadena completa,
     * abriendo un BufferedReader sobre el stream de entrada de la conexión
     * y concatena todas las líneas en una sola cadena.
     * Además usa try-with-resources para asegurar el cierre del lector y
     * devuelve la respuesta completa (cuerpo) como String.
     *
     * Excepciones:
     * - Lanza IOException si ocurre un error al leer el stream.
     */
    private String readResponse(HttpURLConnection conexion) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String linea;

            // Se construye la respuesta línea por línea
            while ((linea = reader.readLine()) != null) {
                response.append(linea);
            }

            return response.toString();
        }
    }
}
