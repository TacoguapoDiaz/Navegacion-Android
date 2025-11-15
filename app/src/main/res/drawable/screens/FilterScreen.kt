package drawable.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moverentals2.R

@Composable
fun FilterScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Cabecera: icono de back + título centrado usando Box para centrar el título exactamente
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = stringResource(R.string.car_details_back))
            }
            Text(stringResource(R.string.back_to_filters), fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.align(Alignment.Center))
        }

        LazyColumn(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                FilterSection(iconRes = R.drawable.ic_star, title = stringResource(R.string.popular_filters)) {
                    FilterCheckbox(label = stringResource(R.string.auto_transmission))
                    FilterCheckbox(label = stringResource(R.string.medium_cars))
                    FilterCheckbox(label = stringResource(R.string.five_stars))
                }
            }
            item {
                FilterSection(iconRes = R.drawable.ic_check, title = stringResource(R.string.special_offers)) {
                    FilterCheckbox(label = stringResource(R.string.discount))
                    FilterCheckbox(label = stringResource(R.string.instant_confirmation))
                }
            }
            item {
                FilterSection(iconRes = R.drawable.ic_tag, title = stringResource(R.string.price_range)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PriceBox(price = "650mx", isMin = true)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("//", fontSize = 24.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        PriceBox(price = "1500mxn", isMin = false)
                    }
                }
            }
            item {
                FilterSection(iconRes = R.drawable.ic_car, title = stringResource(R.string.transmission_type)) {
                    FilterCheckbox(label = stringResource(R.string.auto_transmission))
                    FilterCheckbox(label = stringResource(R.string.manual_transmission))
                }
            }
            item {
                FilterSection(iconRes = R.drawable.ic_door, title = stringResource(R.string.number_of_doors)) {
                    FilterCheckbox(label = stringResource(R.string.zero_doors))
                    FilterCheckbox(label = stringResource(R.string.two_doors))
                    FilterCheckbox(label = stringResource(R.string.four_doors))
                }
            }
        }
    }
}

@Composable
fun FilterSection(iconRes: Int, title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Column(content = content)
    }
}

@Composable
fun FilterCheckbox(label: String) {
    var checked by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = { checked = it })
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}

@Composable
fun PriceBox(price: String, isMin: Boolean) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isMin) Color(0xFF0096C7) else Color(0xFFD90429),
        contentColor = Color.White
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(if (isMin) stringResource(R.string.minimum) else stringResource(R.string.maximum))
            Text(price, fontWeight = FontWeight.Bold)
        }
    }
}
