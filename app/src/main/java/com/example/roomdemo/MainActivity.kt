package com.example.roomdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.roomdemo.ui.theme.RoomDemoTheme
import androidx.compose.runtime.livedata.observeAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: MainViewModel = MainViewModelFactory(
                        LocalContext.current.applicationContext as Application
                    ).create(MainViewModel::class.java)

                    ScreenSetup(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

// ==========================================
// ГЛАВНЫЙ ЭКРАН (ScreenSetup)
// ==========================================
@Composable
fun ScreenSetup(modifier: Modifier = Modifier, viewModel: MainViewModel) {

    // Состояния полей ввода
    var productName by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var searching by remember { mutableStateOf(false) }

    // Обработчики изменения текста
    val onProductNameChange = { text: String -> productName = text }
    val onQuantityTextChange = { text: String -> productQuantity = text }

    // Наблюдение за данными из ViewModel
    val allProducts by viewModel.allProducts.observeAsState(initial = emptyList())
    val searchResults by viewModel.searchResults.observeAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ─── ПОЛЯ ВВОДА ───
        CustomTextField(
            title = "Product Name",
            textState = productName,
            onTextChange = onProductNameChange,
            keyboardType = KeyboardType.Text
        )

        CustomTextField(
            title = "Quantity",
            textState = productQuantity,
            onTextChange = onQuantityTextChange,
            keyboardType = KeyboardType.Number
        )

        // ─── КНОПКИ УПРАВЛЕНИЯ ───
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                if (productQuantity.isNotEmpty()) {
                    viewModel.insertProduct(
                        Product(
                            productName = productName,
                            quantity = productQuantity.toInt()
                        )
                    )
                    searching = false
                }
            }) {
                Text("Add")
            }

            Button(onClick = {
                searching = true
                viewModel.findProduct(productName)
            }) {
                Text("Search")
            }

            Button(onClick = {
                viewModel.deleteProduct(productName)
            }) {
                Text("Delete")
            }

            Button(onClick = {
                productName = ""
                productQuantity = ""
                searching = false
            }) {
                Text("Clear")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ─── ТАБЛИЦА ТОВАРОВ (LazyColumn) ───
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Заголовок таблицы
            item {
                TitleRow(head1 = "ID", head2 = "Product", head3 = "Quantity")
            }

            // Данные: поиск или все товары
            val itemsList = if (searching) searchResults else allProducts

            items(itemsList) { product ->
                ProductRow(
                    id = product.id,
                    name = product.productName,
                    quantity = product.quantity
                )
            }
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================
@Preview(showBackground = true)
@Composable
fun RoomDemoPreview() {
    RoomDemoTheme {
        // Preview требует контекст, поэтому упрощенно:
        Text("RoomDemo Preview")
    }
}