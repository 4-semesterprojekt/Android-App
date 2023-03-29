package com.example.denfriebible

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.denfriebible.ui.theme.DenFrieBibleTheme
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.IOException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DenFrieBibleTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DefaultView(application)

                }
            }
        }
    }
}

@Composable
fun DefaultView(context: Context) {
    val scroll = rememberScrollState(0)
    Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        .verticalScroll(scroll)
        .fillMaxWidth()) {
        // TODO: Formatere teksten korrekt -- mangler kun at det hele står på en linje
        //abbreviation == bruges til kald efter API
        // TODO: Crasher hvis der er noget der er null eller mangler, fx der er ingen fodnoter i ruths bog 2
        /*var result by remember { mutableStateOf(1) }
        when (result) {
            1 -> HomePage()
            2 -> GetBook("Ruths_bog/1.json",context, Modifier)
            else -> {GetBook("Ruths_bog/2.json",context, Modifier)}
        }*/
        GetBook("Ruths_bog/1.json",context, Modifier)

        Spacer(modifier = Modifier.height(30.dp))


    }
}

@Composable
fun HomePage():Int{
    var result = 1
    Button(onClick = {result = 2},
        modifier = Modifier.height(200.dp)
            .width(200.dp)
    ) {
        Text(
            text = "test",
            fontSize = 20.sp)
    }
    return result
}
@Composable
fun GetBook(fileName: String, context: Context, modifier: Modifier = Modifier
    .fillMaxSize()
    .wrapContentSize()){
    lateinit var jsonString: String
    try {
        jsonString = context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
        println(ioException)
    }
    val mapper = ObjectMapper().registerKotlinModule()
    val book = mapper.readValue<Book>(jsonString)
    var number = 0
    var numberTilte = 0
    val lastIndex = book.titles.lastIndex
    var nextCapter = 0
    Text(text = "${book.book}, Kapitel ${book.chapter}", fontSize = 25.sp)
    book.verses.forEach{
        if (nextCapter == number){
            Text(
                text = book.titles[numberTilte].text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            if(book.titles[lastIndex].verse == number+1) {
                nextCapter = 0
            }else{
                nextCapter = book.titles[numberTilte+1].verse -1
                numberTilte++
            }
        }
        number++
        Row(
            Modifier.fillMaxWidth()
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 8.sp, baselineShift = BaselineShift.Superscript)) {
                        append(number.toString())
                    }
                    append(it)
                }
            )
        }
    }
}


data class Book(
    val book: String,
    val abbreviation: String,
    val chapter: String,
    val verses: List<String>,
    val titles: List<Title>,
    val footnotes: List<Footnote>? = null,
)

data class Title(
    val text: String,
    val verse: Int,
)

data class Footnote(
    val text: String? = null,
    val verse: Int? = null,
    val word: Int? = null,
)
