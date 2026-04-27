package com.example.app02_carteiraapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "wallet.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TRANSACTIONS = "transactions"
        private const val COLUMN_ID = "id"
        private const val COLUMN_VALOR = "valor"
        private const val COLUMN_TIPO = "tipo"
        private const val COLUMN_DESCRICAO = "descricao"
        private const val COLUMN_DATA = "data"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_TRANSACTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_VALOR REAL NOT NULL,
                $COLUMN_TIPO TEXT NOT NULL,
                $COLUMN_DESCRICAO TEXT NOT NULL,
                $COLUMN_DATA TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        onCreate(db)
    }

    fun addTransaction(transaction: Transaction): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_VALOR, transaction.valor)
            put(COLUMN_TIPO, transaction.tipo)
            put(COLUMN_DESCRICAO, transaction.descricao)
            put(COLUMN_DATA, transaction.data)
        }
        val id = db.insert(TABLE_TRANSACTIONS, null, values)
        db.close()
        return id
    }

    fun getAllTransactions(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TRANSACTIONS,
            arrayOf(COLUMN_ID, COLUMN_VALOR, COLUMN_TIPO, COLUMN_DESCRICAO, COLUMN_DATA),
            null, null, null, null, "$COLUMN_DATA DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val transaction = Transaction(
                    id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
                    valor = getDouble(getColumnIndexOrThrow(COLUMN_VALOR)),
                    tipo = getString(getColumnIndexOrThrow(COLUMN_TIPO)),
                    descricao = getString(getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                    data = getString(getColumnIndexOrThrow(COLUMN_DATA))
                )
                transactions.add(transaction)
            }
        }
        cursor.close()
        db.close()
        return transactions
    }

    fun getSaldoTotal(): Double {
        val db = readableDatabase
        var saldo = 0.0

        val cursorCredito = db.rawQuery(
            "SELECT SUM($COLUMN_VALOR) FROM $TABLE_TRANSACTIONS WHERE $COLUMN_TIPO = ?",
            arrayOf("CREDITO")
        )
        cursorCredito.use { cursor ->
            if (cursor.moveToFirst()) {
                saldo += cursor.getDouble(0)
            }
        }

        val cursorDebito = db.rawQuery(
            "SELECT SUM($COLUMN_VALOR) FROM $TABLE_TRANSACTIONS WHERE $COLUMN_TIPO = ?",
            arrayOf("DEBITO")
        )
        cursorDebito.use { cursor ->
            if (cursor.moveToFirst()) {
                saldo -= cursor.getDouble(0)
            }
        }

        db.close()
        return saldo
    }

    fun getCreditos(): List<Transaction> {
        return getTransactionsByType("CREDITO")
    }

    fun getDebitos(): List<Transaction> {
        return getTransactionsByType("DEBITO")
    }

    private fun getTransactionsByType(tipo: String): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TRANSACTIONS,
            arrayOf(COLUMN_ID, COLUMN_VALOR, COLUMN_TIPO, COLUMN_DESCRICAO, COLUMN_DATA),
            "$COLUMN_TIPO = ?",
            arrayOf(tipo),
            null, null, "$COLUMN_DATA DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val transaction = Transaction(
                    id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
                    valor = getDouble(getColumnIndexOrThrow(COLUMN_VALOR)),
                    tipo = getString(getColumnIndexOrThrow(COLUMN_TIPO)),
                    descricao = getString(getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                    data = getString(getColumnIndexOrThrow(COLUMN_DATA))
                )
                transactions.add(transaction)
            }
        }
        cursor.close()
        db.close()
        return transactions
    }
}

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date())
}
