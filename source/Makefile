JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(JAR) $*.java

CLASSES = \
    _CreateSpreadSheet.java \
	_NewWeek.java \
	_NewSemester.java \
	BufferedReader.java \
	BufferedWriter.java \
	ClassAnalysis.java \
	CreateStudent.java \
	File.java \
	FileReader.java \
	FileTerminal.java \
	FileWriter.java \
	Grader.java \
	InputScanner.java \
	JOptionPane.java \
	Log.java \
	Math.java \
	Pause.java \
	PrintWriter.java \
	Random.java \
	Scanner.java \
	Settings.java \
	Storage.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
