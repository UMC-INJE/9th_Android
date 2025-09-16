package com.umc.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import com.umc.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var imageViews : List<ImageView>
    private lateinit var textViews : List<TextView>
    private val colors = listOf(Color.YELLOW, Color.CYAN, Color.BLUE, Color.GREEN, Color.RED)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageViews = listOf(binding.imageView1, binding.imageView2, binding.imageView3, binding.imageView4, binding.imageView5)
        textViews = listOf(binding.textView1, binding.textView2, binding.textView3, binding.textView4, binding.textView5)

        imageViews.mapIndexed { imageIndex, imageView ->
            imageView.setOnClickListener {
                textViews.mapIndexed { textIndex, textView ->
                    if(imageIndex == textIndex) {
                        textView.setTextColor(colors[imageIndex])
                        Toast.makeText(this, textView.text, Toast.LENGTH_SHORT).show()
                    }
                    else textView.setTextColor(Color.BLACK)
                }

            }

        }
    }
}