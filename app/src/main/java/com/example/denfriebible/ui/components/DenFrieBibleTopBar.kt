package com.example.denfriebible.ui.components

import com.example.denfriebible.DenFrieBibleDestination
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun DenFrieBibleTopBar(
    allScreens: List<DenFrieBibleDestination>,
    onTabSelected: (DenFrieBibleDestination) -> Unit,
    currentScreen: DenFrieBibleDestination
) {
    Surface(
        Modifier
            .height(TabHeight)
            .fillMaxWidth()
    ) {
        Row(Modifier.selectableGroup()) {
            allScreens.forEach { screen ->
                DFBTab(
                    text = screen.nameNav,
                    icon = screen.icon,
                    onSelected = { onTabSelected(allScreens[0]) },
                    selected = currentScreen == screen
                )
            }
        }
    }
}


@Composable
private fun DFBTab(
    text: String, icon: ImageVector, onSelected: () -> Unit, selected: Boolean
) {

    val color = MaterialTheme.colors.onSurface
    val durationMillis = if (selected) TabFadeInAnimationDuration else TabFadeOutAnimationDuration
    val animSpec = remember {
        tween<Color>(
            durationMillis = durationMillis,
            easing = LinearEasing,
            delayMillis = TabFadeInAnimationDelay
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (selected) color else color.copy(alpha = InactiveTabOpacity),
        animationSpec = animSpec
    )
    Row(modifier = Modifier
        .padding(horizontal = 5.dp, vertical = 16.dp)
        .animateContentSize()
        .height(TabHeight)
        .selectable(
            selected = selected,
            onClick = onSelected,
            role = Role.Tab,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(
                bounded = false, radius = Dp.Unspecified, color = Color.Unspecified
            )
        )
        .clearAndSetSemantics { contentDescription = text }) {
        if (selected) {
            Icon(imageVector = icon, contentDescription = text, tint = tabTintColor)
            Spacer(Modifier.width(12.dp))
            Text(text.uppercase(Locale.getDefault()), color = MaterialTheme.colors.primary)
            Spacer(Modifier.width(12.dp))
        }
    }
}

private val TabHeight = 56.dp
private const val InactiveTabOpacity = 0.60f

private const val TabFadeInAnimationDuration = 150
private const val TabFadeInAnimationDelay = 100
private const val TabFadeOutAnimationDuration = 100