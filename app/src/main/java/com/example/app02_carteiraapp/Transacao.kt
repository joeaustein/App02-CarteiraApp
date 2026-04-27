package com.example.app02_carteiraapp

import java.io.Serializable

data class Transacao(
    val id: Long = 0,
    val valor: Double,
    val tipo: String,
    val descricao: String,
    val data: String
) : Serializable
