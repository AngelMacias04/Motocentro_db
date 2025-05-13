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
public class Moto extends JFrame {
    public Moto(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;

        setTitle("Gestión de Motos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Modelo y tabla
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Placa");
        modelo.addColumn("ID Cliente");
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

        panelFormulario.add(new JLabel("Placa:"));
        txtPlaca = new JTextField();
        panelFormulario.add(txtPlaca);

        panelFormulario.add(new JLabel("ID Cliente:"));
        txtIdCliente = new JTextField();
        panelFormulario.add(txtIdCliente);

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
                txtPlaca.setText(modelo.getValueAt(fila, 1).toString());
                txtIdCliente.setText(modelo.getValueAt(fila, 2).toString());
            }
        });

        // Acciones de los botones
        btnAgregar.addActionListener(e -> agregarMoto());
        btnModificar.addActionListener(e -> modificarMoto());
        btnEliminar.addActionListener(e -> eliminarMoto());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private String url;
    private String user;
    private String pass;
    private int idMoto;
    private String placa;
    private int idCliente;

    private JTable tabla;
    private JTextField txtId, txtPlaca, txtIdCliente;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnModificar, btnEliminar, btnLimpiar;

    private void cargarDatos() {
        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement("SELECT * FROM motocentro.moto");
             ResultSet rs = ps.executeQuery()) {

            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("idMoto");
                fila[1] = rs.getString("placa");
                fila[2] = rs.getInt("idCliente");
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage());
        }
    }

    private void agregarMoto() {
        String sql = "INSERT INTO motocentro.moto (idMoto, placa, idCliente) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.setString(2, txtPlaca.getText());
            ps.setInt(3, Integer.parseInt(txtIdCliente.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Moto agregada correctamente");
            cargarDatos();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID y ID Cliente deben ser números válidos");
        }
    }

    private void modificarMoto() {
        String sql = "UPDATE motocentro.moto SET idMoto = ?, placa = ?, idCliente = ? WHERE idMoto = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.setString(2, txtPlaca.getText());
            ps.setInt(3, Integer.parseInt(txtIdCliente.getText()));
            ps.setInt(4, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Moto modificada correctamente");
            cargarDatos();
            limpiarCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID y ID Cliente deben ser números válidos");
        }
    }

    private void eliminarMoto() {
        String sql = "DELETE FROM motocentro.moto WHERE idMoto = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Moto eliminada correctamente");
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
        txtPlaca.setText("");
        txtIdCliente.setText("");
    }
}