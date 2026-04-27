package com.example.app02_carteiraapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCadastro = findViewById<Button>(R.id.btnCadastro)
        val btnExtrato = findViewById<Button>(R.id.btnExtrato)
        val btnSair = findViewById<Button>(R.id.btnSair)

        //operações de cadastro
        btnCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        //acessa extrato
        btnExtrato.setOnClickListener {
            val intent = Intent(this, ExtratoActivity::class.java)
            startActivity(intent)
        }

        //sai do app
        btnSair.setOnClickListener {
            finishAffinity()
        }
    }
}