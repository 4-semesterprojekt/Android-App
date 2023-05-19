package com.example.denfriebible.data

import android.content.Context
import androidx.compose.runtime.Composable
import com.example.denfriebible.Book
import com.example.denfriebible.Books
import com.example.denfriebible.ListedBooks
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.IOException


//gets all books from books.json
@Composable
fun getAllBooks(context: Context): ListedBooks {
    lateinit var jsonString: String
    try {
        jsonString = context.assets.open("books.json")
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
        println(ioException)
    }
    val mapper = jacksonObjectMapper()
    return mapper.readValue(jsonString)

}

// gets all chapter by Abbreviation
fun getChaptersByAbbreviation(context: Context, abbreviation: String): Books {
    lateinit var jsonString: String
    try {
        jsonString = context.assets.open("books.json")
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
        println(ioException)
    }
    val mapper = jacksonObjectMapper()
    val listedBooks:ListedBooks = mapper.readValue(jsonString)
    listedBooks.books.forEach {
         if (it.abbreviation == abbreviation){
             return it
        }
    }
    return listedBooks.books[0]
}

//Gets book by chapter and Abbreviation
fun getBookByChapter(context: Context, fileName: String): Book {
    lateinit var jsonString: String
    try {
        jsonString = context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
        println(ioException)
    }
    val mapper = jacksonObjectMapper()
    return mapper.readValue(jsonString)
}
