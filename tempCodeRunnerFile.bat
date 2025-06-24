@echo off
del /s *.class

javac -cp ".;freetts.jar;cmu_us_kal.jar;cmu_us_kal16.jar" *.java
java -cp ".;freetts.jar;cmu_us_kal.jar;cmu_us_kal16.jar" GUI
pause
