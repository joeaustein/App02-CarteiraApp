package com.example.app02_carteiraapp

data class Transaction(
    val id: Long = 0,
    val valor: Double,
    val tipo: String,
    val descricao: String,
    val data: String
)