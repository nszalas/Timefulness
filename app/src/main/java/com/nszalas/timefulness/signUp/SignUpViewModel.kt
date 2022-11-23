package com.nszalas.timefulness.signUp

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (Task<AuthResult>) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                result(it)
            }
    }
}