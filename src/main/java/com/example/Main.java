package com.example;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String pass = dotenv.get("DB_PASS");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema de Gestión");
            frame.setSize(400, 350);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new GridLayout(10, 1, 10, 10));

            // Botones para cada entidad
            JButton btnCargo = new JButton("Gestionar Cargos");
            JButton btnCliente = new JButton("Gestionar Clientes");
            JButton btnProveedor = new JButton("Gestionar Proveedores");
            JButton btnEmpleado = new JButton("Gestionar Empleados");
            JButton btnMoto = new JButton("Gestionar Motos");
            JButton btnProducto = new JButton("Gestionar Productos");
            JButton btnInventario = new JButton("Gestionar Inventario");
            JButton btnVenta = new JButton("Gestionar Ventas");
            JButton btnDetalleVenta = new JButton("Gestionar Detalles de Venta");
            JButton btnReparacion = new JButton("Gestionar Reparaciones");

            // Acciones de los botones
            btnCargo.addActionListener(e -> new Cargo(url, user, pass).setVisible(true));
            btnCliente.addActionListener(e -> new Cliente(url, user, pass).setVisible(true));
            btnProveedor.addActionListener(e -> new Proveedor(url, user, pass).setVisible(true));
            btnEmpleado.addActionListener(e -> new Empleado(url, user, pass).setVisible(true));
            btnMoto.addActionListener(e -> new Moto(url, user, pass).setVisible(true));
            btnProducto.addActionListener(e -> new Producto(url, user, pass).setVisible(true));
            btnInventario.addActionListener(e -> new Inventario(url, user, pass).setVisible(true));
            btnVenta.addActionListener(e -> new Venta(url, user, pass).setVisible(true));
            btnDetalleVenta.addActionListener(e -> new DetalleVenta(url, user, pass).setVisible(true));
            btnReparacion.addActionListener(e -> new Reparacion(url, user, pass).setVisible(true));

            // Agregar botones al frame
            frame.add(btnCargo);
            frame.add(btnCliente);
            frame.add(btnProveedor);
            frame.add(btnEmpleado);
            frame.add(btnMoto);
            frame.add(btnProducto);
            frame.add(btnInventario);
            frame.add(btnVenta);
            frame.add(btnDetalleVenta);
            frame.add(btnReparacion);

            frame.setVisible(true);
        });
    }
}