#! /bin/bash

# clear
cd "/Users/kevin/Documents/Repos/LabGrader/source"
printf "====================\n..Compiling Files..\n====================\n"
make 2>&1 1>/dev/null

#JAR FILES
c_jar=""
jar=""

#DERBY DB CODE
#======================================================================================================================================
# c_jar="-classpath /Library/Java/Extensions/db-derby-10.15.1.3-bin/lib/derby.jar -classpath /Users/kevin/Documents/Repos/LabGrader/src"
# jar="-cp .:/Library/Java/Extensions/db-derby-10.15.1.3-bin/lib/derby.jar"
# javac $c_jar DriverManager.java
#======================================================================================================================================


#JAVA FX CODE
#======================================================================================================================================
# c_jar="--module-path /Library/Java/Extensions/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls,javafx.fxml,javafx.graphics"
# jar="--module-path /Library/Java/Extensions/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls"
# javac $c_jar Image.java

#Complile/run java with package/jar files:
#https://www.codejava.net/java-core/tools/how-to-compile-package-and-run-a-java-program-using-command-line-tools-javac-jar-and-java

#V2 - this compiles & runs main file with javafx imported but exception: java.lang.NullPointerException: Location is required. (finding fxml file error)
#javac -d classes --module-path /Library/Java/Extensions/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls,javafx.fxml,javafx.graphics src/sample/*.java
#java -cp ./classes --module-path /Library/Java/Extensions/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls,javafx.fxml,javafx.graphics sample.Main
#======================================================================================================================================



java CreateStudent
if [[ $? == "1" ]]
then
    exit 1
fi
printf "====================\n..Compiling Student..\n====================\n"
# # RunStudent doesn't compile without Student
javac $c_jar Student.java
if [[ $? == "1" ]]
then
    exit 1
fi
javac $c_jar RunStudent.java
if [[ $? == "1" ]]
then
    exit 1
fi

java $jar ClassAnalysis
if [[ $? == "1" ]]
    then
    exit 1
fi
printf "====================\n..Running Student..\n====================\n"
java $jar Student
java Pause

java $jar RunStudent
if [[ $? == "1" ]]
    then
    exit 1
fi
java $jar Student
java Pause
printf "====================\n..Grader..\n====================\n"

java Grader
if [[ $? == "1" ]]
    then
    exit 1
fi