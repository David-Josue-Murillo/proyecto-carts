package api;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ApiFetch {
    public Object fetchApiData() {
        HttpURLConnection connection = null;

        try {
            connection = createConnection("https://dummyjson.com/carts?limit=20");
            String response = readResponse(connection);

            // Se procesa la respuesta JSON y se convierte en un objeto Java para retornarlo
            Gson gson = new Gson();
            return gson.fromJson(response, Object.class);
        } catch (Exception e) {
            System.out.println("Error al obtener el JSON: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea y configura la conexión HTTP.
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
     * Lee la respuesta de la API usando try-with-resources.
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
