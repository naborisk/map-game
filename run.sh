#!/bin/bash

javac --module-path ~/Java_lib/javafx-sdk-15.0.1/lib --add-modules javafx.web,javafx.fxml ./src/mapgame/*.java
java --module-path ~/java_lib/javafx-sdk-15.0.1/lib  --add-modules javafx.web,javafx.fxml out/production/MapGame一式/mapgame/MapGame