package db;

import model.Cart;

import java.sql.*;
import java.util.*;

public class CartDAO {

    //  verifica si el registro ya esxite
    public boolean existCart(int id) {
        boolean existe = false;
        String sql = "SELECT id FROM carts WHERE id = ?";

        Connection conn = ConexionBD.getConexion();
        if (conn == null) {
            System.out.println("No se pudo obtener conexión a la base de datos en existCart().");
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                existe = true;
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia del carrito: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { conn.close(); } catch (SQLException ignored) {}
        }

        return existe;
    }


    /**
     * Inserta un nuevo registro de carrito en la tabla 'carts'.
     */
    public void insertCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo");
        }

        // Verificar duplicado antes de insertar
        if (existCart(cart.getId())) {
            System.out.println("Carrito ID " + cart.getId() + " ya existe. No se insertó.");
            return;
        }

        String sqlInsert = "INSERT INTO carts "
                + "(id, total, discounted_total, user_id, total_products, total_quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionBD.getConexion();
        if (conexion == null) {
            System.out.println("No se pudo obtener conexión a la base de datos en insertCart().");
            return;
        }

        try (PreparedStatement ps = conexion.prepareStatement(sqlInsert)) {

            ps.setInt(1, cart.getId());
            ps.setDouble(2, cart.getTotal());
            ps.setDouble(3, cart.getDiscountedTotal());
            ps.setInt(4, cart.getUserId());
            ps.setInt(5, cart.getTotalProducts());
            ps.setInt(6, cart.getTotalQuantity());

            ps.executeUpdate();
            System.out.println("Carrito ID " + cart.getId() + " insertado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al insertar carrito: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { conexion.close(); } catch (SQLException ignored) {}
        }
    }


    /**
     * Recupera todos los carritos almacenados en la base de datos.
     */
    public List<Cart> getAll() {
        List<Cart> lista = new ArrayList<>();
        String sql = "SELECT * FROM carts";

        Connection conexion = ConexionBD.getConexion();
        if (conexion == null) {
            System.out.println("No se pudo obtener conexión a la base de datos en getAll(). Devolviendo lista vacía.");
            return lista;
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Recorre los resultados y crea objetos Cart
            while (rs.next()) {
                Cart cart = new Cart();

                cart.setId(rs.getInt("id"));
                cart.setTotal(rs.getDouble("total"));
                cart.setDiscountedTotal(rs.getDouble("discounted_total"));
                cart.setUserId(rs.getInt("user_id"));
                cart.setTotalProducts(rs.getInt("total_products"));
                cart.setTotalQuantity(rs.getInt("total_quantity"));

                lista.add(cart);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener carritos de la base de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { conexion.close(); } catch (SQLException ignored) {}
        }

        // Devuelve una lista de objetos Cart para ser usada por la capa de presentación.
        return lista;
    }


    /**
     * Actualiza un carrito existente identificado por api_cart_id.
     * Solo actualiza los campos editables: user_id, total, discounted_total, total_products, total_quantity.
     */
    public void updateCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo");
        }

        String sql = "UPDATE carts SET user_id = ?, total = ?, discounted_total = ?, "
                + "total_products = ?, total_quantity = ? WHERE id = ?";

        Connection conexion = ConexionBD.getConexion();
        if (conexion == null) {
            System.out.println("No se pudo obtener conexión a la base de datos en updateCart().");
            return;
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, cart.getUserId());
            ps.setDouble(2, cart.getTotal());
            ps.setDouble(3, cart.getDiscountedTotal());
            ps.setInt(4, cart.getTotalProducts());
            ps.setInt(5, cart.getTotalQuantity());
            ps.setInt(6, cart.getId());

            // Ejecuta la actualización y obtiene el número de filas afectadas
            int updateId = ps.executeUpdate();
            if (updateId == 0) { // Se verifica si se actualizó algún registro
                System.out.println("No se encontró el cart ID " + cart.getId() + " para actualizar.");
                return;
            } // Si se actualizó correctamente, entonces se muestra el mensaje de exito
            System.out.println("Cart ID " + cart.getId() + " actualizado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al actualizar cart: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { conexion.close(); } catch (SQLException ignored) {}
        }
    }
}
