package com.example.moverentals2.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moverentals2.R
import com.example.moverentals2.ui.viewmodels.UploadCarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadCarDetailsScreen(
    navController: NavController,
    uploadCarViewModel: UploadCarViewModel = viewModel()
) {
    val uiState by uploadCarViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Efectos
    LaunchedEffect(key1 = uiState.uploadSuccess) {
        if (uiState.uploadSuccess) {
            Toast.makeText(context, "Car published successfully!", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }
    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            uploadCarViewModel.dismissError()
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) uploadCarViewModel.onImageSelected(uri)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Car Details", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // --- IMAGEN ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.selectedImageUri != null) {
                        AsyncImage(
                            model = uiState.selectedImageUri,
                            contentDescription = "Selected car image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.placeholder_upload_car),
                            contentDescription = "Upload car image placeholder",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Text("Click to select an image", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Car Specifications", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                Spacer(modifier = Modifier.height(8.dp))

                // --- DROPDOWNS ---
                UploadDropdownField(label = "Brand", selectedValue = uiState.selectedBrand, options = uiState.brandOptions, onOptionSelected = uploadCarViewModel::onBrandSelected)
                UploadDropdownField(label = "Model", selectedValue = uiState.selectedModel, options = uiState.modelOptions, onOptionSelected = uploadCarViewModel::onModelSelected)
                UploadDropdownField(label = "Year", selectedValue = uiState.selectedYear, options = uiState.yearOptions, onOptionSelected = uploadCarViewModel::onYearSelected)
                UploadDropdownField(label = "Body Type", selectedValue = uiState.selectedBody, options = uiState.bodyOptions, onOptionSelected = uploadCarViewModel::onBodySelected)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        UploadDropdownField(label = "Fuel", selectedValue = uiState.selectedFuel, options = uiState.fuelOptions, onOptionSelected = uploadCarViewModel::onFuelSelected)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        UploadDropdownField(label = "Transmission", selectedValue = uiState.selectedTransmission, options = uiState.transmissionOptions, onOptionSelected = uploadCarViewModel::onTransmissionSelected)
                    }
                }

                // NUEVOS DROPDOWNS (Puertas y Asientos)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        UploadDropdownField(label = "Doors", selectedValue = uiState.selectedDoors, options = uiState.doorsOptions, onOptionSelected = uploadCarViewModel::onDoorsSelected)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        UploadDropdownField(label = "Seats", selectedValue = uiState.selectedSeats, options = uiState.seatsOptions, onOptionSelected = uploadCarViewModel::onSeatsSelected)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Rental Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                Spacer(modifier = Modifier.height(8.dp))

                // --- CAMPOS DE TEXTO ---
                OutlinedTextField(
                    value = uiState.price,
                    onValueChange = { uploadCarViewModel.onPriceChanged(it) },
                    label = { Text("Price per day ($)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.city,
                    onValueChange = { uploadCarViewModel.onCityChanged(it) },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // NUEVO: Campo de dirección exacta
                OutlinedTextField(
                    value = uiState.location,
                    onValueChange = { uploadCarViewModel.onLocationChanged(it) },
                    label = { Text("Exact Location / Address") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // NUEVO: Campo de descripción
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { uploadCarViewModel.onDescriptionChanged(it) },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- BOTÓN ---
                Button(
                    onClick = { uploadCarViewModel.publishCar() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.isUploading
                ) {
                    if (uiState.isUploading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(text = "Publish Car", fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// Componente reutilizable
@Composable
fun UploadDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { isExpanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, fontSize = 12.sp, color = Color.Gray)
                Text(text = selectedValue, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Open dropdown")
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        isExpanded = false
                    }
                )
            }
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
    }
}