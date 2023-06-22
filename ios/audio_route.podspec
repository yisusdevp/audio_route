#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint audio_route.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'audio_route'
  s.version          = '1.0.3'
  s.summary          = 'Flutter plugin to get all the information about the device audio routes'
  s.description      = <<-DESC
Flutter plugin to get all the information about the device audio routes
                       DESC
  s.homepage         = 'https://github.com/yisusdevp/audio_route'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Jesus Coronado' => 'yisusdevp@gmail.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.platform = :ios, '13.0'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
end
