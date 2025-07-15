@echo off
set JAVA_HOME=C:\Users\%USERNAME%\.jdks\corretto-11.0.27
set PATH=%JAVA_HOME%\bin;%PATH%

echo ===================================================
echo Simple Debug Test
echo ===================================================

echo Java version:
java -version

echo.
echo Maven version:
mvn.cmd -version

echo.
echo Checking if test class exists:
if exist "src\test\java\trainticket_twostage_test\TrainTicketTwoStageTest_1752552831126\POST_api_v1_adminrouteservice_adminroute_1.java" (
    echo ✓ Test class file exists
) else (
    echo ✗ Test class file NOT found
)

echo.
echo Checking compiled test class:
if exist "target\test-classes\trainticket_twostage_test\TrainTicketTwoStageTest_1752552831126\POST_api_v1_adminrouteservice_adminroute_1.class" (
    echo ✓ Compiled test class exists
) else (
    echo ✗ Compiled test class NOT found
)

echo.
echo Compiling test classes...
mvn.cmd test-compile -q

echo.
echo Checking compiled test class again:
if exist "target\test-classes\trainticket_twostage_test\TrainTicketTwoStageTest_1752552831126\POST_api_v1_adminrouteservice_adminroute_1.class" (
    echo ✓ Compiled test class now exists
) else (
    echo ✗ Compiled test class still NOT found
)

echo.
echo Trying Maven test with minimal options...
mvn.cmd test -Dtest=*POST_api_v1_adminrouteservice_adminroute_1 -DfailIfNoTests=false

pause 