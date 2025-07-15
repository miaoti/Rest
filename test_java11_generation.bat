@echo off
set JAVA_HOME=C:\Users\%USERNAME%\.jdks\corretto-11.0.27
set PATH=%JAVA_HOME%\bin;%PATH%

echo ===================================================
echo Testing RESTest with Java 11 (Corretto 11.0.27)
echo ===================================================
echo JAVA_HOME: %JAVA_HOME%
echo.

echo Verifying Java version...
java -version
echo.

echo Compiling with Maven using Java 11...
mvn.cmd compile -q
if errorlevel 1 (
    echo Maven compilation failed!
    pause
    exit /b 1
)

echo.
echo Running test generation...
java -cp "target/classes;src/main/resources;target/lib/*" es.us.isa.restest.main.TestGenerationAndExecution src/main/resources/My-Example/trainticket-demo.properties

echo.
echo Done!
pause 