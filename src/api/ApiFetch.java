package api;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiFetch {
    public Object fetchApiData() {
        HttpURLConnection conexion = null;

        try {
            conexion = createConnection("https://dummyjson.com/carts?limit=20");
            String response = readResponse(conexion);

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
        HttpURLConnection conexion = (HttpURLConnection) uri.toURL().openConnection();

        conexion.setRequestMethod("GET");
        conexion.setRequestProperty("Accept", "application/json");
        conexion.setConnectTimeout(5000);
        conexion.setReadTimeout(5000);

        return conexion;
    }

    /**
     * Lee la respuesta de la API usando try-with-resources.
     */
    private String readResponse(HttpURLConnection conexion) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
            StringBuilder respuesta = new StringBuilder();
            String linea;

            // Se construye la respuesta línea por línea
            while ((linea = reader.readLine()) != null) {
                respuesta.append(linea);
            }

            return respuesta.toString();
        }
    }
}
