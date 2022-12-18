package com.nszalas.timefulness.domain.usecase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class GetCredentialFromAccountUseCase @Inject constructor() {
    operator fun invoke(account: GoogleSignInAccount): AuthCredential =
        GoogleAuthProvider.getCredential(account.idToken, null)
}