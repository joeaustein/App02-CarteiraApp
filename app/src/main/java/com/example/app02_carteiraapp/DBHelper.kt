package com.example.app02_carteiraapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "carteira.db"
        const val DATABASE_VERSION = 1
        const val TABELA_TRANSACOES = "transacoes"
        const val COLUNA_ID = "id"
        const val COLUNA_VALOR = "valor"
        const val COLUNA_TIPO = "tipo"
        const val COLUNA_DESCRICAO = "descricao"
        const val COLUNA_DATA = "data"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val criarTabela = """
            CREATE TABLE $TABELA_TRANSACOES (
                $COLUNA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUNA_VALOR REAL NOT NULL,
                $COLUNA_TIPO TEXT NOT NULL,
                $COLUNA_DESCRICAO TEXT NOT NULL,
                $COLUNA_DATA TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(criarTabela)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABELA_TRANSACOES")
        onCreate(db)
    }
}

fun obterDataHoraAtual(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date())
}
