#!/bin/bash
javac -d ./build `find ./src -name '*.java'`
cd build
jar cfe solitaire.jar com.acc240.solitaire.Game `find . -name '*.class'`
