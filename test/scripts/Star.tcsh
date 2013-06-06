#!/bin/tcsh

if ($# == 0) then
  set testFile=src/johar/samples/Profiler.idf
else
  set testFile=$1
endif

ant -buildfile build.xml

java -classpath .:./classes:./src johar.interfaceinterpreter.star.Star $testFile

exit 
