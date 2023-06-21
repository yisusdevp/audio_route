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
            
            if (inputs.isEmpty) {
                result(nil)
            } else {
                let input = inputs.first!
                let parsedDevice: [String: String] = ["uid": input.uid, "name": input.portName]
                try result(parsedDevice)
            }
            
        } catch {
            result(FlutterError(
                    code: "GET_CURRENT_INPUT_ERROR",
                    message: "Something went wrong while trying to get the current audio route input.",
                    details: nil
                )
            )
        }
        
        return
    }

    private func getCurrentOutput(result: FlutterResult) {
        do {
            let outputs = try AVAudioSession.sharedInstance().currentRoute.outputs
            
            if (outputs.isEmpty) {
                result(nil)
            } else {
                let output = outputs.first!
                let parsedDevice: [String: String] = ["uid": output.uid, "name": output.portName]
                try result(parsedDevice)
            }
            
        } catch {
            result(FlutterError(
                    code: "GET_CURRENT_OUTPUT_ERROR",
                    message: "Something went wrong while trying to get the current audio route output.",
                    details: nil
                )
            )
        }
        
        return
    }

}
