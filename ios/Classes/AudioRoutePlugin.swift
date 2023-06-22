import Flutter
import UIKit
import AVFoundation

public class AudioRoutePlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "yisusdevp.flutter/audio_route", binaryMessenger: registrar.messenger())
        let instance: AudioRoutePlugin = AudioRoutePlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "getCurrentInput":
            self.getCurrentInput(result: result)
            break
        case "getCurrentOutput":
            self.getCurrentOutput(result: result)
            break
        default:
            result(FlutterMethodNotImplemented)
            break
        }
    }

    private func getCurrentInput(result: FlutterResult) {
        do {
            let inputs = try AVAudioSession.sharedInstance().currentRoute.inputs
            
            if (!inputs.isEmpty) {
                let inputDevice = inputs.first!
                let inputId = inputDevice.uid.components(separatedBy: "-").first!
                let parsedDevice: [String: String] = ["id": inputId, "name": inputDevice.portName]
                return try result(parsedDevice)
            }
            
            return try result(FlutterError(
                    code: "NONE_CURRENT_INPUT_FOUND",
                    message: "There is none current audio route input",
                    details: nil
                )
            )
        } catch {
            return result(FlutterError(
                    code: "GET_CURRENT_INPUT_ERROR",
                    message: "Something went wrong while trying to get the current audio route input.",
                    details: nil
                )
            )
        }
    }

    private func getCurrentOutput(result: FlutterResult) {
        do {
            let outputs = try AVAudioSession.sharedInstance().currentRoute.outputs
            
            if (!outputs.isEmpty) {
                let outputDevice = outputs.first!
                let outputId = outputDevice.uid.components(separatedBy: "-").first!
                let parsedDevice: [String: String] = ["id": outputId, "name": outputDevice.portName]
                return try result(parsedDevice)
            }
            
            return try result(FlutterError(
                    code: "NONE_CURRENT_OUTPUT_FOUND",
                    message: "There is none current audio route output",
                    details: nil
                )
            )
        } catch {
            return result(FlutterError(
                    code: "GET_CURRENT_OUTPUT_ERROR",
                    message: "Something went wrong while trying to get the current audio route output.",
                    details: nil
                )
            )
        }
    }

}
