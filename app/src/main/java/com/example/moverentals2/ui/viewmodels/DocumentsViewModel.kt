package com.example.moverentals2.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.moverentals2.ui.viewmodels.User



data class DocumentsUiState(
    val licenseUrl: String? = null,
    val idCardUrl: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class DocumentsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DocumentsUiState())
    val uiState = _uiState.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val userId: String
        get() = auth.currentUser?.uid.orEmpty()

    init {
        loadUserDocuments()
    }

    private fun loadUserDocuments() {
        if (userId.isEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val userDoc = firestore.collection("users").document(userId).get().await()
                val user = userDoc.toObject(User::class.java)
                _uiState.update {
                    it.copy(
                        licenseUrl = user?.licenseUrl,
                        idCardUrl = user?.idDocumentUrl,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar documentos: ${e.message}") }
            }
        }
    }

    fun uploadDocument(imageUri: Uri, documentType: DocumentType) {
        if (userId.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Usuario no autenticado.") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {

                val storagePath = "users/$userId/documents/${documentType.fileName}"
                val storageRef = storage.reference.child(storagePath)
                storageRef.putFile(imageUri).await()


                val downloadUrl = storageRef.downloadUrl.await().toString()


                val fieldToUpdate = documentType.firestoreField
                firestore.collection("users").document(userId).update(fieldToUpdate, downloadUrl).await()


                _uiState.update {
                    val newState = when(documentType) {
                        DocumentType.LICENSE -> it.copy(licenseUrl = downloadUrl)
                        DocumentType.ID_CARD -> it.copy(idCardUrl = downloadUrl)
                    }
                    newState.copy(isLoading = false, successMessage = "${documentType.displayName} actualizado.")
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al subir documento: ${e.message}") }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}

enum class DocumentType(val firestoreField: String, val fileName: String, val displayName: String) {
    LICENSE("licenseUrl", "license.jpg", "Licencia"),
    ID_CARD("idCardUrl", "id_card.jpg", "Credencial")
}
