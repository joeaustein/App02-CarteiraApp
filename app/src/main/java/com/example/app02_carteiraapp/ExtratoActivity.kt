package com.example.app02_carteiraapp

import android.annotation.SuppressLint
import android.os.Bundle
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
    private lateinit var adapter: TransactionAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extrato)

        dbHelper = DBHelper(this)

        recyclerView = findViewById(R.id.recyclerViewTransactions)
        textViewSaldo = findViewById(R.id.textViewSaldo)
        radioGroupFiltro = findViewById(R.id.radioGroupFiltro)

        setupRecyclerView()
        updateSaldo()


        radioGroupFiltro.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioTodos -> showAllTransactions()
                R.id.radioCreditos -> showCreditos()
                R.id.radioDebitos -> showDebitos()
            }
        }
    }

    private fun showAllTransactions() {
        val transactions = dbHelper.getAllTransactions()
        adapter = TransactionAdapter(transactions)
        recyclerView.adapter = adapter
    }

    private fun showCreditos() {
        val transactions = dbHelper.getCreditos()
        adapter = TransactionAdapter(transactions)
        recyclerView.adapter = adapter
    }

    private fun showDebitos() {
        val transactions = dbHelper.getDebitos()
        adapter = TransactionAdapter(transactions)
        recyclerView.adapter = adapter
    }

    private fun setupRecyclerView() {
        showAllTransactions()
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun updateSaldo() {
        val saldo = dbHelper.getSaldoTotal()
        textViewSaldo.text = "Saldo: R$ ${"%.2f".format(saldo)}"
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
        updateSaldo()
    }
}
