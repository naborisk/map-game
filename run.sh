#!/bin/bash

javac --module-path ~/Java_lib/javafx-sdk-15.0.1/lib --add-modules javafx.web,javafx.fxml ./*.java
java --module-path ~/java_lib/javafx-sdk-15.0.1/lib  --add-modules javafx.web,javafx.fxml MapGame

