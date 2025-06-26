import 'dart:convert';
import 'dart:ui';

class ProductModel {
  final String name;
  final double price;
  final String description;
  final Color textColor;
  final double textSize;
  final FontWeight textStyle;
  final String imageUrl;

  ProductModel({
    required this.name,
    required this.imageUrl,
    required this.price,
    required this.description,
    required this.textColor,
    required this.textSize,
    required this.textStyle,
  });

  factory ProductModel.fromMap(Map<String, dynamic> map) {
    String colorString = map['textColor'] ?? "FF000000";
    if (colorString.startsWith('#')) {
      colorString = colorString.substring(1);
    }
    if (colorString.length == 6) {
      colorString = 'FF$colorString';
    }
    return ProductModel(
      name: map['name'] ?? '',
      price: (map['price'])?.toDouble() ?? 0.0,
      description: map['description'] ?? '',
      textColor: Color(int.tryParse(colorString, radix: 16) ?? 0xFF000000),
      textSize: (map['textSize'])?.toDouble() ?? 0.0,
      textStyle: getFontWeight(map['textStyle']),
      imageUrl: map['imageUrl'] ?? '',
    );
  }

  factory ProductModel.fromJson(String source) =>
      ProductModel.fromMap(json.decode(source));

  @override
  String toString() {
    return 'ProductModel(name: $name, price: $price, description: $description, textColor: $textColor, textSize: $textSize, textStyle: $textStyle, imageUrl: $imageUrl)';
  }
}

FontWeight getFontWeight(String textStyle) {
  switch (textStyle) {
    case "Bold":
      return FontWeight.bold;
    case "Italic":
      return FontWeight.normal;
    case "All Caps":
      return FontWeight.w100;
    default:
      return FontWeight.bold;
  }
}
