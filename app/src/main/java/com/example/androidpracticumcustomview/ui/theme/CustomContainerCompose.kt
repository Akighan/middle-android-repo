package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?, secondChild: @Composable (() -> Unit)?
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val parentWidth = maxWidth
        val parentHeight = maxHeight
        val childHeight = maxHeight / 2

        var startAnimation by remember { mutableStateOf(false) }

        val transition = updateTransition(startAnimation)

        val alphaTransition by transition.animateFloat(transitionSpec = { tween(2000) }) { state ->
            if (state) 1f else 0f
        }

        val translationYForFirstChildTransition by transition.animateDp(transitionSpec = {
            tween(
                5000
            )
        }) { state ->
            if (state) -childHeight / 2 else 0.dp
        }

        val translationYForSecondChildTransition by transition.animateDp(transitionSpec = {
            tween(
                5000
            )
        }) { state ->
            if (state) childHeight / 2 else 0.dp
        }

        LaunchedEffect(Unit) {
            startAnimation = true
        }

        Box(
            modifier = Modifier
                .size(parentWidth, parentHeight / 2)
                .align(Alignment.Center)
                .offset(y = translationYForFirstChildTransition)
                .alpha(alphaTransition)
        ) {
            firstChild?.invoke()
        }
        Box(
            modifier = Modifier
                .size(parentWidth, parentHeight / 2)
                .align(Alignment.Center)
                .offset(y = translationYForSecondChildTransition)
                .alpha(alphaTransition)
        ) {
            secondChild?.invoke()
        }
    }
}