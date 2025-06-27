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
import android.util.Base64

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
        var imageString: String = ""

       binding.textStyleRadioGroup.setOnCheckedChangeListener {_, checkedId ->
           when (checkedId){
               R.id.radio_bold -> textStyle = "Bold"
               R.id.radio_italic -> textStyle = "Italic"
               R.id.radio_allCaps -> textStyle = "All Caps"
           }
       }

        binding.colorRecyclerView.adapter = ColorItemRecyclerView(colors,
            onItemClick = fun (selectedColor: TextColor) {
                println("SELECTED COLOR : ${selectedColor.hexString}")
                textColor = selectedColor.hexString
        })
        binding.colorRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.colorRecyclerView.setHasFixedSize(true)


        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.productImageView.setImageURI(uri)
                imageString = Base64.encodeToString(contentResolver.openInputStream(uri)?.readBytes(), Base64.NO_WRAP)
            } else {
                println("No media selected")
            }
        }

        binding.selectImageButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.submitButton.setOnClickListener {

            println("TEXT COLOR : ${textColor}")
           val productModel: ProductModel = ProductModel(
               name = binding.nameEditText.text.toString().ifBlank { "Default Name" },
               price = binding.priceEditText.text.toString().toDoubleOrNull() ?: 0.0,
               description = binding.descriptionEditText.text.toString().ifBlank { "No description" },
               textColor = textColor.ifBlank { "#FFFFFF" },
               textSize = binding.textSizeSlider.values[0].toDouble(),
               textStyle = textStyle.ifBlank { "Bold" },
               imageBase64String = imageString
           )

            val jsonString = Json.encodeToString(productModel)

            println(jsonString)

            val engine = FlutterEngine(this)
            engine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

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