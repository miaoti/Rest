@echo off
set JAVA_HOME=C:\Users\%USERNAME%\.jdks\corretto-11.0.27
set PATH=%JAVA_HOME%\bin;%PATH%

echo ===================================================
echo Testing Maven with Java 11 for single test class
echo ===================================================
echo JAVA_HOME: %JAVA_HOME%
echo.

echo Verifying Java version...
java -version
echo.

echo Testing compilation...
mvn.cmd compile -q
if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compiling test classes...
mvn.cmd test-compile -q

echo.
echo Running single test class...
mvn.cmd test -Dtest=trainticket_twostage_test.TrainTicketTwoStageTest_1752550871957.POST_api_v1_adminrouteservice_adminroute_1 -DfailIfNoTests=false

echo.
echo Done!
pause 