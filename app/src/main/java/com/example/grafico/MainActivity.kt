package com.example.grafico

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
              graficos()
        }
    }
}

@Composable
fun VicoChart() {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(5, 6, 5, 2, 11, 8, 5, 2, 15, 11, 8, 13, 12, 10, 2, 7) }
        }
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
        ),
        modelProducer,
    )
}

@Composable
fun ManualChart() {
    val chartData = listOf(
        Pair("Perros", 90),
        Pair("Gatos", 40),
        Pair("Ratas", 200),
        Pair("Pajaros", 10),
    )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
        ) {
            val canvasHeight = size.height
            val canvasWidth = size.width

            val spacingFromLeft = canvasWidth * 0.15f
            val spacingFromBottom = canvasHeight * 0.15f

            val upperValue = chartData.maxOfOrNull { it.second }?.plus(10) ?: 100
            val lowerValue = 0
            val range = upperValue - lowerValue
            val spacePerData = (canvasWidth - spacingFromLeft) / chartData.size

            drawLine(
                start = Offset(spacingFromLeft, 0f),
                end = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
                color = Color.Black,
                strokeWidth = 2f
            )

            drawLine(
                start = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
                end = Offset(canvasWidth, canvasHeight - spacingFromBottom),
                color = Color.Black,
                strokeWidth = 2f
            )

            val valuesToShow = 6
            for (i in 0..valuesToShow) {
                val y = canvasHeight - spacingFromBottom - (i.toFloat() / valuesToShow) * (canvasHeight - spacingFromBottom)

                val value = lowerValue + (range * i / valuesToShow.toFloat())
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        value.roundToInt().toString(),
                        spacingFromLeft - 10f,
                        y + 5f,
                        Paint().apply {
                            color = android.graphics.Color.BLACK
                            textAlign = Paint.Align.RIGHT
                            textSize = 12.sp.toPx()
                        }
                    )
                }
            }


            chartData.forEachIndexed { index, (label, value) ->
                val barWidth = spacePerData * 0.6f
                val barHeight = ((value - lowerValue).toFloat() / range) * (canvasHeight - spacingFromBottom)
                val leftPosition = spacingFromLeft + (index + 0.2f) * spacePerData
                val topPosition = canvasHeight - spacingFromBottom - barHeight

                drawRoundRect(
                    color = Color.Blue,
                    topLeft = Offset(leftPosition, topPosition),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(4f, 4f)
                )

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        label,
                        leftPosition + barWidth / 2,
                        canvasHeight - spacingFromBottom + 40f,
                        Paint().apply {
                            color = android.graphics.Color.BLACK
                            textAlign = Paint.Align.CENTER
                            textSize = 12.sp.toPx()
                        }
                    )
                }
            }
        }
    }


@Composable
fun graficos(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(text = "Grafico con Vico", modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(10.dp))
            VicoChart()
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = "Grafico con Canvas", modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(10.dp))
            ManualChart()
        }
    }
}