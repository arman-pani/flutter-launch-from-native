import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/product_model.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter View',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: ProductDisplayScreen(),
    );
  }
}

class ProductDisplayScreen extends StatefulWidget {
  const ProductDisplayScreen({super.key});

  @override
  State<ProductDisplayScreen> createState() => _ProductDisplayScreenState();
}

class _ProductDisplayScreenState extends State<ProductDisplayScreen> {
  static const platform = MethodChannel('com.example.myapplication/product');

  ProductModel? productModel;

  @override
  void initState() {
    getDataFromMc();
    super.initState();
  }

  void getDataFromMc() {
    platform.setMethodCallHandler((call) async {
      if (call.method == "sendJson") {
        final String jsonString = call.arguments;
        productModel = ProductModel.fromJson(jsonString);

        setState(() {
          debugPrint("Decode: ${productModel.toString()}");
        });
      }
    });
  }

  String getText(String text) {
    return productModel?.textStyle == FontTextStyle.allCaps
        ? text.toUpperCase()
        : text;
  }

  @override
  Widget build(BuildContext context) {
    final TextStyle textStyle = TextStyle(
      color: productModel?.textColor ?? Colors.white,
      fontSize: productModel?.textSize ?? 20,
      fontWeight: productModel?.textStyle == FontTextStyle.bold
          ? FontWeight.bold
          : FontWeight.normal,
      fontStyle: productModel?.textStyle == FontTextStyle.italic
          ? FontStyle.italic
          : FontStyle.normal,
    );

    debugPrint("=====> ${productModel.toString()}");

    return Scaffold(
      backgroundColor: Colors.black,
      body: productModel == null
          ? Center(child: CircularProgressIndicator())
          : Padding(
              padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 50),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                spacing: 20,
                children: [
                  Text(
                    "Product Display Screen in Flutter",
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  productModel?.imageBase64String != null
                      ? Image.memory(
                          base64Decode(productModel?.imageBase64String ?? ""),
                          width: double.infinity,
                          height: 200,
                          fit: BoxFit.cover,
                        )
                      : const Text("No image selected"),

                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        getText(productModel?.name ?? "Product Name"),
                        style: textStyle,
                      ),
                      Text(
                        getText("\$ ${productModel?.price.toString() ?? 0.00}"),
                        style: textStyle,
                      ),
                    ],
                  ),
                  Text(
                    getText(productModel?.description ?? 'Description'),
                    textAlign: TextAlign.left,
                    style: textStyle,
                  ),
                ],
              ),
            ),
    );
  }
}
