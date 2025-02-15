package com.example.androidpracticumcustomview

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidpracticumcustomview.MainActivity.ScreenState.ComposeUI
import com.example.androidpracticumcustomview.MainActivity.ScreenState.CustomView
import com.example.androidpracticumcustomview.MainActivity.ScreenState.Unselected
import com.example.androidpracticumcustomview.ui.theme.CustomContainer
import com.example.androidpracticumcustomview.ui.theme.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { // Jetpack Compose
            ChooseScreen()
        }
    }

    private fun startXmlPracticum() {
        val customContainer = CustomContainer(this)
        setContentView(customContainer)

        val firstView = TextView(this).apply {
            setBackgroundColor(Color.RED)
            text = context.getText(R.string.main_activity_first_view)
        }

        val secondView = TextView(this).apply {
            setBackgroundColor(Color.BLUE)
            text = context.getText(R.string.main_activity_second_view)
        }

        customContainer.addView(firstView)
        // Добавление второго элемента через некоторое время
        Handler(Looper.getMainLooper()).postDelayed({
            customContainer.addView(secondView)
        }, 2000)
    }

    @Preview
    @Composable
    fun ChooseScreen() {
        var showComposeScreen: ScreenState by remember { mutableStateOf(Unselected) }

        when (showComposeScreen) {
            Unselected -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(onClick = { showComposeScreen = ComposeUI }) {
                        Text(applicationContext.getString(R.string.main_activity_open_compose))
                    }
                    Button(onClick = { showComposeScreen = CustomView }) {
                        Text(applicationContext.getString(R.string.main_activity_open_custom_view))
                    }
                }
            }

            ComposeUI -> {
                MainScreen()
            }

            CustomView -> {
                LaunchedEffect(key1 = Unit) {
                    startXmlPracticum()
                }
            }
        }
    }

    sealed class ScreenState {
        data object Unselected : ScreenState()
        data object ComposeUI : ScreenState()
        data object CustomView : ScreenState()
    }
}