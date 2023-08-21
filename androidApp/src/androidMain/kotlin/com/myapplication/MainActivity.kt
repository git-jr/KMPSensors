package com.myapplication

import MainView
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.PI


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    private var valueToBeChangedOnTheLeft by mutableStateOf(4f)
    private var valueToBeChangedOnTheRight by mutableStateOf(4f)

    private var rotate by mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainView()
        }

        //initSensors()

        // Old()
    }

//    private fun Old() {
//        setContent {
//            val animatedOffset by animateFloatAsState(
//                targetValue = rotate,
//                animationSpec = spring(
//                    dampingRatio = Spring.DampingRatioHighBouncy,
//                    stiffness = Spring.StiffnessLow
//                ), label = "animated rotation"
//            )
//
//
//            Box {
//                Box(
//                    modifier = Modifier
//                        .rotate(animatedOffset)
//                        .fillMaxSize()
//                ) {
//                    MainView()
//                }
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    verticalArrangement = Arrangement.Bottom,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//                    Box(
//                        modifier = Modifier
//                            .alpha(0.5f)
//                            .fillMaxWidth()
//                            .weight(1f),
//                        contentAlignment = Alignment.BottomCenter
//                    ) {
//
//                        var width: Float
//                        var height = 1080f
//
//
//                        val startHeight by animateFloatAsState(
//                            targetValue = height / valueToBeChangedOnTheLeft,
//                            animationSpec = spring(
//                                dampingRatio = Spring.DampingRatioHighBouncy,
//                                stiffness = Spring.StiffnessLow
//                            ), label = "animate of the start height"
//                        )
//
//                        val endHeight by animateFloatAsState(
//                            targetValue = height / valueToBeChangedOnTheRight,
//                            animationSpec = spring(
//                                dampingRatio = Spring.DampingRatioHighBouncy,
//                                stiffness = Spring.StiffnessLow
//                            ), label = "animate of the end height"
//                        )
//
//                        Canvas(
//                            modifier = Modifier
//                                .size(500.dp)
//                        ) {
//                            width = size.width
//                            height = size.height
//
//                            val peakHeight = height * 0.2f
//                            val dipHeight = height * 0.2f
//
//                            val path = Path().apply {
//                                moveTo(-50f, startHeight + 100)  // Começa no canto esquerdo
//                                cubicTo(
//                                    width * 0.33f, peakHeight,  // Primeiro ponto de controle
//                                    width * 0.66f, dipHeight,  // Segundo ponto de controle
//                                    width + 50, endHeight + 100  // Termina no canto direito
//                                )
//                                lineTo(width + 50, height)  // Vai para a borda inferior direita
//                                lineTo(-50f, height)  // Vai para a borda inferior esquerda
//                                close()  // Fecha o Path, voltando ao ponto inicial
//                            }
//
//                            drawPath(path, color = Color(255, 0, 0), style = Stroke(width = 50f))
//
//                            drawPath(path, color = Color(0, 30, 255, 255), style = Fill)
//
//                        }
//                    }
//                }
//            }
//        }
//    }


    private fun initSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GRAVITY)
            gravity = event.values

        if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = event.values

        if (gravity != null && geomagnetic != null) {
            val firstArrayOfRotation = FloatArray(9)
            val seconArrayOfRotation = FloatArray(9)
            val success = SensorManager.getRotationMatrix(
                firstArrayOfRotation,
                seconArrayOfRotation,
                gravity,
                geomagnetic
            )
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(firstArrayOfRotation, orientation)

                val pitchInDegrees = (orientation[2] * (180.0 / PI)).toFloat()

                val inclination = pitchInDegrees.toInt() * 2

                rotate = pitchInDegrees / 8

                // Nomalizing the inclination to a value between 0 and 1.
                val normalizedInclination = (inclination + 80f) / 160f

                // Usando CoerceIn para limitar os valores maximo e minimo
                valueToBeChangedOnTheLeft = (10 - normalizedInclination * 9).coerceIn(
                    2f,
                    15f
                ) // ".coerceIn(5f, 8f)" is a good value for compound wave
                valueToBeChangedOnTheRight = (1 + normalizedInclination * 9).coerceIn(
                    2f,
                    15f
                ) // ".coerceIn(5f, 8f)" is a good value for compound wave

                Log.d("teste", "valor na Esquerda $valueToBeChangedOnTheLeft")
                Log.d("teste", "valor na Direita $valueToBeChangedOnTheRight")
            }
        }
    }

}