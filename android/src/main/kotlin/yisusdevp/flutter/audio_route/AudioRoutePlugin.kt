package yisusdevp.flutter.audio_route

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

/** AudioRoutePlugin **/
class AudioRoutePlugin: FlutterPlugin, MethodCallHandler {
  private lateinit var channel : MethodChannel
  private lateinit var context: Context

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "yisusdevp.flutter/audio_route")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when (call.method) {
      "getCurrentInput" -> {
        getCurrentInput(context, result)
      }
      "getCurrentOutput" -> {
        getCurrentOutput(context, result)
      }
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  private fun getCurrentInput(context: Context, result: Result) {
    try {
      val audioManager = context.getSystemService(AudioManager::class.java)
      val inputs: Array<AudioDeviceInfo> = audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)

      if (inputs.isNotEmpty()) {
        var inputDevice = inputs.first()
        val parsedDevice: HashMap<String, String> = HashMap()
        parsedDevice["uid"] = inputDevice.id.toString()
        parsedDevice["name"] = inputDevice.productName.toString()

        return result.success(parsedDevice)
      }

      return result.error( "NONE_CURRENT_INPUT_FOUND", "There is none current audio route input", null)
    } catch (e: Exception) {
      result.error( "GET_CURRENT_INPUT_ERROR", "Something went wrong while trying to get the current audio route input.", null)
    }
  }

  private fun getCurrentOutput(context: Context, result: Result) {
   try {
     val audioManager = context.getSystemService(AudioManager::class.java)
     val outputs: Array<AudioDeviceInfo> = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)

     if (outputs.isNotEmpty()) {
       var outputDevice = outputs.first()
       val parsedDevice: HashMap<String, String> = HashMap()
       parsedDevice["id"] = outputDevice.id.toString()
       parsedDevice["name"] = outputDevice.productName.toString()

       return result.success(parsedDevice)
     }

     return result.error( "NONE_CURRENT_OUTPUT_FOUND", "There is none current audio route output", null)
   } catch (e: Exception) {
     result.error( "GET_CURRENT_OUTPUT_ERROR", "Something went wrong while trying to get the current audio route output.", null)
   }
  }
}
