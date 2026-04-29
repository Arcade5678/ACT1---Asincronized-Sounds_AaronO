package com.example.act1_asincronizedsoundsaarono

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.act1_asincronizedsoundsaarono.ui.theme.ACT1AsincronizedSoundsAaronOTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "SonidosAsync"

data class Sonido(val etiqueta: String, val recursoId: Int)

class MainActivity : ComponentActivity() {

    private val listaSonidos = listOf(
        Sonido("Pop",        R.raw.pop),
        Sonido("Whoosh",     R.raw.whoosh),
        Sonido("Thud",       R.raw.thud),
        Sonido("Fah",        R.raw.fah),
        Sonido("Mouse Click",R.raw.mouse_click)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ACT1AsincronizedSoundsAaronOTheme {
                val colores = listOf(
                    Color(0xFF6200EE),
                    Color(0xFF03DAC5),
                    Color(0xFFCF6679),
                    Color(0xFFFF9800),
                    Color(0xFF4CAF50)
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF121212)
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Asincron Sounds!",
                            fontSize = 26.sp,
                            color = Color(0xFFE0E0E0)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        listaSonidos.forEachIndexed { index, sonido ->
                            Button(
                                onClick = { reproducirAsync(sonido) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colores[index]
                                )
                            ) {
                                Text(
                                    text = "▶  ${sonido.etiqueta}",
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Pulsa varios botones a la vez para reproducir audios simultáneamente",
                            fontSize = 13.sp,
                            color = Color(0xFF888888)
                        )
                    }
                }
            }
        }
    }

    // Reproducción asíncrona con MediaPlayer dentro de una coroutine
    private fun reproducirAsync(sonido: Sonido) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "Inicio reproducción: ${sonido.etiqueta} | hilo: ${Thread.currentThread().name}")

            val mp = MediaPlayer.create(this@MainActivity, sonido.recursoId)
            mp.setOnCompletionListener { player ->
                Log.d(TAG, "Completado: ${sonido.etiqueta}")
                player.release() // liberar recursos al terminar
            }
            mp.start()

            Log.d(TAG, "MediaPlayer.start() llamado para: ${sonido.etiqueta}")
        }
    }
}