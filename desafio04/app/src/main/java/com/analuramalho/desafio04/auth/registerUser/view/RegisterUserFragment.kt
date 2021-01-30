package com.analuramalho.desafio04.auth.registerUser.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.analuramalho.desafio04.R
import com.analuramalho.desafio04.auth.registerUser.viewmodel.RegisterUserViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterUserFragment : Fragment(), View.OnClickListener {

    private lateinit var _view: View
    private lateinit var _viewModel: RegisterUserViewModel
    private lateinit var _navController: NavController
    private lateinit var _auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_register_user, container, false)
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _auth = Firebase.auth
        _viewModel = ViewModelProvider(this).get(RegisterUserViewModel::class.java)
        _navController = Navigation.findNavController(_view)

        bindEvents()
    }


    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.btnCreateAccount_RegisterUser -> redirectLogin()
            }
        }
    }

    private fun bindEvents() {
        _view.findViewById<Button>(R.id.btnCreateAccount_RegisterUser).setOnClickListener(this)
    }


    private fun redirectLogin() {
        val txtName = _view.findViewById<TextInputLayout>(R.id.textName_RegisterUser)
        val name = txtName.editText?.text.toString().trim()
        val txtEmail = _view.findViewById<TextInputLayout>(R.id.textEmail_RegisterUser)
        val email = txtEmail.editText?.text.toString().trim()
        val txtPassword = _view.findViewById<TextInputLayout>(R.id.textPassword_RegisterUser)
        val password = txtPassword.editText?.text.toString().trim()
        val txtRepeatPassword =
            _view.findViewById<TextInputLayout>(R.id.textPasswordConfirm_RegisterUser)
        val repeatPassword = txtPassword.editText?.text.toString().trim()

        txtName.error = null
        txtEmail.error = null
        txtPassword.error = null
        txtRepeatPassword.error = null

        when {
            name.isBlank() -> {
                txtName.error = "Preencha um nome válido!"
            }
            email.isBlank() -> {
                txtEmail.error = "Preencha um endereço de email."
            }
            password.isBlank() -> {
                txtPassword.error = "Preencha uma senha válida."
            }
            password != repeatPassword -> {
                txtPassword.error = "As senhas devem ser iguais."
                txtRepeatPassword.error = "As senhas devem ser iguais."
            }
            else -> {
                activity?.let { itAct ->
                    _auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(itAct) { result ->
                            if (result.isSuccessful) {
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build()

                                _auth.currentUser!!.updateProfile(profileUpdates)

                                Toast.makeText(_view.context, "Usuário criado com sucesso! $name", Toast.LENGTH_SHORT).show()

                                _navController.navigate(R.id.action_registerUserFragment_to_mainActivity)
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", result.exception)
                                Snackbar.make(
                                    _view,
                                    "Criação de usuário falhou",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }

    companion object {
        const val TAG = "REGISTER_FRAGMENT"
    }
}