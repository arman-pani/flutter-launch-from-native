package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class ProductDisplayActivity : FlutterActivity() {
    private val CHANNEL = "com.example.myapplication/product"

    private lateinit var _productJson: String

    companion object {

        fun start(context: Context, productJson: String) {
            val intent = Intent(context, ProductDisplayActivity::class.java)
            intent.putExtra("product_json", productJson)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _productJson = intent.getStringExtra("product_json") ?: ""
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
                call, result ->
            if (call.method == "getProductJson"){
                if (::_productJson.isInitialized){
                    result.success(_productJson)
                } else {
                    result.error("UNAVAILABLE", "Product data not available.", null)
                }
            } else {
                result.notImplemented()
            }

        }
    }
}