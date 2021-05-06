package com.felipelyp.buscacep.utils

import java.util.regex.Pattern

object CepValidator {

    fun isValid(cep: String): Boolean {
        val regex = "^(([0-9]{2}\\\\.[0-9]{3}-[0-9]{3})|([0-9]{2}[0-9]{3}-[0-9]{3})|([0-9]{8}))\$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(cep.replace("-", ""))
        return matcher.find()
    }
}