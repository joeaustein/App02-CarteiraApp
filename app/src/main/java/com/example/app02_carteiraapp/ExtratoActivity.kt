package com.example.app02_carteiraapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExtratoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewSaldo: TextView
    private lateinit var radioGroupFiltro: RadioGroup
    private lateinit var editTextBusca: EditText
    private lateinit var adapter: TransacaoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extrato)

        dbHelper = DBHelper(this)

        recyclerView = findViewById(R.id.recyclerViewTransactions)
        textViewSaldo = findViewById(R.id.textViewSaldo)
        radioGroupFiltro = findViewById(R.id.radioGroupFiltro)
        editTextBusca = findViewById(R.id.editTextBusca)

        setupRecyclerView()
        setupListeners()
        atualizarResumo()
    }

    private fun setupRecyclerView() {
        adapter = TransacaoAdapter(dbHelper.buscarTodasAsTransacoes()) { transacao ->
            val intent = Intent(this, CadastroActivity::class.java)
            intent.putExtra("TRANSACAO_EDITAR", transacao)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        radioGroupFiltro.setOnCheckedChangeListener { _, checkedId ->
            filtrar()
        }

        editTextBusca.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrar()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filtrar() {
        val busca = editTextBusca.text.toString()
        val listaFiltrada = if (busca.isNotEmpty()) {
            dbHelper.buscarTransacaoPorDescricao(busca)
        } else {
            when (radioGroupFiltro.checkedRadioButtonId) {
                R.id.radioCreditos -> dbHelper.buscarTransacoesPorTipo("CREDITO")
                R.id.radioDebitos -> dbHelper.buscarTransacoesPorTipo("DEBITO")
                else -> dbHelper.buscarTodasAsTransacoes()
            }
        }
        adapter.atualizarLista(listaFiltrada)
    }

    private fun atualizarResumo() {
        val saldo = dbHelper.obterSaldoTotal()
        textViewSaldo.text = "Saldo: R$ ${"%.2f".format(saldo)}"
    }

    override fun onResume() {
        super.onResume()
        filtrar()
        atualizarResumo()
    }
}
