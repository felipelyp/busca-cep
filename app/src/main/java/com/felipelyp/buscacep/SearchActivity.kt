package com.felipelyp.buscacep

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.felipelyp.buscacep.databinding.ActivitySearchBinding
import com.felipelyp.buscacep.utils.CepValidator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(R.layout.dialog_loading)
            .create()

        val model = ViewModelProvider(this)[SearchViewModel::class.java]

        model.result.observe(this, { result ->
            dialog.dismiss()

            when {
                result.isSuccess -> {
                    val cep = result.getOrNull()

                    if (cep!!.erro) {
                        binding.cepLayout.error = getString(R.string.cep_error)
                    }

                    binding.apply {
                        consultado.text = cep.cep.validate()
                        localidade.text = cep.localidade.validate()
                        estado.text = cep.uf.validate()
                        bairro.text = cep.bairro.validate()
                        endereco.text = cep.logradouro.validate()
                        complemento.text = cep.complemento.validate()
                        ddd.text = cep.ddd.validate()

                        binding.cardResult.visibility = View.VISIBLE
                    }
                }
                result.isFailure -> {
                    val error = result.exceptionOrNull()
                    Snackbar.make(
                        binding.root,
                        error?.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })

        binding.btnSearch.setOnClickListener {
            binding.cepLayout.error = ""
            val cep = binding.cep.text.toString()

            if (CepValidator.isValid(cep)) {
                binding.cardResult.visibility = View.GONE

                dialog.show()
                model.search(cep)
            } else {
                binding.cepLayout.error = getString(R.string.cep_invalid)
            }
        }
    }

    private fun String?.validate(): String {
        return when {
            this.isNullOrEmpty() -> getString(R.string.cep_info_empty)
            else -> this
        }
    }
}