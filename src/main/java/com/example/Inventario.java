package com.example;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Inventario extends JFrame {
    public Inventario(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;

        setTitle("Gestión de Inventario");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Modelo y tabla
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("ID Producto");
        modelo.addColumn("Cantidad");
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para formularios y botones
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos de texto
        panelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("ID Producto:"));
        txtIdProducto = new JTextField();
        panelFormulario.add(txtIdProducto);

        panelFormulario.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        panelFormulario.add(txtCantidad);

        // Botones
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelFormulario.add(btnAgregar);
        panelFormulario.add(btnModificar);
        panelFormulario.add(btnEliminar);
        panelFormulario.add(btnLimpiar);

        add(panelFormulario, BorderLayout.SOUTH);

        // Cargar datos iniciales
        cargarDatos();

        // Seleccionar fila
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (tabla.getSelectedRow() != -1) {
                int fila = tabla.getSelectedRow();
                txtId.setText(modelo.getValueAt(fila, 0).toString());
                txtIdProducto.setText(modelo.getValueAt(fila, 1).toString());
                txtCantidad.setText(modelo.getValueAt(fila, 2).toString());
            }
        });

        // Acciones de los botones
        btnAgregar.addActionListener(e -> agregarInventario());
        btnModificar.addActionListener(e -> modificarInventario());
        btnEliminar.addActionListener(e -> eliminarInventario());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private String url;
    private String user;
    private String pass;
    private int idInventario;
    private int idProducto;
    private int cantidad;

    private JTable tabla;
    private JTextField txtId, txtIdProducto, txtCantidad;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnModificar, btnEliminar, btnLimpiar;

    private void cargarDatos() {
        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement("SELECT * FROM motocentro.inventario");
             ResultSet rs = ps.executeQuery()) {

            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("idInventario");
                fila[1] = rs.getInt("idProducto");
                fila[2] = rs.getInt("cantidad");
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage());
        }
    }

    private void agregarInventario() {
        String sql = "INSERT INTO motocentro.inventario (idInventario, idProducto, cantidad) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.setInt(2, Integer.parseInt(txtIdProducto.getText()));
            ps.setInt(3, Integer.parseInt(txtCantidad.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Inventario agregado correctamente");
            cargarDatos();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser números válidos");
        }
    }

    private void modificarInventario() {
        String sql = "UPDATE motocentro.inventario SET idInventario = ?, idProducto = ?, cantidad = ? WHERE idInventario = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.setInt(2, Integer.parseInt(txtIdProducto.getText()));
            ps.setInt(3, Integer.parseInt(txtCantidad.getText()));
            ps.setInt(4, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Inventario modificado correctamente");
            cargarDatos();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser números válidos");
        }
    }

    private void eliminarInventario() {
        String sql = "DELETE FROM motocentro.inventario WHERE idInventario = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Inventario eliminado correctamente");
            cargarDatos();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido");
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtIdProducto.setText("");
        txtCantidad.setText("");
    }
}