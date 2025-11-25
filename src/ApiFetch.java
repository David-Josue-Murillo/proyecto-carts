import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiFetch {
    public String fetchApiData(String endpoint) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            // Configurar la conexión
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");

            // Se lee la respuesta de la API
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;

            // Se construye la respuesta línea por línea
            while((linea = entrada.readLine()) != null) {
                respuesta.append(linea);
            }

            // Cerrar conexiones
            entrada.close();
            conexion.disconnect();

            // Se devuelve la respuesta como cadena JSON
            return respuesta.toString();
        } catch (Exception e) {
            System.out.println("Error al obtener el JSON: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
