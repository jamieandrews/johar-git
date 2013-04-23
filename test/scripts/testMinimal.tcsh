#!/bin/tcsh

java -classpath classes johar.idf.Idf2xml \
  < test/resources/Minimal.idf \
  > test/resources/Minimal.xml

java -classpath classes johar.idf.Idf test/resources/Minimal.xml

exit 0

