package com.example.denfriebible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.Text
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.denfriebible.ui.theme.DenFrieBibleTheme
import com.example.denfriebible.ui.components.DenFrieBibleTopBar
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.denfriebible.data.getAllBooks
import com.example.denfriebible.data.getBookByChapter
import com.example.denfriebible.data.getChaptersByAbbreviation
import com.fasterxml.jackson.annotation.JsonProperty


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DFBApp()
        }
    }
}

@Composable
fun DFBApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    val currentScreen =
        DenFrieBibleTabRowScreens.find { it.route == currentDestination?.route } ?: GetBook
    DenFrieBibleTheme {

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

            }
        }
        Scaffold(topBar = {
            DenFrieBibleTopBar(
                allScreens = DenFrieBibleTabRowScreens,
                currentScreen = currentScreen,
                navController = navController
            )
        }


        ) { innerPadding ->
            DenFrieBibleNavHost(
                navController = navController, modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

/*@Composable
fun Menu(){
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val contextForToast = LocalContext.current.applicationContext
    TopAppBar(

        title = { Text("Den Frie Bibel") },

        actions = {
            /*IconButton(onClick = { Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT).show() }) {
                Icon(Icons.Default.Favorite, "")
            }*/



            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert, "")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {

                DropdownMenuItem(onClick = {
                    //navController.navigate("defaultView")
                    showMenu = false }) {
                    Text(text = "Home")
                }
                DropdownMenuItem(onClick = {
                    Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
                    showMenu = false}) {
                    Text(text = "Settings")
                }


            }


        }
    )
}*/
@Composable
fun GetBook(navController: NavController) {
    val context = LocalContext.current
    val listedBooks = getAllBooks(context)
    val book = listedBooks.books
    Text(text = "Vælg en bog")
    Spacer(modifier = Modifier.height(20.dp))
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier.padding(horizontal = 2.dp, vertical = 46.dp)
    ) {

        for (i in book) {
            items(1) {
                Button(modifier = Modifier.padding(5.dp), onClick = {
                    navController.navigate("book/${i.abbreviation}")
                }

                ) {
                    Text(text = i.name)
                }
            }
        }
    }
}

@Composable
fun GetChapter(navController: NavController, abbreviation: String) {
    val context = LocalContext.current
    val abbreviationList = getChaptersByAbbreviation(context = context, abbreviation = abbreviation)

    Text(text = abbreviation, modifier = Modifier.fillMaxWidth())
    LazyVerticalGrid(
        columns = GridCells.Adaptive(61.dp), modifier = Modifier.padding(0.dp)
    ) {

        items(abbreviationList.count()) { bookList ->
            Button(onClick = {
                navController.navigate("book/${abbreviation}/${abbreviationList[bookList]}")
            }

            ) {
                Text(text = "${abbreviationList[bookList]}")
            }
        }

    }

}

@Composable
fun GetText(
    abbreviation: String,
    number: String,
    navController: NavController,
    modifier: Modifier = Modifier
        .fillMaxSize()
        .wrapContentSize()
) {
    val fileName = "$abbreviation/$number.json"
    val context = LocalContext.current
    val book = getBookByChapter(context, fileName)
    var chapnumber = 0
    var numberTilte = 0
    val lastIndex = book.titles.lastIndex
    var nextCapter = 0
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(5))
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        Text(
            text = "${book.book}, Kapitel ${book.chapter}",
            fontSize = 25.sp,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
        book.verses.forEach {
            if (book.titles.isNotEmpty()) {
                if (nextCapter == chapnumber) {
                    //fejl med der ikke er titler på nogle af json filerne :)))
                    Text(
                        text = book.titles[numberTilte].text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (book.titles[lastIndex].verse == chapnumber + 1) {
                        nextCapter = 0
                    } else {
                        nextCapter = book.titles[numberTilte + 1].verse - 1
                        numberTilte++
                    }
                }
            }
            chapnumber++
            Text(

                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 8.sp,
                            baselineShift = BaselineShift.Superscript,
                            color = Color.Black
                        )
                    ) {
                        append(chapnumber.toString())
                    }
                    append(it.text)
                }, color = Color.Black
            )
        }
    }
}

data class ListedBooks(
    @JsonProperty("books") val books: List<Books>,
)

data class Books(
    val name: String,
    val abbreviation: String,
    val chapters: Int,
)

data class Book(
    val book: String,
    val abbreviation: String,
    val chapter: Int,
    val version: Long,
    val verses: List<Verses>,
    val titles: List<Title>,
    val footnotes: List<Footnote>? = null,
)

data class Title(
    val text: String,
    val verse: Int,
)

data class Footnote(
    val text: String? = null,
    val type: String? = null,
    val verse: Int? = null,
    val position: Int? = null,
)

data class Verses(
    val text: String,
    val number: Int,
)