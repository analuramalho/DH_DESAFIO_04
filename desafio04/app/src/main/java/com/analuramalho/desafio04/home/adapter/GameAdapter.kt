package com.analuramalho.desafio04.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.analuramalho.desafio04.R
import com.analuramalho.desafio04.home.model.GameModel

class GameAdapter (
    private val dataSet: List<GameModel>,
    private val clickListener: (GameModel) -> Unit
) : RecyclerView.Adapter<GameViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_list_item, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val dsPosition = dataSet[position]
        holder.bind(dsPosition)
        holder.itemView.setOnClickListener{clickListener(dsPosition)}
    }

    override fun getItemCount()=dataSet.size
}