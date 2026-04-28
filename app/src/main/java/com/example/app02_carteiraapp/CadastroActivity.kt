package com.example.app02_carteiraapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CadastroActivity : AppCompatActivity() {

    private lateinit var transacaoDAO: TransacaoDAO
    private var transacaoParaEditar: Transacao? = null

    private lateinit var editTextValor: EditText
    private lateinit var editTextDescricao: EditText
    private lateinit var radioGroupTipo: RadioGroup
    private lateinit var radioButtonDebito: RadioButton
    private lateinit var radioButtonCredito: RadioButton
    private lateinit var btnSalvar: Button
    private lateinit var btnExcluir: Button
    private lateinit var textViewTitulo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        transacaoDAO = TransacaoDAO(this)

        // Inicializar componentes
        textViewTitulo = findViewById(R.id.textViewTitulo)
        editTextValor = findViewById(R.id.editTextValor)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        radioGroupTipo = findViewById(R.id.radioGroupTipo)
        radioButtonDebito = findViewById(R.id.radioButtonDebito)
        radioButtonCredito = findViewById(R.id.radioButtonCredito)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnExcluir = findViewById(R.id.btnExcluir)

        // Verificar se é edição
        transacaoParaEditar = intent.getSerializableExtra("TRANSACAO_EDITAR") as? Transacao

        if (transacaoParaEditar != null) {
            configurarModoEdicao(transacaoParaEditar!!)
        } else {
            radioButtonDebito.isChecked = true
        }

        btnSalvar.setOnClickListener { salvar() }
        btnExcluir.setOnClickListener { confirmarExclusao() }
    }

    private fun configurarModoEdicao(transacao: Transacao) {
        textViewTitulo.text = getString(R.string.titulo_editar)
        editTextValor.setText(transacao.valor.toString())
        editTextDescricao.setText(transacao.descricao)
        
        if (transacao.tipo == "CREDITO") {
            radioButtonCredito.isChecked = true
        } else {
            radioButtonDebito.isChecked = true
        }

        btnExcluir.visibility = View.VISIBLE
    }

    private fun salvar() {
        val valorText = editTextValor.text.toString().trim()
        val descricao = editTextDescricao.text.toString().trim()
        val tipo = if (radioGroupTipo.checkedRadioButtonId == R.id.radioButtonCredito) "CREDITO" else "DEBITO"

        // Validação (Requisito 7)
        if (valorText.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            if (valorText.isEmpty()) editTextValor.requestFocus() else editTextDescricao.requestFocus()
            return
        }

        val valor = valorText.toDoubleOrNull()
        if (valor == null || valor <= 0) {
            Toast.makeText(this, "Insira um valor válido e maior que zero", Toast.LENGTH_SHORT).show()
            editTextValor.requestFocus()
            return
        }

        val novaTransacao = Transacao(
            id = transacaoParaEditar?.id ?: 0,
            valor = valor,
            tipo = tipo,
            descricao = descricao,
            data = transacaoParaEditar?.data ?: obterDataHoraAtual()
        )

        val resultado = if (transacaoParaEditar == null) {
            transacaoDAO.adicionar(novaTransacao)
        } else {
            transacaoDAO.atualizar(novaTransacao).toLong()
        }

        if (resultado != -1L) {
            Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Erro ao salvar no banco de dados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmarExclusao() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage(getString(R.string.msg_confirmar_exclusao))
            .setPositiveButton("Sim") { _, _ ->
                transacaoParaEditar?.let {
                    transacaoDAO.excluir(it.id)
                    Toast.makeText(this, "Excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Não", null)
            .show()
    }
}
