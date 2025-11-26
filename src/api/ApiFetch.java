package api;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiFetch {
    public Object fetchApiData() {
        try {
            URL url = new URL("https://dummyjson.com/carts?limit=15");
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

            // Se procesa la respuesta JSON y se convierte en un objeto Java para retornarlo
            Gson gson = new Gson();
            Object jsonObject = gson.fromJson(respuesta.toString(), Object.class);
            return jsonObject;
        } catch (Exception e) {
            System.out.println("Error al obtener el JSON: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
