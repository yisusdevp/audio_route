package yisusdevp.flutter.audio_route

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.MediaRouter
import android.os.Build
import androidx.annotation.RequiresApi
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
    // TODO: Refactor this logic, currently not working
    try {
      val audioManager = context.getSystemService(AudioManager::class.java)
      val inputs: Array<AudioDeviceInfo> = audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)

      if (inputs.isNotEmpty()) {
        var inputDevice = inputs.first()
        val parsedDevice: HashMap<String, String> = HashMap()
        parsedDevice["id"] = inputDevice.id.toString()
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
     val mediaRouter = context.getSystemService(MediaRouter::class.java)
     val audioManager = context.getSystemService(AudioManager::class.java)

     val currentOutput = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO)
     val deviceOutputs: Array<AudioDeviceInfo> = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)

     if (deviceOutputs.isNotEmpty()) {
       val outputDevice = deviceOutputs.find { e -> e.productName.toString() == currentOutput.name.toString() } ?: deviceOutputs.first()

       val parsedDevice: HashMap<String, String> = HashMap()
       parsedDevice["id"] = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) outputDevice.address else outputDevice.id.toString()
       parsedDevice["name"] = outputDevice.productName.toString()

       return result.success(parsedDevice)

     }

     return result.error( "NONE_CURRENT_OUTPUT_FOUND", "There is none current audio route output", null)
   } catch (e: Exception) {
     result.error( "GET_CURRENT_OUTPUT_ERROR", "Something went wrong while trying to get the current audio route output.", null)
   }
  }
}
