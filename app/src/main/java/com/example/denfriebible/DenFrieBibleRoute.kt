package com.example.denfriebible

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

interface DenFrieBibleDestination {
    val icon: ImageVector
    val route: String
    val nameNav: String
}
object DefaultView : DenFrieBibleDestination {
    override val icon = Icons.Filled.Home
    override val route = "defaultView"
    override val nameNav = "Den Frie Bible"
}
object GetChapter : DenFrieBibleDestination {
    override val icon = Icons.Filled.ArrowBack
    override val route = "book/{abbreviation}"
    override val nameNav = "Chapter"
}
object GetBook : DenFrieBibleDestination {
    override val icon = Icons.Filled.ArrowBack
    override val route = "book/{abbreviation}/{number}"
    override val nameNav = "Book"
}



val DenFrieBibleTabRowScreens = listOf(DefaultView, GetBook, GetChapter)