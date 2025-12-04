package com.example.moverentals2.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moverentals2.ui.viewmodels.DocumentType
import com.example.moverentals2.ui.viewmodels.DocumentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(
    navController: NavController,
    documentsViewModel: DocumentsViewModel = viewModel()
) {
    val uiState by documentsViewModel.uiState.collectAsState()
    val context = LocalContext.current


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {

            }
        }
    )

    var documentToUpload by remember { mutableStateOf<DocumentType?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            documentToUpload?.let { docType ->
                documentsViewModel.uploadDocument(it, docType)
            }
        }
    }


    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            documentsViewModel.clearMessages()
        }
    }
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            documentsViewModel.clearMessages()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Documentos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {

                DocumentUploadCard(
                    documentName = "Licencia de Conducir",
                    imageUrl = uiState.licenseUrl,
                    onUploadClick = {
                        documentToUpload = DocumentType.LICENSE
                        launcher.launch("image/*")
                    },
                    onRemoveClick = { /* TODO */ }
                )


                DocumentUploadCard(
                    documentName = "Credencial / ID Oficial",
                    imageUrl = uiState.idCardUrl,
                    onUploadClick = {
                        documentToUpload = DocumentType.ID_CARD
                        launcher.launch("image/*")
                    },
                    onRemoveClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun DocumentUploadCard(
    documentName: String,
    imageUrl: String?,
    onUploadClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(documentName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
                    .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = documentName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.FileUpload, contentDescription = "Upload Placeholder", tint = Color.Gray, modifier = Modifier.size(48.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            val status = if (imageUrl != null) "Subido" else "No subido"
            Text("Estado: $status", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onUploadClick, modifier = Modifier.weight(1f)) { Text("Subir / Actualizar") }
                OutlinedButton(onClick = onRemoveClick, modifier = Modifier.weight(1f), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)) { Text("Quitar") }
            }
        }
    }
}
