library audio_route;

import 'package:flutter/services.dart';

class AudioRoute {
  const AudioRoute._();

  static const AudioRoute _instance = AudioRoute._();
  static AudioRoute get instance => _instance;

  final MethodChannel _channel = const MethodChannel('yisusdevp.flutter/audio_route');

  Future<AudioDevice?> getCurrentInput() async {
    try {
      final inputDevice = await _channel.invokeMethod<Map<String, String>?>("getCurrentInput");

      if (inputDevice != null) {
        return AudioDeviceModel.fromMap(inputDevice);
      }

      return null;
    } catch (e) {
      rethrow;
    }
  }

  Future<AudioDevice?> getCurrentOutput() async {
    try {
      final outputDevice = await _channel.invokeMethod<Map<String, String>?>("getCurrentOutput");

      if (outputDevice != null) {
        return AudioDeviceModel.fromMap(outputDevice);
      }

      return null;
    } catch (e) {
      rethrow;
    }
  }
}

abstract class AudioDevice {
  String get id;
  String get name;
}

class AudioDeviceModel implements AudioDevice {
  AudioDeviceModel({
    required this.id,
    required this.name,
  });

  @override
  final String id;

  @override
  final String name;

  factory AudioDeviceModel.fromMap(Map<String, String> map) {
    return AudioDeviceModel(
      id: map['uid']!,
      name: map['name']!,
    );
  }

  @override
  int get hashCode => id.hashCode ^ name.hashCode;

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;

    return other is AudioDeviceModel && other.id == id && other.name == name;
  }

  @override
  String toString() => "AudioDevice(\nid: $id,\nname: $name\n)";
}
