#!/bin/tcsh
# ant -buildfile build.xml
# javac -classpath .:./classes test/IdfTest/TCGenerator.java
# javac -classpath .:./classes test/IdfTest/TCSummary.java
java -classpath .:./classes test.IdfTest.TCGenerator test/IdfTest/TestFile.idf
java -classpath .:./classes test.IdfTest.TCSummary

exit 0
