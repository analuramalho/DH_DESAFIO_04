package com.analuramalho.desafio04.home.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.analuramalho.desafio04.R
import com.analuramalho.desafio04.home.model.GameModel
import com.squareup.picasso.Picasso

class GameViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

    private val imgGame =view.findViewById<ImageView>(R.id.img_Home)
    private val textName = view.findViewById<TextView>(R.id.txtnameGame_Home)
    private val textYear = view.findViewById<TextView>(R.id.txtyearGame_Home)

    fun bind(gameModel: GameModel){

        if(gameModel.imgUrl.isNotEmpty()){
            Picasso.get().load(gameModel.imgUrl).into(imgGame)
        }
        textName.text=gameModel.name
        textYear.text=gameModel.createAt.toString()
    }
}