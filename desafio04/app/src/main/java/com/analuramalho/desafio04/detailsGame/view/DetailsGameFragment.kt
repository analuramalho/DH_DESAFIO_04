package com.analuramalho.desafio04.detailsGame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.analuramalho.desafio04.R
import com.analuramalho.desafio04.home.adapter.GameAdapter
import com.analuramalho.desafio04.home.model.GameModel
import com.analuramalho.desafio04.home.view.GameFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class DetailsGameFragment : Fragment(), View.OnClickListener{
    private lateinit var _view: View

    private lateinit var _navController: NavController
    private lateinit var _gameModel: GameModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_details_game, container, false)
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _navController = Navigation.findNavController(_view)

        val imgGame = _view.findViewById<ImageView>(R.id.imageCover_Details)
        val txtNameImg = _view.findViewById<TextView>(R.id.textTitleCover_Details)
        val txtName = _view.findViewById<TextView>(R.id.textTitleGame_Details)
        val txtYear = _view.findViewById<TextView>(R.id.textlancamento_Details)
        val txtDesc = _view.findViewById<TextView>(R.id.textDescriptionComic_Details)

        val id = arguments?.getString(GameFragment.ID)
        val image = arguments?.getString(GameFragment.IMGURL)
        val name = arguments?.getString(GameFragment.NAME)
        val description = arguments?.getString(GameFragment.DESCRIPTION)
        val createAt = arguments?.getInt(GameFragment.CREATEDAT)


        _gameModel = GameModel(
            id!!,
            name!!,
            description!!,
            createAt!!,
            image!!
        )

        if(_gameModel.imgUrl.isNotEmpty()){
        Picasso.get().load(image).into(imgGame)
        }
        txtNameImg.text = name
        txtName.text = name
        txtYear.text = createAt.toString()
        txtDesc.text = description

        bindEvents()
    }

    private fun bindEvents() {
        val imgGame = _view.findViewById<ImageView>(R.id.imgBack_Details)
        imgGame.setOnClickListener(this)

        val btnEdit = _view.findViewById<FloatingActionButton>(R.id.btnEdit_Details)
        btnEdit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.imgBack_Details -> returnToGameList()
                R.id.btnEdit_Details -> editGameForm()
            }
        }
    }

    private fun returnToGameList() {
        _navController.navigate(R.id.action_detailsGameFragment_to_gameFragment)
    }

    private fun editGameForm() {
        val bundle= bundleOf(
            IMGURL to _gameModel.imgUrl,
            NAME to _gameModel.name,
            DESCRIPTION to _gameModel.description,
            CREATEDAT to _gameModel.createAt,
            ID to _gameModel.id
        )
        _view.findNavController().navigate(R.id.action_detailsGameFragment_to_registerGameFragment,bundle)
    }

    companion object {
        fun newInstance() = DetailsGameFragment()
        const val IMGURL = "IMGURL"
        const val NAME = "NAME"
        const val DESCRIPTION = "DESCRIPTION"
        const val CREATEDAT = "CREATEDAT"
        const val ID = "ID"
    }


}

