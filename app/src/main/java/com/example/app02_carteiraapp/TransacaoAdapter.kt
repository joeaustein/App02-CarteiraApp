package com.example.app02_carteiraapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransacaoAdapter(
    private var listaTransacoes: List<Transacao>,
    private val aoClicarNoItem: (Transacao) -> Unit
) : RecyclerView.Adapter<TransacaoAdapter.TransacaoViewHolder>() {

    class TransacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDescricao: TextView = itemView.findViewById(R.id.textViewDescricao)
        val txtValor: TextView = itemView.findViewById(R.id.textViewValor)
        val txtData: TextView = itemView.findViewById(R.id.textViewData)
        val txtTipo: TextView = itemView.findViewById(R.id.textViewTipo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransacaoViewHolder, position: Int) {
        val transacao = listaTransacoes[position]
        holder.txtDescricao.text = transacao.descricao
        holder.txtValor.text = "R$ ${"%.2f".format(transacao.valor)}"
        holder.txtData.text = transacao.data
        holder.txtTipo.text = if (transacao.tipo == "CREDITO") "Crédito" else "Débito"

        holder.itemView.setOnClickListener {
            aoClicarNoItem(transacao)
        }
    }

    override fun getItemCount() = listaTransacoes.size

    fun atualizarLista(novaLista: List<Transacao>) {
        listaTransacoes = novaLista
        notifyDataSetChanged()
    }
}
