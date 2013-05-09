#!/bin/tcsh

if ($# == 0) then
  set testFile=test/IdfTest/TestFile.idf
else
  set testFile=$1
endif

#ant -buildfile build.xml
#javac -classpath .:./classes test/IdfTest/TCGenerator.java
#javac -classpath .:./classes test/IdfTest/TCSummary.java

java -classpath .:./classes test.IdfTest.TCGenerator $testFile
java -classpath .:./classes test.IdfTest.TCSummary   $testFile

exit 0
