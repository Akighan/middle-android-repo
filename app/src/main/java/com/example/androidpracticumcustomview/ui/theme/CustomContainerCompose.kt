package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.example.androidpracticumcustomview.ui.theme.AnimationConstants.ALPHA_ANIM_DURATION
import com.example.androidpracticumcustomview.ui.theme.AnimationConstants.TRANSLATION_Y_ANIM_DURATION
import kotlinx.coroutines.launch

@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?, secondChild: @Composable (() -> Unit)?
) {
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    Box(modifier = Modifier
        .fillMaxSize()
        .onSizeChanged {
            parentSize = it
        }) {

        if (parentSize != IntSize.Zero) {
            val parentHeight = parentSize.height
            val childHeight = parentHeight / 2

            val offsetYForFirstChild = remember { Animatable(0f) }
            val offsetYForSecondChild = remember { Animatable(0f) }
            val alphaAnimation = remember { Animatable(0f) }

            LaunchedEffect(Unit) {
                launch {
                    offsetYForFirstChild.animateTo((-childHeight / 2).toFloat(), animationSpec = tween(TRANSLATION_Y_ANIM_DURATION, easing = LinearEasing))
                }
                launch {
                    offsetYForSecondChild.animateTo((childHeight / 2).toFloat(), animationSpec = tween(TRANSLATION_Y_ANIM_DURATION, easing = LinearEasing))
                }
                launch {
                    alphaAnimation.animateTo(1f, animationSpec = tween(ALPHA_ANIM_DURATION, easing = LinearEasing))
                }
            }

            firstChild?.let {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        translationY = offsetYForFirstChild.value
                        alpha = alphaAnimation.value
                    }) {
                    it.invoke()
                }
            }
            secondChild?.let {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        translationY = offsetYForSecondChild.value
                        alpha = alphaAnimation.value
                    }) {
                    it.invoke()
                }
            }
        }
    }
}

object AnimationConstants {
    const val TRANSLATION_Y_ANIM_DURATION = 5_000
    const val ALPHA_ANIM_DURATION = 2_000
}