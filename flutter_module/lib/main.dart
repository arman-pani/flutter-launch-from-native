import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

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

  Map<String, dynamic>? data;

  @override
  void initState() {
    getDataFromMc();
    super.initState();
  }

  void getDataFromMc() {
    platform.setMethodCallHandler((call) async {
      if (call.method == "sendJson") {
        final jsonString = call.arguments;
        data = jsonDecode(jsonString);

        setState(() {
          debugPrint("Decode: ${data ?? ''}");
        });
      }
    });
  }

  Uint8List? getImageBytes() {
    final bytes = data?['imageBytes'];
    if (bytes != null && bytes is List) {
      return Uint8List.fromList(List<int>.from(bytes));
    }
    return null;
  }

  @override
  Widget build(BuildContext context) {
    final TextStyle textStyle = TextStyle(
      color: Colors.white,
      fontSize: 20,
      fontWeight: FontWeight.bold,
    );

    debugPrint("Decode inseide build: ${data ?? ''}");

    final imageBytes = getImageBytes();

    return Scaffold(
      backgroundColor: Colors.black,
      body: data == null
          ? Center(child: CircularProgressIndicator())
          : Padding(
              padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 50),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Text(
                    "Product Display Screen in Flutter",
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  imageBytes != null
                      ? Image.memory(
                          imageBytes,
                          width: double.infinity,
                          height: 200,
                          fit: BoxFit.cover,
                        )
                      : const Text("No image selected"),

                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(data?['name'] ?? 'Product Name', style: textStyle),
                      Text(data!['price'].toString(), style: textStyle),
                    ],
                  ),
                  Text(data?['description'] ?? 'MBB', style: textStyle),
                ],
              ),
            ),
    );
  }
}
