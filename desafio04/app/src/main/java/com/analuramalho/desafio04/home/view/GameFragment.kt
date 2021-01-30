package com.analuramalho.desafio04.home.view

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.analuramalho.desafio04.R
import com.analuramalho.desafio04.home.adapter.GameAdapter
import com.analuramalho.desafio04.home.model.GameModel
import com.analuramalho.desafio04.home.repository.GameRepository
import com.analuramalho.desafio04.home.viewmodel.GameViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameFragment : Fragment(), View.OnClickListener {
    private lateinit var _viewModel: GameViewModel
    private lateinit var _view: View
    private lateinit var _navController: NavController
    private lateinit var _auth: FirebaseAuth
    private lateinit var _database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_game, container, false)
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _auth = Firebase.auth
        val currentUser = _auth.currentUser


        _database = Firebase.database.reference.child("users")
            .child(currentUser!!.uid).child("games")


        _navController = Navigation.findNavController(_view)
        _viewModel = ViewModelProvider(
            this,
            GameViewModel.GameViewModelFactory(GameRepository(_database))
        ).get(GameViewModel::class.java)

        _viewModel.games.observe(viewLifecycleOwner, Observer {
            createList(it)
        })

        _viewModel.getGames()

        bindEvents()
    }

    private fun createList(games: List<GameModel>) {
        val viewManager = GridLayoutManager(_view.context, 2)
        val recyclerView = _view.findViewById<RecyclerView>(R.id.rvHome)
        val viewAdapter = GameAdapter(games) {
            val bundle= bundleOf(
                IMGURL to it.imgUrl,
                NAME to it.name,
                DESCRIPTION to it.description,
                CREATEDAT to it.createAt,
                ID to it.id
            )
            _view.findNavController().navigate(R.id.action_gameFragment_to_detailsGameFragment,bundle)
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }


    private fun bindEvents() {
        val btnCreate = _view.findViewById<FloatingActionButton>(R.id.btnAdd_GameFragment)
        btnCreate.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id) {
                R.id.btnAdd_GameFragment -> redirectAddGame()
            }
        }
    }

    private fun redirectAddGame() {
        _navController.navigate(R.id.registerGameFragment)
    }

    companion object {
        const val IMGURL ="IMGURL"
        const val NAME ="NAME"
        const val DESCRIPTION ="DESCRIPTION"
        const val CREATEDAT ="CREATEDAT"
        const val ID ="ID"
    }
}