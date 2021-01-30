package com.analuramalho.desafio04.home.repository

import android.util.Log
import com.analuramalho.desafio04.home.model.GameModel
import com.analuramalho.desafio04.home.view.GameFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class GameRepository(private val databaseReference: DatabaseReference) {

    fun getGames(callback: (games: MutableList<GameModel>) -> Unit) {
        val games = databaseReference.orderByKey()
        val singleListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                callback.invoke(mutableListOf())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val mutableList = mutableListOf<GameModel>()
                snapshot.children.forEach {
                    mutableList.add(it.getValue<GameModel>()!!)
                }
                callback.invoke(mutableList)
            }
        }
        games.addListenerForSingleValueEvent(singleListener)
    }
}