package com.analuramalho.desafio04.auth.login.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.analuramalho.desafio04.R
import com.analuramalho.desafio04.auth.login.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var _view: View
    private lateinit var _viewModel: LoginViewModel
    private lateinit var _navController: NavController
    private lateinit var _auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_login, container, false)
        return _view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // _view = view
        _auth = Firebase.auth
        _viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        _navController = Navigation.findNavController(this._view)

        bindEvents()
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.btnCreateAccount_Login -> redirectRegister()
                R.id.btnLogin_Login -> redirectHome()
            }
        }
    }

    private fun bindEvents(){
        _view.findViewById<Button>(R.id.btnCreateAccount_Login).setOnClickListener(this)
        _view.findViewById<Button>(R.id.btnLogin_Login).setOnClickListener(this)
    }

    private fun redirectHome() {
        val txtEmail = _view.findViewById<TextInputLayout>(R.id.textEmail_Login)
        val email = txtEmail.editText?.text.toString().trim()
        val txtPassword = _view.findViewById<TextInputLayout>(R.id.textPassword_Login)
        val password = txtPassword.editText?.text.toString().trim()

        txtEmail.error = null
        txtPassword.error = null

        when {
            email.isBlank() -> {
                txtEmail.error = "Preencha o email."
            }
            password.isBlank() -> {
                txtPassword.error = "Preencha com uma senha válida."
            }
            else -> {
                activity?.let { itActivity ->
                    _auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(itActivity) { result ->
                            if (result.isSuccessful) {
                                val user = _auth.currentUser
                                Log.d(TAG, "login:success ${user?.displayName}")
                                _navController.navigate(R.id.action_loginFragment_to_mainActivity)
                            } else {
                                Log.w(TAG, "login:failure", result.exception)
                                Snackbar.make(
                                    _view,
                                    "Email ou senha incorretas!",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }
    }

    private fun redirectRegister() {
        _navController.navigate(R.id.action_loginFragment_to_registerUserFragment)
    }



    companion object {
        //fun newInstance() = LoginFragment()
        private const val TAG = "LOG_IN_FRAGMENT"
    }
}