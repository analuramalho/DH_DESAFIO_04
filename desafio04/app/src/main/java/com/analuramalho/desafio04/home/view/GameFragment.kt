package com.analuramalho.desafio04.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.analuramalho.desafio04.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {
    private lateinit var _view:View
    private var _navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _view = view
        _navController=findNavController()

        _view.findViewById<FloatingActionButton>(R.id.btnAdd_Home).setOnClickListener{
            redirectAddGame()
        }
    }

    private fun redirectAddGame() {
            _navController!!.navigate(R.id.action_homeFragment_to_registerGameFragment)
    }
}