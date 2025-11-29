package api;// interactua con la api

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

import com.google.gson.Gson;
import model.Cart;

import java.util.List;

public class ApiClientCarts {
    public List<Cart> obtenerCarts() {

        List<Cart> list = null;

        try {
			String urlStr = "https://dummyjson.com/carts?limit=20";
			URL url = new URL(urlStr);

            HttpURLConnection conexionServer = (HttpURLConnection) url.openConnection();

            conexionServer.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexionServer.getInputStream()));

            //verificar la respuesta del servidor
			int responseCode = conexionServer.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new IOException("Error: Código HTTP " + responseCode);
			}

            String line;
            StringBuilder respuesta = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                respuesta.append(line);
            }

            Gson gson = new Gson();
            ApiResponse apiResp = gson.fromJson(respuesta.toString(), ApiResponse.class);

            list = apiResp.getCarts();

        } catch (com.google.gson.JsonSyntaxException e) {
			System.out.println("Error al parsear JSON: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error de conexión al obtener datos de la API: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error al obtener los carritos: " + e.getMessage());
		}

        return list;
    }


    /*public static void main(String[] args) {
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
    }*/
}