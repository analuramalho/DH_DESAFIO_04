package com.analuramalho.desafio04.registerGame.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.analuramalho.desafio04.R
import com.analuramalho.desafio04.detailsGame.view.DetailsGameFragment
import com.analuramalho.desafio04.home.model.GameModel
import com.analuramalho.desafio04.home.view.GameFragment
import com.analuramalho.desafio04.registerGame.viewmodel.RegisterGameViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.lang.System.currentTimeMillis

class RegisterGameFragment : Fragment(), View.OnClickListener {

    private lateinit var _auth: FirebaseAuth
    private lateinit var _view: View
    private lateinit var _gameModel: GameModel
    private lateinit var database: DatabaseReference
    private lateinit var _navController: NavController
    private var imgUri: Uri? = null
    private var imgUrlStorage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_register_game, container, false)
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _auth = Firebase.auth
        val currentUser = _auth.currentUser

        database = Firebase.database.reference.child("users")
            .child(currentUser!!.uid).child("games")

        _navController = Navigation.findNavController(_view)


        val id = arguments?.getString(DetailsGameFragment.ID)
        val image = arguments?.getString(DetailsGameFragment.IMGURL)
        val name = arguments?.getString(DetailsGameFragment.NAME)
        val description = arguments?.getString(DetailsGameFragment.DESCRIPTION)
        val createAt = arguments?.getInt(DetailsGameFragment.CREATEDAT)

        _gameModel = GameModel(
            id ?: "",
            name ?: "",
            description ?: "",
            createAt ?: -1,
            image ?: ""
        )

        setData()
        bindEvents()
    }

    private fun setData() {
        if (_gameModel.id.isNotEmpty()) {
            val txtName = _view.findViewById<TextInputLayout>(R.id.textName_RegisterGame)
            txtName.editText?.setText(_gameModel.name)

            val txtCreatedAt = _view.findViewById<TextInputLayout>(R.id.textCreatedAt_RegisterGame)
            txtCreatedAt.editText?.setText(_gameModel.createAt.toString())

            val txtDesc = _view.findViewById<TextInputLayout>(R.id.textDescription_RegisterGame)
            txtDesc.editText?.setText(_gameModel.description)

            val imgView = _view.findViewById<ImageView>(R.id.imgGame_RegisterGame)

            if (_gameModel.imgUrl.isNotEmpty()) {
                imgUrlStorage = _gameModel.imgUrl


                Picasso.get().load(_gameModel.imgUrl).transform(CropCircleTransformation()).into(imgView)
            }
        }
    }

    private fun bindEvents() {
        val btnSave = _view.findViewById<Button>(R.id.btnSave_RegisterGame)
        btnSave.setOnClickListener(this)

        val imgView = _view.findViewById<ImageView>(R.id.imgGame_RegisterGame)
        imgView.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.btnSave_RegisterGame -> saveGame()
                R.id.imgGame_RegisterGame -> searchImage()
            }
        }
    }


    private fun searchImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CONTENT_REQUEST_CODE)
    }

    private fun saveGame() {

        val txtName = _view.findViewById<TextInputLayout>(R.id.textName_RegisterGame)
        val name = txtName.editText?.text.toString().trim()

        val txtCreatedAt = _view.findViewById<TextInputLayout>(R.id.textCreatedAt_RegisterGame)
        val createdAt = txtCreatedAt.editText?.text.toString().trim()

        val txtDescription = _view.findViewById<TextInputLayout>(R.id.textDescription_RegisterGame)
        val desc = txtDescription.editText?.text.toString().trim()

        txtName.error = null
        txtCreatedAt.error = null
        txtDescription.error = null

        when {
            name.isBlank() -> {
                txtName.error = getString(R.string.nomegame_vazio)
            }
            createdAt.isBlank() -> {
                txtCreatedAt.error = getString(R.string.createAt_vazio)
            }
            desc.isBlank() -> {
                txtDescription.error = getString(R.string.descricao_vazia)
            }
            else -> {
                if (_gameModel.id.isNotEmpty()) {
                    updateGame(
                        name,
                        desc,
                        createdAt,
                        imgUrlStorage
                    )
                } else {
                    insertGame(
                        name,
                        desc,
                        createdAt,
                        imgUrlStorage
                    )
                }
            }
        }
    }

    private fun insertGame(name: String, desc: String, createdAt: String, imgUrl: String) {
        val newGame = database.push()

        newGame.setValue(
            GameModel(
                newGame.key!!,
                name,
                desc,
                createdAt.toInt(),
                imgUrl
            )
        ).addOnSuccessListener {
            _navController.navigate(R.id.gameFragment)
        }.addOnFailureListener {
            Snackbar.make(_view, "Falha ao cadastrar o jogo.", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun updateGame(name: String, desc: String, createdAt: String, imgUrl: String) {
        val uid = _gameModel.id

        database.child(uid).setValue(
            GameModel(
                uid,
                name,
                desc,
                createdAt.toInt(),
                imgUrl
            )
        ).addOnSuccessListener {
            _navController.navigate(R.id.gameFragment)
        }.addOnFailureListener {
            Snackbar.make(_view, "Falha ao atualizar o jogo o jogo.", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            imgUri = data?.data

            sendImgToStorage()
        }
    }

    private fun sendImgToStorage() {
        imgUri?.run {
            val firebase = FirebaseStorage.getInstance()
            val storage = firebase.getReference("gameImages")

            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                _view.context.contentResolver.getType(
                    this
                )
            )
            val fileReference = storage.child("${currentTimeMillis()}.${extension}")
            fileReference.putFile(this)
                .addOnSuccessListener {

                    fileReference.downloadUrl.addOnSuccessListener {
                        imgUrlStorage = it.toString()
                        Log.d("GAME_FORM_FRAGMENT", "Image upload: success - $imgUrlStorage")

                        val imgView = _view.findViewById<ImageView>(R.id.imgGame_RegisterGame)

                        Picasso.get().load(imgUrlStorage).transform(CropCircleTransformation()).into(imgView)

                    }.addOnFailureListener {
                        imgUrlStorage = ""
                        Log.d("GAME_FORM_FRAGMENT", "Image download: failure - ${it.message}")
                    }

                }
                .addOnFailureListener {
                    Log.d("GAME_FORM_FRAGMENT", "Image upload: error - ${it.message}")
                }
        }
    }

    companion object {
        const val CONTENT_REQUEST_CODE = 1
    }

}