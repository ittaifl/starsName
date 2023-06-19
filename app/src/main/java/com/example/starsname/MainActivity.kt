package com.example.starsname

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.launch
import com.example.starsname.api.APODApi
import com.example.starsname.api.APODResponse

class MainActivity : AppCompatActivity() {

    private lateinit var apodApi: APODApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apodApi = retrofit.create(APODApi::class.java)

        val nameInput = findViewById<EditText>(R.id.editText)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val titleView = findViewById<TextView>(R.id.titleView)
        val explanationView = findViewById<TextView>(R.id.explanationView)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val date = nameToDate(nameInput.text.toString())
            lifecycleScope.launch {
                try {
                    val response = apodApi.getAPOD("p4AjgHole0V9Bdv3iSOSJ9r1K2YGl7azBh5hkUaT", date)
                    Glide.with(this@MainActivity).load(response.url).into(imageView)
                    titleView.text = response.title
                    explanationView.text = response.explanation
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Failed to load APOD", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun nameToDate(name: String): String {
        val sum = name.sumOf { it.code }
        val year = 2000 + sum % 21  // APOD API has data from 1995, so this will cover all years
        val month = 1 + sum % 12
        val day = 1 + sum % 28
        return "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
    }
}
