@echo off
set JAVA_HOME=C:\Users\%USERNAME%\.jdks\corretto-11.0.27
set PATH=%JAVA_HOME%\bin;%PATH%

echo ===================================================
echo Testing Fixed Path Logic
echo ===================================================
echo JAVA_HOME: %JAVA_HOME%
echo.

echo Running test generation to verify path fix...
java -cp "target/classes;src/main/resources;target/lib/*" es.us.isa.restest.main.TestGenerationAndExecution src/main/resources/My-Example/trainticket-demo.properties

echo.
echo Done!
pause 