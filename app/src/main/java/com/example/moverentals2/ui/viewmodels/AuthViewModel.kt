package com.example.moverentals2.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore // <-- 1. IMPORTACIÓN NUEVA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class UserData(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val userName: String = "",
    val email: String = ""
)

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Authenticating : AuthState()
    data class Authenticated(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val user = auth.currentUser
        _authState.value = if (user != null) AuthState.Authenticated(user.uid) else AuthState.Unauthenticated
    }


    fun signUp(email: String, password: String, firstName: String, lastName: String, userName: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Authenticating
            try {

                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user

                if (firebaseUser != null) {

                    val userData = UserData(
                        userId = firebaseUser.uid,
                        firstName = firstName,
                        lastName = lastName,
                        userName = userName,
                        email = email
                    )
                    saveUserData(userData)


                    _authState.value = AuthState.Authenticated(firebaseUser.uid)
                } else {
                    _authState.value = AuthState.Error("Error: No se pudo crear el usuario.")
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is FirebaseAuthUserCollisionException -> "El correo electrónico ya está en uso."
                    is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil (mínimo 6 caracteres)."
                    else -> e.localizedMessage ?: "Ocurrió un error desconocido."
                }
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }

    private suspend fun saveUserData(userData: UserData) {
        try {

            db.collection("users").document(userData.userId).set(userData).await()
            Log.d("AuthViewModel", "Datos de usuario guardados en Firestore con éxito.")
        } catch (e: Exception) {

            Log.e("AuthViewModel", "Error al guardar datos en Firestore", e)
            _authState.value = AuthState.Error("Error al guardar la información del perfil.")
        }
    }


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Authenticating
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = if (result.user != null) AuthState.Authenticated(result.user!!.uid) else AuthState.Error("Error: No se pudo iniciar sesión.")
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Correo o contraseña incorrectos.")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun resetAuthState() {
        _authState.value = AuthState.Unauthenticated
    }
}
