package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    // Panggil App (yang sekarang sudah di top-level)
                    App(
                        navController = navController
                    )
                }
            }
        }
    }
}

data class Student(
    var name: String
)

// PERBAIKAN: Fungsi Home sekarang di top-level
@Composable
fun Home(navigateFromHomeToResult: (String) -> Unit) {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    val inputField = remember { mutableStateOf(Student("")) }

    // PERBAIKAN: Memperbaiki cara memanggil HomeContent
    HomeContent(
        listData = listData,
        inputField = inputField.value,
        onInputValueChange = { newName ->
            inputField.value = inputField.value.copy(name = newName)
        },
        onButtonClick = {
            // Logika untuk menambah item
            listData.add(inputField.value)
            // Reset input field
            inputField.value = Student("")
        },
        navigateFromHomeToResult = {
            // Logika untuk navigasi
            navigateFromHomeToResult(listData.toList().toString())
        }
    )
}

// PERBAIKAN: Fungsi HomeContent sekarang di top-level
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit // Parameter ini sebelumnya salah passing
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundTitleText(
                    text = stringResource(
                        id = R.string.enter_item
                    )
                )
                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = {
                        onInputValueChange(it)
                    }
                )
                // PERBAIKAN: Tambahkan import untuk Row
                Row {
                    PrimaryTextButton(
                        text = stringResource(
                            id =
                                R.string.button_click
                        )
                    ) {
                        onButtonClick() // Panggil lambda onButtonClick
                    }
                    PrimaryTextButton(
                        text = stringResource(
                            id =
                                R.string.button_navigate
                        )
                    ) {
                        navigateFromHomeToResult() // Panggil lambda navigate
                    }
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
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

// PERBAIKAN: Fungsi App sekarang di top-level
@Composable
fun App(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home { listDataString ->
                navController.navigate(
                    "resultContent/?listData=$listDataString"
                )
            }
        }
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            // PERBAIKAN: Ambil argumen dan panggil ResultContent
            val listDataString = backStackEntry.arguments?.getString("listData") ?: ""
            ResultContent(listData = listDataString)
        }
    }
}

// PERBAIKAN: Fungsi ResultContent sekarang di top-level
@Composable
fun ResultContent(listData: String) {
    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnBackgroundItemText(text = listData)
    }
}

// PERBAIKAN: Fungsi PreviewHome sekarang di top-level
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        // PERBAIKAN: Berikan lambda kosong untuk parameter navigasi
        Home(navigateFromHomeToResult = {})
    }
}