package com.example.ejemplopractico

import android.os.Bundle

import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var buttonRefresh: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el RecyclerView y Button
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        buttonRefresh = findViewById(R.id.buttonRefresh)

        // Al iniciar la actividad, llamas a fetchPosts para obtener los datos
        // Esto asegura que la UI no se bloquee mientras se espera la respuesta de la API

        fetchPosts()

        // Refrescar datos al hacer clic en el botón
        buttonRefresh.setOnClickListener {
            fetchPosts()
        }
    }

    // Función para hacer la solicitud a la API y actualizar el RecyclerView
    private fun fetchPosts() {

        // Lanzar una corutina en el contexto de I/O

        lifecycleScope.launch(Dispatchers.IO) {

            // Realizamos la llamada a la API para obtener los posts

            val response: Response<List<Post>> = RetrofitInstance.api.getPosts()

// Verificamos si la respuesta fue exitosa

            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()

                withContext(Dispatchers.Main) {
                    // Configura el adaptador con los datos recibidos
                    postAdapter = PostAdapter(posts)
                    recyclerView.adapter = postAdapter

                    // Mostramos un mensaje de éxito

                    Toast.makeText(this@MainActivity, "Datos recibidos!", Toast.LENGTH_SHORT).show()
                }
            } else {

                // Si la respuesta no fue exitosa, mostramos un mensaje de error

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


