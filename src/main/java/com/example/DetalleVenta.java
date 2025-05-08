package com.example;

import lombok.Data;

@Data
public class DetalleVenta {
    private int idDetalleVenta;
    private int idProducto;
    private int cantidad;
}
