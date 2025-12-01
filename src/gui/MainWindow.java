package gui;

import api.ApiFetch;
import api.ApiResponse;
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
    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnLoadAPI;
    private JButton btnShowDB;
    private JButton btnUpdateDB;
    private JButton btnHideDB;

    private JScrollPane scrollPane; 

    private CartDAO dao = new CartDAO();

    // se realiza un constructor para inicializar la llamada a los metodos
    public MainWindow() {
        setTitle("Base de Datos - proyecto_cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // cierra el programa al cerrar ventana
        setLayout(new BorderLayout());   // para poder distribuir en NORTH, CENTER, SOUTH

        initTitlePanel();  // panel que contiene título e icono
        initTable();  // crea tabla y scroll
        initButtonsPanel(); // botones y eventos

        pack();  // ajusta la ventana al tamaño de sus componentes
        setLocationRelativeTo(null); // centrar en la pantalla
    }

    // metodos para el diseño

    private void initTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(255, 253, 208));

		// carga icono y se escala a un tamaño 50x50 píxeles
        ImageIcon icon = new ImageIcon("img/carrito-de-compras.png");
        icon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel imgLabel = new JLabel(icon);
        JLabel titleLabel = new JLabel("Gestión de Carritos - API y MySQL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        titlePanel.add(imgLabel);
        titlePanel.add(titleLabel);

        add(titlePanel, BorderLayout.NORTH);
    }

    private void initTable() {
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        tableModel.addColumn("ID");
        tableModel.addColumn("Total");
        tableModel.addColumn("Descuento");
        tableModel.addColumn("UserID");
        tableModel.addColumn("Total Productos");
        tableModel.addColumn("Total Cantidad");

        scrollPane = new JScrollPane(table);  // crea scroll contenedor de la tabla
        scrollPane.setVisible(false);   // oculta la tabla al inicio

        add(scrollPane, BorderLayout.CENTER);
    }

    // panel de botones y eventos
    private void initButtonsPanel() {
        JPanel buttonsPanel = new JPanel(); // panel inferior para los botones
        Color buttonColor = new Color(240, 253, 208);

        btnLoadAPI = new JButton("Cargar API a MySQL");
        btnShowDB = new JButton("Mostrar BD");
        btnUpdateDB = new JButton("Actualizar BD");
        btnHideDB = new JButton("Ocultar BD");

		// se coloca color alos botones
        btnLoadAPI.setBackground(buttonColor);
        btnShowDB.setBackground(buttonColor);
        btnUpdateDB.setBackground(buttonColor);
        btnHideDB.setBackground(buttonColor);

        buttonsPanel.add(btnLoadAPI);
        buttonsPanel.add(btnShowDB);
        buttonsPanel.add(btnUpdateDB);
        buttonsPanel.add(btnHideDB);

        add(buttonsPanel, BorderLayout.SOUTH);

        configureEvents(); // agregar eventos de botones
    }

    // eventos para los botones
    private void configureEvents() {

        // boton de cargar API
        btnLoadAPI.addActionListener(e -> {
            ApiClientCarts api = new ApiClientCarts();
            List<Cart> list = api.obtenerCarts(); // lista ordenada desde JSON

            if (list != null) {
                for (Cart cart : list) dao.insertCart(cart);

				// icono para elmensaje
                ImageIcon icon = new ImageIcon("img/exito.png");
                icon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

                JOptionPane.showMessageDialog(null, "Datos cargados desde la API correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE, icon);

            } else {
                JOptionPane.showMessageDialog(null, "Error al obtener datos de la API.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // boton de mostrar BD
        btnShowDB.addActionListener(e -> {
            tableModel.setRowCount(0);  // limpia la tabla antes de llenarla

            List<Cart> list = dao.getAll();
            // se agrga cada registro a la tabla
            for (Cart cart : list) {
                tableModel.addRow(new Object[]{
                        cart.getId(),
                        cart.getTotal(),
                        cart.getDiscountedTotal(),
                        cart.getUserId(),
                        cart.getTotalProducts(),
                        cart.getTotalQuantity()
                });
            }

            scrollPane.setVisible(true);
            pack(); // ajusta la ventana al nuevo tamaño
            setLocationRelativeTo(null); // se centra despues del ajuste
        });

        // boton de ctualizar BD
        btnUpdateDB.addActionListener(e -> {

            int rows = tableModel.getRowCount(); // numero de filas de la tabla

			 // se recorre las filas y se actualiza MySQL
            for (int i = 0; i < rows; i++) {
                Cart cart = new Cart();

				 // obtiene datos desde JTable y los asigna al objeto Cart
                cart.setId(Integer.parseInt(tableModel.getValueAt(i, 0).toString()));
                cart.setTotal(Double.parseDouble(tableModel.getValueAt(i, 1).toString()));
                cart.setDiscountedTotal(Double.parseDouble(tableModel.getValueAt(i, 2).toString()));
                cart.setUserId(Integer.parseInt(tableModel.getValueAt(i, 3).toString()));
                cart.setTotalProducts(Integer.parseInt(tableModel.getValueAt(i, 4).toString()));
                cart.setTotalQuantity(Integer.parseInt(tableModel.getValueAt(i, 5).toString()));

                dao.updateCart(cart);
            }

            ImageIcon icon = new ImageIcon("img/exito.png");
            icon = new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

            JOptionPane.showMessageDialog(null, "Base de datos actualizada correctamente.", "Actualización", JOptionPane.INFORMATION_MESSAGE, icon);
        });

        // boton de ocultar BD
        btnHideDB.addActionListener(e -> {
            scrollPane.setVisible(false);
            pack();
            setLocationRelativeTo(null);
        });
    }
}