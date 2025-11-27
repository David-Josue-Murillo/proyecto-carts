package db;

import model.Cart;

import java.sql.*;
import java.util.*;

public class CartDAO {

    //  verifica si el registro ya esxite
    public boolean existCart(int id) {
        boolean existe = false;
        String sql = "SELECT id FROM carts WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                existe = true;
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar carrito: " + e.getMessage());
        }

        return existe;
    }


    /**
     * Inserta un nuevo registro de carrito en la tabla 'carts'.
     */
    public void insertCart(Cart cart) {
        // Verificar duplicado antes de insertar
        if (existCart(cart.getId())) {
            System.out.println("Carrito ID " + cart.getId() + " ya existe. No se insertó.");
            return;
        }

        String sqlInsert = "INSERT INTO carts "
                + "(id, total, discounted_total, user_id, total_products, total_quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sqlInsert)) {

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
        }
    }


    /**
     * Recupera todos los carritos almacenados en la base de datos.
     * Devuelve una lista de objetos Cart para ser usada por la capa de presentación.
     */
    public List<Cart> getAll() {
        List<Cart> lista = new ArrayList<>();
        String sql = "SELECT * FROM carts";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cart c = new Cart();

                c.setId(rs.getInt("id"));
                c.setTotal(rs.getDouble("total"));
                c.setDiscountedTotal(rs.getDouble("discounted_total"));
                c.setUserId(rs.getInt("user_id"));
                c.setTotalProducts(rs.getInt("total_products"));
                c.setTotalQuantity(rs.getInt("total_quantity"));

                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener carritos: " + e.getMessage());
        }

        return lista;
    }


    // actualiza el registro
    public void actualizar(Cart c) {

        String sql = "UPDATE carts SET total = ?, discounted_total = ?, user_id = ?, "
                + "total_products = ?, total_quantity = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, c.getTotal());
            ps.setDouble(2, c.getDiscountedTotal());
            ps.setInt(3, c.getUserId());
            ps.setInt(4, c.getTotalProducts());
            ps.setInt(5, c.getTotalQuantity());
            ps.setInt(6, c.getId());

            ps.executeUpdate();
            System.out.println("Carrito ID " + c.getId() + " actualizado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al actualizar carrito: " + e.getMessage());
        }
    }
}
