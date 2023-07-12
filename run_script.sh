#!/bin/bash
rm -rf target
mkdir target
cp -rf files/ target/files
javac src/com/tamatem/assessment/*.java -d target
cd target/
jar cfe assessment.jar com.tamatem.assessment.Main com/tamatem/assessment/*.class
java -jar assessment.jar