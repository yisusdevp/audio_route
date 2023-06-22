import 'package:audio_route/audio_route.dart';
import 'package:flutter/material.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      title: 'Material App',
      home: HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Material App Bar'),
      ),
      body: Center(
        child: MaterialButton(
          onPressed: () async {
            try {
              final device = await AudioRoute.instance.getCurrentOutput();

              if (context.mounted) {
                showAboutDialog(
                  context: context,
                  children: [
                    Text("ID: ${device.id}"),
                    Text("Name: ${device.name}"),
                  ],
                );
              }
            } catch (e) {
              showAboutDialog(
                context: context,
                children: [
                  Text(e.toString()),
                ],
              );
            }
          },
          child: const Text("Get Output"),
        ),
      ),
    );
  }
}
