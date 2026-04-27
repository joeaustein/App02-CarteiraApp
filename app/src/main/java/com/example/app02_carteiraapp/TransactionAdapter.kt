package com.example.app02_carteiraapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDescricao: TextView = itemView.findViewById(R.id.textViewDescricao)
        val textViewValor: TextView = itemView.findViewById(R.id.textViewValor)
        val textViewData: TextView = itemView.findViewById(R.id.textViewData)
        val textViewTipo: TextView = itemView.findViewById(R.id.textViewTipo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.textViewDescricao.text = transaction.descricao
        holder.textViewValor.text = "R$ ${"%.2f".format(transaction.valor)}"
        holder.textViewData.text = transaction.data
        holder.textViewTipo.text = if (transaction.tipo == "CREDITO") "Crédito" else "Débito"
    }

    override fun getItemCount() = transactions.size
}
