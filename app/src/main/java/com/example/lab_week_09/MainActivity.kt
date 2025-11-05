package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home()
                }
            }
        }
    }
}

data class Student(
    var name: String
)

@Composable
fun Home() {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    // PERBAIKAN: 'var' diubah menjadi 'val' (sesuai warning)
    val inputField = remember { mutableStateOf(Student("")) }

    // PERBAIKAN: Panggil HomeContent di sini
    HomeContent(
        listData = listData,
        inputField = inputField.value, // Berikan .value (objek Student)
        onInputValueChange = { newName ->
            // Saat nilai berubah, update state 'inputField'
            inputField.value = inputField.value.copy(name = newName)
        },
        onButtonClick = {
            // Saat tombol diklik, tambahkan state ke list
            listData.add(inputField.value)
            // Reset input field
            inputField.value = Student("")
        }
    )
}

// PERBAIKAN: HomeContent dipindah KELUAR dari Home
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
) {
    //Here, we use LazyColumn to display a list of items lazily
    LazyColumn {
        //Here, we use item to display an item inside the LazyColumn
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        id = R.string.enter_item
                    )
                )
                TextField(
                    value = inputField.name, // Gunakan nama dari objek Student
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = {
                        // Panggil lambda yang sudah kita kirim dari Home
                        onInputValueChange(it)
                    }
                )
                Button(onClick = {
                    // Panggil lambda yang sudah kita kirim dari Home
                    onButtonClick()
                }) {
                    Text(
                        text = stringResource(
                            id = R.string.button_click
                        )
                    )
                }
            }
        }

        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item.name)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    // PERBAIKAN: Panggil Home() tanpa argumen
    LAB_WEEK_09Theme { // Tambahkan Theme agar preview-nya sesuai
        Home()
    }
}