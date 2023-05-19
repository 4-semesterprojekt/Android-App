package com.example.denfriebible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.denfriebible.ui.components.DenFrieBibleTopBar
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.denfriebible.data.getAllBooks
import com.example.denfriebible.data.getBookByChapter
import com.example.denfriebible.data.getChaptersByAbbreviation
import com.example.denfriebible.ui.theme.*
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
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
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

@Composable
fun GetBook(navController: NavController) {
    val context = LocalContext.current
    val listedBooks = getAllBooks(context)
    val book = listedBooks.books
    LazyVerticalGrid(
        columns = GridCells.Adaptive(145.dp),
        modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp)
    ) {

        for (i in book) {
            items(1) {
                Button(
                    modifier = Modifier
                        .padding(5.dp)
                        .height(60.dp), onClick = {
                        navController.navigate("book/${i.abbreviation}")
                    }

                ) {
                    Text(
                        text = i.name,
                        textAlign = TextAlign.Center,
                        style = replyTypography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun GetChapter(navController: NavController, abbreviation: String) {
    val context = LocalContext.current
    val listedBooks = getChaptersByAbbreviation(context, abbreviation)
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(5))
            .fillMaxWidth()
            .padding(horizontal = 15.dp)

    ) {
        Text(
            text = listedBooks.name,
            style = replyTypography.headlineLarge,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(80.dp), modifier = Modifier.padding(10.dp)
        ) {
            items(listedBooks.chapters.count()) { bookList ->
                var tranlationLevelBTNColor = Color.Black
                var tranlationLevelFontColor = Color.White
                //var tranlationLevel = "fejl"
                if (listedBooks.chapters[bookList] == 0) {
                    tranlationLevelBTNColor = Color.Red
                    //tranlationLevel = "Ikke begyndt"
                } else if (listedBooks.chapters[bookList] < 50) {
                    tranlationLevelBTNColor = Color.Gray
                    //tranlationLevel = "Ufuldstændig"
                } else if (listedBooks.chapters[bookList] < 75) {
                    tranlationLevelBTNColor = Color.Yellow
                    tranlationLevelFontColor = Color.Black
                    //tranlationLevel = "Rå oversættelse"
                } else if (listedBooks.chapters[bookList] < 100) {
                    tranlationLevelBTNColor = Color.Gray
                    //tranlationLevel = "Delvis færdig"
                } else if (listedBooks.chapters[bookList] == 100) {
                    tranlationLevelBTNColor = Color.Green
                    //tranlationLevel = "Færdig"
                }

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tranlationLevelBTNColor
                    ),
                    onClick = {
                        if (listedBooks.chapters[bookList] != 0)
                            navController.navigate("book/${abbreviation}/${bookList + 1}")
                    }, modifier = Modifier.padding(5.dp)

                ) {
                    Text(
                        text = "${bookList + 1}",
                        textAlign = TextAlign.Center,
                        style = replyTypography.bodyMedium,
                        color = tranlationLevelFontColor
                    )

                }
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
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(5))
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .verticalScroll(scrollState)

    ) {
        Text(
            text = "${book.book}, Kapitel ${book.chapter}",
            style = replyTypography.headlineLarge,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)

        )
        book.verses.forEach {
            if (it.title != null){
                Text(
                    text = it.title,
                    style = replyTypography.headlineMedium
                )
            }

            //hvis vers har fodnote tekst split på pos
                Text(

                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 8.sp,
                                baselineShift = BaselineShift.Superscript,
                                color = Color.Black)
                        ) {
                            append(it.number.toString())
                        }
                        append(it.text)
                    }, color = Color.Black,
                    style = replyTypography.bodyMedium
                )


            //hvis det er et vers udskriv title
            //hvis det er et vers udskriv vers

        }
        Spacer(Modifier.width(200.dp))
    }
}

data class ListedBooks(
    @JsonProperty("books") val books: List<Books>,
)

data class Books(
    val name: String,
    val abbreviation: String,
    val chapters: List<Int>,
    val verses: List<Int>,
)

data class Book(
    val book: String,
    val abbreviation: String,
    val chapter: Int,
    val translation: Int,
    val contributors: List<Contributor>,
    val verses: List<Verse>,
)
data class Contributor(
    val name: String,
    val type: String,
)
data class Verse(
    val text: String,
    val title: String?,
    val number: Int,
    val version: Long,
    val footnotes: List<Footnote>?,
)

data class Footnote(
    val text: String,
    val type: String,
    val position: Int,
)