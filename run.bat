cd "/Users/kevin/Documents/Repos/LabGrader/source"
echo "====================\n..Compiling Files..\n====================\n"
make 2>&1 1>/dev/null

@REM #JAR FILES
c_jar=""
jar=""

@REM #DERBY DB CODE
@REM #======================================================================================================================================
@REM # c_jar="-classpath /Library/Java/Extensions/db-derby-10.15.1.3-bin/lib/derby.jar -classpath /Users/kevin/Documents/Repos/LabGrader/src"
@REM # jar="-cp .:/Library/Java/Extensions/db-derby-10.15.1.3-bin/lib/derby.jar"
@REM # javac $c_jar DriverManager.java
@REM #======================================================================================================================================


@REM #JAVA FX CODE
@REM #======================================================================================================================================
@REM # c_jar="--module-path /Library/Java/Extensions/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls,javafx.fxml,javafx.graphics"
@REM # jar="--module-path /Library/Java/Extensions/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls"
@REM # javac $c_jar Image.java

@REM #Complile/run java with package/jar files:
@REM #https://www.codejava.net/java-core/tools/how-to-compile-package-and-run-a-java-program-using-command-line-tools-javac-jar-and-java

@REM #V2 - this compiles & runs main file with javafx imported but exception: java.lang.NullPointerException: Location is required. (finding fxml file error)
@REM #javac -d classes --module-path /Library/Java/Extensions/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls,javafx.fxml,javafx.graphics src/sample/*.java
@REM #java -cp ./classes --module-path /Library/Java/Extensions/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls,javafx.fxml,javafx.graphics sample.Main
@REM #======================================================================================================================================



java CreateStudent
if [[ $? == "1" ]]
then
    exit 1
fi
echo "====================\n..Compiling Student..\n====================\n"
@REM # # RunStudent doesn't compile without Student
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
echo "====================\n..Running Student..\n====================\n"
java $jar Student
java Pause

java $jar RunStudent
if [[ $? == "1" ]]
    then
    exit 1
fi
java $jar Student
java Pause
echo "====================\n..Grader..\n====================\n"

java Grader
if [[ $? == "1" ]]
    then
    exit 1
fi