package com.felipelyp.buscacep.model

import androidx.annotation.Keep

@Keep
data class Cep(
    val bairro: String,
    val cep: String,
    val complemento: String,
    val ddd: String,
    val localidade: String,
    val logradouro: String,
    val uf: String,
    val erro: Boolean
)