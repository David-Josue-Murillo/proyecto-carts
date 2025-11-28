package gui;

import api.ApiClientCarts;
import db.CartDAO;
import model.Cart;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnCargarAPI;
    private JButton btnMostrarBD;
    private JButton btnActualizarBD;

    private CartDAO dao = new CartDAO();

    public MainWindow() {
        setTitle("Proyecto JSON - MySQL - JTable");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configurar la tabla
        modelo = new DefaultTableModel();
        tabla = new JTable(modelo);

        modelo.addColumn("ID");
        modelo.addColumn("Total");
        modelo.addColumn("Descuento");
        modelo.addColumn("UserID");
        modelo.addColumn("Total Productos");
        modelo.addColumn("Total Cantidad");

        JScrollPane scroll = new JScrollPane(tabla);


        // Botones
        btnCargarAPI = new JButton("Cargar API -> MySQL");
        btnMostrarBD = new JButton("Mostrar desde BD");
        btnActualizarBD = new JButton("Guardar cambios");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCargarAPI);
        panelBotones.add(btnMostrarBD);
        panelBotones.add(btnActualizarBD);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);


        // EVENTOS BOTONES

        // BOTÓN 1: CARGAR API y guardar en MySQL
        btnCargarAPI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ApiClientCarts api = new ApiClientCarts();
                java.util.List<Cart> lista = api.obtenerCarts();

                if (lista != null) {
                    for (Cart c : lista) {
                        dao.insertCart(c);
                    }
                    JOptionPane.showMessageDialog(null, "Datos cargados desde API y guardados en MySQL.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al obtener datos de la API.");
                }
            }
        });

        // BOTÓN 2: Mostrar datos de MySQL en JTable
        btnMostrarBD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                modelo.setRowCount(0); // limpiar tabla

                List<Cart> lista = dao.getAll();

                for (Cart c : lista) {
                    modelo.addRow(new Object[]{
                            c.getId(),
                            c.getTotal(),
                            c.getDiscountedTotal(),
                            c.getUserId(),
                            c.getTotalProducts(),
                            c.getTotalQuantity()
                    });
                }

            }
        });

        // BOTÓN 3: Guardar cambios hechos en JTable -> MySQL
        btnActualizarBD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int filas = modelo.getRowCount();

                for (int i = 0; i < filas; i++) {

                    Cart c = new Cart();

                    c.setId(Integer.parseInt(modelo.getValueAt(i, 0).toString()));
                    c.setTotal(Double.parseDouble(modelo.getValueAt(i, 1).toString()));
                    c.setDiscountedTotal(Double.parseDouble(modelo.getValueAt(i, 2).toString()));
                    c.setUserId(Integer.parseInt(modelo.getValueAt(i, 3).toString()));
                    c.setTotalProducts(Integer.parseInt(modelo.getValueAt(i, 4).toString()));
                    c.setTotalQuantity(Integer.parseInt(modelo.getValueAt(i, 5).toString()));

                    dao.updateCart(c);
                }

                JOptionPane.showMessageDialog(null, "Base de datos actualizada.");
            }
        });
    }
}
