package com.example.app02_carteiraapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        val editTextValor = findViewById<EditText>(R.id.editTextValor)
        val editTextDescricao = findViewById<EditText>(R.id.txtViewDescricao)
        val radioGroupTipo = findViewById<RadioGroup>(R.id.btnTipo)
        val radioDebito = findViewById<RadioButton>(R.id.btnDebito)
        val radioCredito = findViewById<RadioButton>(R.id.btnCredito)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        radioDebito.isChecked = true

        btnSalvar.setOnClickListener {
            val valorText = editTextValor.text.toString().trim()
            val descricao = editTextDescricao.text.toString().trim()
            val selectedId = radioGroupTipo.checkedRadioButtonId
            val tipo = if (selectedId == R.id.btnCredito) "CREDITO" else "DEBITO"

            if (valorText.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val valor = valorText.toDouble()

                if (valor <= 0) {
                    Toast.makeText(this, "O valor deve ser maior que zero", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                val dbHelper = DBHelper(this)
                val transaction = Transaction(
                    valor = valor,
                    tipo = tipo,
                    descricao = descricao,
                    data = getCurrentDateTime()
                )

                val id = dbHelper.addTransaction(transaction)

                if (id != -1L) {
                    val tipoTexto = if (tipo == "CREDITO") "Crédito" else "Débito"
                    val mensagem = "Transação de $tipoTexto salva:\n" +
                            "Valor: R$ ${"%.2f".format(valor)}\n" +
                            "Descrição: $descricao"

                    Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show()


                    // limpa os campos depois de salvar
                    editTextValor.text.clear()
                    editTextDescricao.text.clear()
                    radioDebito.isChecked = true
                } else {
                    Toast.makeText(this, "Erro ao salvar transação!", Toast.LENGTH_SHORT).show()
                }

            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Digite um valor válido!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
