package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.*;

import io.github.cdimascio.dotenv.Dotenv;

public class BdTable extends JFrame {
    Dotenv dotenv = Dotenv.load();

    private String url = dotenv.get("DB_URL");
    private String user = dotenv.get("DB_USER");
    private String pass = dotenv.get("DB_PASS");

    JTable tabla;
    DefaultTableModel modelo;
    JTextField txtId, txtNombre, txtDireccion, txtTelefono;
    JButton btnAgregar, btnModificar, btnEliminar, btnLimpiar;

    public BdTable() {
        setTitle("Personas Registradas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Modelo y tabla
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Dirección");
        modelo.addColumn("Teléfono");
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para formularios y botones
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos de texto
        panelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelFormulario.add(txtDireccion);

        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono);

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
                txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                txtDireccion.setText(modelo.getValueAt(fila, 2).toString());
                txtTelefono.setText(modelo.getValueAt(fila, 3).toString());
            }
        });

        // Acciones de los botones
        btnAgregar.addActionListener(e -> agregarPersona());
        btnModificar.addActionListener(e -> modificarPersona());
        btnEliminar.addActionListener(e -> eliminarPersona());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void cargarDatos() {
        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement("SELECT * FROM persona");
             ResultSet rs = ps.executeQuery()) {

            // Limpiar modelo antes de cargar
            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt("idpersona");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("direccion");
                fila[3] = rs.getString("telefono");
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage());
        }
    }

    private void agregarPersona() {
        String sql = "INSERT INTO persona (idpersona, nombre, direccion, telefono) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.setString(2, txtNombre.getText());
            ps.setString(3, txtDireccion.getText());
            ps.setString(4, txtTelefono.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Persona agregada correctamente");
            cargarDatos();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido");
        }
    }

    private void modificarPersona() {
        String sql = "UPDATE persona SET idpersona = ?, nombre = ?, direccion = ?, telefono = ? WHERE idpersona = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            int nuevoId = Integer.parseInt(txtId.getText());
            ps.setInt(1, nuevoId);
            ps.setString(2, txtNombre.getText());
            ps.setString(3, txtDireccion.getText());
            ps.setString(4, txtTelefono.getText());
            ps.setInt(5, Integer.parseInt(txtId.getText())); // Usamos el ID actual para la condición WHERE
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Persona modificada correctamente");
            cargarDatos();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido");
        }
    }

    private void eliminarPersona() {
        String sql = "DELETE FROM persona WHERE idpersona = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Persona eliminada correctamente");
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
        txtNombre.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        tabla.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BdTable().setVisible(true);
        });
    }
}