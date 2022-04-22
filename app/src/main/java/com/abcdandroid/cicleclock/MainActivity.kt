package com.abcdandroid.cicleclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.abcdandroid.cicleclock.ui.theme.CicleClockTheme
import kotlinx.coroutines.delay
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CicleClockTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Clock()
                }
            }
        }
    }
}


@Composable
fun Clock(modifier: Modifier = Modifier) {

    val secondAngle = 360 / 60
    val minuteAngle = 360 / 60
    val hourAngle = 360 / 12
    val radianFactor = PI / 180

    val minuteIndicator = 50
    val secondIndicator = 25

    val secondPointer = 350f
    val minutePointer = 300f
    val hourPointer = 220f

    var currentCalender by remember {
        mutableStateOf(Calendar.getInstance())
    }

    LaunchedEffect(key1 = true)
    {
        while (true) {
            delay(1000)
            currentCalender = Calendar.getInstance()
        }
    }

    val offset = animateOffsetAsState(
        targetValue = Offset(
            x = (secondPointer * cos(currentCalender.get(Calendar.SECOND) * secondAngle * radianFactor)).toFloat(),
            y = (secondPointer * sin(currentCalender.get(Calendar.SECOND) * secondAngle * radianFactor)).toFloat(),
        ), animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    Canvas(
        modifier = modifier.rotate(-90f),
        onDraw = {
            val radius = size.width / 2

            drawCircle(
                color = Color.White,
                radius = radius,
            )

            val angle = 360 / 60
            for (i in 0..59) {
                val currentAngle = i * angle
                val currentAngleInRad = currentAngle * (PI / 180)

                val indicatorLength = if (i % 5 == 0) minuteIndicator else secondIndicator

                drawLine(
                    color = Color.Black,
                    start = Offset(
                        x = (center.x + (radius - indicatorLength) * cos(currentAngleInRad)).toFloat(),
                        y = (center.y + (radius - indicatorLength) * sin(currentAngleInRad)).toFloat(),
                    ),
                    end = Offset(
                        x = (center.x + radius * cos(currentAngleInRad)).toFloat(),
                        y = (center.y + radius * sin(currentAngleInRad)).toFloat(),
                    ),
                    strokeWidth = 5f
                )
            }


            drawLine(
                strokeWidth = 5f,
                color = Color.Red,
                start = center,
                end = Offset(
                    x = offset.value.x + center.x,
                    y = offset.value.y + center.y
                )
            )


            drawLine(
                strokeWidth = 5f,
                color = Color.Black,
                start = center,
                end = Offset(
                    x = (center.x + minutePointer * cos(currentCalender.get(Calendar.MINUTE) * minuteAngle * radianFactor)).toFloat(),
                    y = (center.y + minutePointer * sin(currentCalender.get(Calendar.MINUTE) * minuteAngle * radianFactor)).toFloat(),
                )
            )

            drawLine(
                strokeWidth = 5f,
                color = Color.Gray,
                start = center,
                end = Offset(
                    x = (center.x + hourPointer * cos((currentCalender.get(Calendar.HOUR) + currentCalender.get(Calendar.MINUTE).toFloat() / 60) * hourAngle * radianFactor)).toFloat(),
                    y = (center.y + hourPointer * sin((currentCalender.get(Calendar.HOUR) + currentCalender.get(Calendar.MINUTE).toFloat() / 60) * hourAngle * radianFactor)).toFloat(),
                )
            )


        }
    )


}