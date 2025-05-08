package com.example;

import lombok.Data;

@Data
public class Producto {
    private int idProducto;
    private String nombre;
    private int precio;
    private int idProveedor;
}
