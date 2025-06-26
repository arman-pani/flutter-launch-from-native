package com.example.myapplication

import android.os.Bundle
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val colors = listOf(
            TextColor.BLUE,
            TextColor.GREEN,
            TextColor.RED,
            TextColor.YELLOW
        )

        var textColor: String = colors[0].hexString
        var textStyle = "Bold"
        var imageBytes: ByteArray = byteArrayOf()

        binding.boldStyleButton.setOnClickListener {
            textStyle = "Bold"
        }

        binding.italicStyleButton.setOnClickListener {
            textStyle = "Italic"
        }

        binding.allCapsStyleButton.setOnClickListener {
            textStyle = "All Caps"
        }

        binding.colorRecyclerView.adapter = ColorItemRecyclerView(colors,
            onItemClick = fun (selectedColor: TextColor) {
            textColor = selectedColor.toString()
        })
        binding.colorRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.colorRecyclerView.setHasFixedSize(true)


        // Register only once here
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.productImageView.setImageURI(uri)
                val inputStream = contentResolver.openInputStream(uri)
                imageBytes = inputStream?.readBytes()!!

            } else {
                println("No media selected")
            }
        }

        // Now trigger it on click
        binding.selectImageButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.submitButton.setOnClickListener {


           val productModel: ProductModel = ProductModel(
               name = binding.nameEditText.text.toString().ifBlank { "Default Name" },
               price = binding.priceEditText.text.toString().toDoubleOrNull() ?: 0.0,
               description = binding.descriptionEditText.text.toString().ifBlank { "No description" },
               textColor = textColor.ifBlank { "#000000" },
               textSize = binding.textSizeSlider.value.toDouble().takeIf { it > 0 } ?: 14.0,
               textStyle = textStyle.ifBlank { "Normal" },
               imageBytes = imageBytes
           )

            val jsonString = Json.encodeToString(productModel)

            println(jsonString)

//            val intent = FlutterActivity
//                .withNewEngine()
//                .build(this)
//
//            startActivity(intent)



            val engine = FlutterEngine(this)
            engine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
            // Start FlutterActivity with that engine
            FlutterEngineCache.getInstance().put("engine_id", engine)
            val intent = FlutterActivity.withCachedEngine("engine_id").build(this)
            startActivity(intent)
            val channel = MethodChannel(engine.dartExecutor.binaryMessenger, "com.example.myapplication/product")

            android.os.Handler(Looper.getMainLooper()).postDelayed({
                channel.invokeMethod("sendJson", jsonString)
            }, 1000)

        }
    }


}