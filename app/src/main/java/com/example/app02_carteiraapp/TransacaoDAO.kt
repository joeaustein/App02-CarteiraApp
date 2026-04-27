package com.example.app02_carteiraapp

import android.content.ContentValues
import android.content.Context

class TransacaoDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    fun adicionar(transacao: Transacao): Long {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("valor", transacao.valor)
            put("tipo", transacao.tipo)
            put("descricao", transacao.descricao)
            put("data", transacao.data)
        }
        return db.insert("transacoes", null, valores).also { db.close() }
    }

    fun atualizar(transacao: Transacao): Int {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("valor", transacao.valor)
            put("tipo", transacao.tipo)
            put("descricao", transacao.descricao)
            put("data", transacao.data)
        }
        return db.update("transacoes", valores, "id = ?", arrayOf(transacao.id.toString())).also { db.close() }
    }

    fun excluir(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete("transacoes", "id = ?", arrayOf(id.toString())).also { db.close() }
    }

    fun buscarTodas(): List<Transacao> = consultar(null, null)

    fun buscarPorTipo(tipo: String): List<Transacao> = consultar("tipo = ?", arrayOf(tipo))

    fun buscarPorDescricao(busca: String): List<Transacao> = consultar("descricao LIKE ?", arrayOf("%$busca%"))

    fun obterSaldo(): Double {
        val db = dbHelper.readableDatabase
        var saldo = 0.0
        
        // Receitas
        db.rawQuery("SELECT SUM(valor) FROM transacoes WHERE tipo = 'CREDITO'", null).use {
            if (it.moveToFirst()) saldo += it.getDouble(0)
        }
        // Despesas
        db.rawQuery("SELECT SUM(valor) FROM transacoes WHERE tipo = 'DEBITO'", null).use {
            if (it.moveToFirst()) saldo -= it.getDouble(0)
        }
        
        db.close()
        return saldo
    }

    private fun consultar(selecao: String?, argumentos: Array<String>?): List<Transacao> {
        val lista = mutableListOf<Transacao>()
        val db = dbHelper.readableDatabase
        val cursor = db.query("transacoes", null, selecao, argumentos, null, null, "data DESC")

        cursor.use {
            while (it.moveToNext()) {
                lista.add(Transacao(
                    it.getLong(it.getColumnIndexOrThrow("id")),
                    it.getDouble(it.getColumnIndexOrThrow("valor")),
                    it.getString(it.getColumnIndexOrThrow("tipo")),
                    it.getString(it.getColumnIndexOrThrow("descricao")),
                    it.getString(it.getColumnIndexOrThrow("data"))
                ))
            }
        }
        db.close()
        return lista
    }
}
