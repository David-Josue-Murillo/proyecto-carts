// interactua con la api

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import java.util.List;

public class ApiClientCarts {
    public List<Cart> obtenerCarts() {

        List<Cart> lista = null;

        try {
			String urlStr = "https://dummyjson.com/carts?limit=20";
			URL url = new URL(urlStr);

            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            conexion.setRequestMethod("GET");

            BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

            String linea;
            StringBuilder respuesta = new StringBuilder();
            while ((linea = lector.readLine()) != null) {
                respuesta.append(linea);
            }

            Gson gson = new Gson();
            ApiResponse apiResp = gson.fromJson(respuesta.toString(), ApiResponse.class);

            lista = apiResp.getCarts();

        } catch (Exception e) {
            System.out.println("Error al obtener los carritos: " + e.getMessage());
        }

        return lista;
    }


    public static void main(String[] args) {
        ApiClientCarts api = new ApiClientCarts();

        List<Cart> carts = api.obtenerCarts();

        if (carts != null) {
            for (Cart c : carts) {
                System.out.println("ID: " + c.getId() +
                                   ", Total: " + c.getTotal() +
                                   ", Descuento: " + c.getDiscountedTotal() +
                                   ", id de usuario: " + c.getUserId() +
                                   ", Cantidad de pedidos: " + c.getTotalProducts() +
                                   ", Total de productos: " + c.getTotalQuantity() + "\n");
            }
        }
    }
}
