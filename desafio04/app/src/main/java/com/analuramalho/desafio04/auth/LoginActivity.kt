package com.analuramalho.desafio04.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.analuramalho.desafio04.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Firebase.auth.signOut()

    }
}
