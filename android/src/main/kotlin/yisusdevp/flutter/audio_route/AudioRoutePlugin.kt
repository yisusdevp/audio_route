package yisusdevp.flutter.audio_route

import androidx.annotation.NonNull
import android.content.Context
import android.media.AudioManager

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.lang.Exception

/** AudioRoutePlugin */
class AudioRoutePlugin: FlutterPlugin, MethodCallHandler {
  private lateinit var channel : MethodChannel
  private lateinit var context: Context

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "yisusdevp.flutter/audio_route")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "getCurrentInput" => {
        getCurrentInput(context, result)
      }
      "getCurrentOutput" => {
        getCurrentOutput(context, result)
      }
    }
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  private fun getCurrentInput(context: Context, result: Result) {
    try {
      val audioManager = context.getSystemService(AudioManager::class.java)
      val inputs = audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)

      if (inputs.isEmpty()) {
        return result(null)
      }

      var inputDevice: inputs.first
      val parsedDevice: HashMap<String, String> = HashMap()
      parsedDevice["uid"] = inputDevice.id.toString()
      parsedDevice["name"] = inputDevice.name

      return result(parsedDevice)
    } catch (e: Exception) {
      result.error( "GET_CURRENT_INPUT_ERROR", "Something went wrong while trying to get the current audio route input.", "")
    }
  }

  private fun getCurrentOutput(context: Context, result: Result) {
   try {
     val audioManager = context.getSystemService(AudioManager::class.java)
     val outputs = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)

     if (outputs.isEmpty()) {
       return result(null)
     }

     var outputDevice: outputs.first
     val parsedDevice: HashMap<String, String> = HashMap()
     parsedDevice["uid"] = outputDevice.id.toString()
     parsedDevice["name"] = outputDevice.name

     return result(parsedDevice)
   } catch (e: Exception) {
     result.error( "GET_CURRENT_OUTPUT_ERROR", "Something went wrong while trying to get the current audio route output.", "")
   }
  }
}
