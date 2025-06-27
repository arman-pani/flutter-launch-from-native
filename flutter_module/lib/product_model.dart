import 'dart:convert';

import 'package:flutter/material.dart';

class ProductModel {
  final String name;
  final double price;
  final String description;
  final Color textColor;
  final double textSize;
  final FontTextStyle textStyle;
  final String imageBase64String;

  ProductModel({
    required this.name,
    required this.imageBase64String,
    required this.price,
    required this.description,
    required this.textColor,
    required this.textSize,
    required this.textStyle,
  });
  factory ProductModel.fromMap(Map<String, dynamic> map) {
    String colorString = map['textColor'] ?? "FFFFFFFF";

    debugPrint("before: $colorString");

    if (colorString.startsWith('#')) {
      colorString = colorString.substring(1);
    }
    if (colorString.length == 6) {
      colorString = 'FF$colorString';
    }

    debugPrint("after: $colorString");

    return ProductModel(
      name: map['name'] ?? '',
      price: (map['price'])?.toDouble() ?? 0.0,
      description: map['description'] ?? '',
      textColor: Color(int.tryParse(colorString, radix: 16) ?? 0xFFFFFFFF),
      textSize: (map['textSize'])?.toDouble() ?? 0.0,
      textStyle: getFontStyle(map['textStyle']),
      imageBase64String: map['imageBase64String'] ?? '',
    );
  }

  factory ProductModel.fromJson(String source) =>
      ProductModel.fromMap(json.decode(source));

  @override
  String toString() {
    return 'ProductModel(name: $name, price: $price, description: $description, textColor: $textColor, textSize: $textSize, textStyle: $textStyle, imageBase64String: $imageBase64String)';
  }
}

FontTextStyle getFontStyle(String textStyle) {
  switch (textStyle) {
    case "Bold":
      return FontTextStyle.bold;
    case "Italic":
      return FontTextStyle.italic;
    case "All Caps":
      return FontTextStyle.allCaps;
    default:
      return FontTextStyle.bold;
  }
}

enum FontTextStyle { bold, italic, allCaps }
