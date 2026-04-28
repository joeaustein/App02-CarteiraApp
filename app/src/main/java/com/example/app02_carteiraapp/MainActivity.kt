package com.example.app02_carteiraapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var transacaoDAO: TransacaoDAO
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewSaldo: TextView
    private lateinit var radioGroupFiltro: RadioGroup
    private lateinit var editTextBusca: EditText
    private lateinit var adapter: TransacaoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        transacaoDAO = TransacaoDAO(this)

        recyclerView = findViewById(R.id.recyclerViewTransactions)
        textViewSaldo = findViewById(R.id.textViewSaldo)
        radioGroupFiltro = findViewById(R.id.radioGroupFiltro)
        editTextBusca = findViewById(R.id.editTextBusca)
        val btnCadastro = findViewById<Button>(R.id.btnCadastro)
        val btnSair = findViewById<Button>(R.id.btnSair)

        setupRecyclerView()
        setupListeners()
        
        btnCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        btnSair.setOnClickListener {
            finishAffinity()
        }
    }

    private fun setupRecyclerView() {
        adapter = TransacaoAdapter(transacaoDAO.buscarTodas()) { transacao ->
            val intent = Intent(this, CadastroActivity::class.java)
            intent.putExtra("TRANSACAO_EDITAR", transacao)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        radioGroupFiltro.setOnCheckedChangeListener { _, _ -> filtrar() }

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
            transacaoDAO.buscarPorDescricao(busca)
        } else {
            when (radioGroupFiltro.checkedRadioButtonId) {
                R.id.radioCreditos -> transacaoDAO.buscarPorTipo("CREDITO")
                R.id.radioDebitos -> transacaoDAO.buscarPorTipo("DEBITO")
                else -> transacaoDAO.buscarTodas()
            }
        }
        adapter.atualizarLista(listaFiltrada)
        atualizarResumo()
    }

    private fun atualizarResumo() {
        val saldo = transacaoDAO.obterSaldo()
        textViewSaldo.text = "Saldo: R$ ${"%.2f".format(saldo)}"
    }

    override fun onResume() {
        super.onResume()
        filtrar()
    }
}
