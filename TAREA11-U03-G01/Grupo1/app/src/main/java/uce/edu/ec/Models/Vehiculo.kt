package uce.edu.ec.Models

import uce.edu.ec.R

data class Vehiculo(
    val placa: String,
    val marca: String,
    val modelo: String,
    val anio: Int,
    val color: String,
    val costoPorDia: Double,
    val activo: Boolean,
    val imagenRes: Int = R.drawable.a4,
)

