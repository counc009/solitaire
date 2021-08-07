#!/bin/bash
javac -d ./build `find . -name '*.java'`
cd build
jar cfe solitaire.jar com.acc240.solitaire.Game `find . -name '*.class'`
