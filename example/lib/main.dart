import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_step_count/flutter_step_count.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _stepCount = '0';

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            Text('Running on: $_stepCount'),
            FlatButton(
              child: Text('初始化'),
              onPressed: () => _init(),
            ),
            FlatButton(
              child: Text('获取步数'),
              onPressed: () => _getCount(),
            )
          ],
        ),
      ),
    );
  }

  Future<void> _init() async {
    await FlutterStepCount.init;
  }

  Future<void> _getCount() async {
    String stepCount = await FlutterStepCount.stepCount;
    setState(() {
      _stepCount = stepCount;
    });
  }
}
