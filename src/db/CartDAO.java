package db;

import java.sql.*;
import java.util.*;
import model.Cart;

public class CartDAO {

    /**
     * Inserta el registro de carrito en la tabla 'carts'.
     */
    public void insertCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo");
        }

        /** 
          *consulta SQL que intenta INSERTAR cuando se carga la api, si el 'id' ya existe (detectado por la clave primaria/única), 
         en lugar de fallar o duplicar, actualiza los campos especificados con los nuevos valores.
         */
        String sql = "INSERT INTO carts"
            + "(id, total, discounted_total, user_id, total_products, total_quantity)"
			+ "VALUES (?, ?, ?, ?, ?, ?) "
			+ "ON DUPLICATE KEY UPDATE "
			+ "total = VALUES(total), "
			+ "discounted_total = VALUES(discounted_total), "
			+ "user_id = VALUES(user_id), "
			+ "total_products = VALUES(total_products), "
			+ "total_quantity = VALUES(total_quantity)";

        Connection conexion = getConnectionOrNull("insertCart");
        if (conexion == null) return;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, cart.getId());
            ps.setDouble(2, cart.getTotal());
            ps.setDouble(3, cart.getDiscountedTotal());
            ps.setInt(4, cart.getUserId());
            ps.setInt(5, cart.getTotalProducts());
            ps.setInt(6, cart.getTotalQuantity());

            ps.executeUpdate();
            System.out.println("Carrito ID " + cart.getId() + " insertado correctamente");

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
        List<Cart> list = new ArrayList<>();
        String sql = "SELECT * FROM carts";

        Connection conexion = getConnectionOrNull("getAll");
        if (conexion == null) {
            System.out.println("Devolviendo lista vacía por falta de conexión.");
            return list;
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

                list.add(cart);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener carritos de la base de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { conexion.close(); } catch (SQLException ignored) {}
        }

        // Devuelve una lista de objetos Cart para ser usada por la capa de presentación.
        return list;
    }

    // metodo que obteniene un cart por ID
    public Cart getById(int id) {
        String sql = "SELECT * FROM carts WHERE id = ?";
        Connection conexion = getConnectionOrNull("getById");
        if (conexion == null) return null;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setTotal(rs.getDouble("total"));
                cart.setDiscountedTotal(rs.getDouble("discounted_total"));
                cart.setUserId(rs.getInt("user_id"));
                cart.setTotalProducts(rs.getInt("total_products"));
                cart.setTotalQuantity(rs.getInt("total_quantity"));
                return cart;
            }
        } catch (SQLException e) {
            System.out.println("Error en getById: " + e.getMessage());
        }
        return null;
    }

    // metodo para actualizar
    public void updateCart(Cart nuevo) {
        if (nuevo == null) {
            System.out.println("Carrito vacío, no se puede actualizar.");
            return;
        }

        // obtiene r el estado original desde la BD
        Cart original = getById(nuevo.getId());
        if (original == null) {
            System.out.println("Cart ID " + nuevo.getId() + " no encontrado en la BD.");
            return;
        }

        // verifica si hubo algún cambio
        boolean hayCambios =
                original.getUserId() != nuevo.getUserId() ||
                original.getTotal() != nuevo.getTotal() ||
                original.getDiscountedTotal() != nuevo.getDiscountedTotal() ||
                original.getTotalProducts() != nuevo.getTotalProducts() ||
                original.getTotalQuantity() != nuevo.getTotalQuantity();

        if (!hayCambios) {
            System.out.println("Cart ID " + nuevo.getId() + " sin cambios. No se actualiza.");
            return;
        }

        // si hay cambios se realiza update
        String sql = "UPDATE carts SET user_id = ?, total = ?, discounted_total = ?, " +
                "total_products = ?, total_quantity = ? WHERE id = ?";

        Connection conexion = getConnectionOrNull("updateCart");
        if (conexion == null) return;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, nuevo.getUserId());
            ps.setDouble(2, nuevo.getTotal());
            ps.setDouble(3, nuevo.getDiscountedTotal());
            ps.setInt(4, nuevo.getTotalProducts());
            ps.setInt(5, nuevo.getTotalQuantity());
            ps.setInt(6, nuevo.getId());

            ps.executeUpdate();
            System.out.println("Cart ID " + nuevo.getId() + " actualizado");

        } catch (SQLException e) {
            System.out.println("Error al actualizar el carrito: " + e.getMessage());
        }
    }

    // Método auxiliar para obtener la conexión y validar si es null.
    private Connection getConnectionOrNull(String callerMethod) {
        Connection conn = ConexionBD.getConexion();
        if (conn == null) {
            System.out.println("No se pudo obtener conexión a la base de datos en " + callerMethod + "().");
        }
        return conn;
    }

}
