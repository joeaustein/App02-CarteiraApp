package com.example.app02_carteiraapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        val viewIndicator: View = itemView.findViewById(R.id.viewIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransacaoViewHolder, position: Int) {
        val transacao = listaTransacoes[position]
        val ctx = holder.itemView.context
        val isCredito = transacao.tipo == "CREDITO"

        holder.txtDescricao.text = transacao.descricao
        holder.txtData.text = transacao.data

        if (isCredito) {
            holder.txtValor.text = "+ R$ ${"%.2f".format(transacao.valor)}"
            holder.txtValor.setTextColor(ContextCompat.getColor(ctx, R.color.credito_color))
            holder.txtTipo.text = "Crédito"
            holder.txtTipo.setTextColor(ContextCompat.getColor(ctx, R.color.credito_color))
            holder.txtTipo.background = ContextCompat.getDrawable(ctx, R.drawable.bg_badge_credito)
            holder.viewIndicator.background = ContextCompat.getDrawable(ctx, R.drawable.bg_indicator_credito)
        } else {
            holder.txtValor.text = "- R$ ${"%.2f".format(transacao.valor)}"
            holder.txtValor.setTextColor(ContextCompat.getColor(ctx, R.color.debito_color))
            holder.txtTipo.text = "Débito"
            holder.txtTipo.setTextColor(ContextCompat.getColor(ctx, R.color.debito_color))
            holder.txtTipo.background = ContextCompat.getDrawable(ctx, R.drawable.bg_badge_debito)
            holder.viewIndicator.background = ContextCompat.getDrawable(ctx, R.drawable.bg_indicator_debito)
        }

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
