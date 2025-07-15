@echo off
set JAVA_HOME=C:\Users\%USERNAME%\.jdks\corretto-11.0.27
set PATH=%JAVA_HOME%\bin;%PATH%

echo ===================================================
echo Testing Maven with Java 11 without JaCoCo
echo ===================================================
echo JAVA_HOME: %JAVA_HOME%
echo.

echo Verifying Java version...
java -version
echo.

echo Running single test class without JaCoCo...
mvn.cmd test -Dtest=trainticket_twostage_test.TrainTicketTwoStageTest_1752550871957.POST_api_v1_adminrouteservice_adminroute_1 -DfailIfNoTests=false -Dmaven.jacoco.skip=true -Dmaven.surefire.debug=false

echo.
echo Checking Allure results...
if exist "target\allure-results" (
    echo Allure results directory exists
    dir target\allure-results
) else (
    echo No Allure results directory found
)

echo.
echo Done!
pause 