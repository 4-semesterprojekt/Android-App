package com.example.denfriebible.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.sp
import com.example.denfriebible.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
// Set of Material typography styles to start with
val quicksand = GoogleFont("Quicksand")
val b612Mono = GoogleFont("B612 Mono")
val carterOne = GoogleFont("Carter One")
//download font and use them with R.font.carterOne
//get them in through resource Manager
val quickSandFontFamily = FontFamily(
    Font(
        googleFont = quicksand,
        fontProvider = provider,
        weight = FontWeight.SemiBold
        )
)
val b612MonoFontFamily = FontFamily(
    Font(
        googleFont = b612Mono,
        fontProvider = provider,
        weight = FontWeight.Normal,
    )
)
val carterOneFontFamily = FontFamily(
    Font(
        googleFont = carterOne,
        fontProvider = provider,
        weight = FontWeight.Normal,
    )
)
val replyTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22. sp,
        lineHeight = 28. sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16. sp,
        lineHeight = 24. sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontFamily = b612MonoFontFamily,
        fontSize = 19. sp,
        lineHeight = 24. sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontFamily = quickSandFontFamily,
        fontSize = 15. sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontFamily = carterOneFontFamily,
        fontSize = 24. sp,
        lineHeight = 28. sp,
        letterSpacing = 0.4.sp
    ),

)
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
