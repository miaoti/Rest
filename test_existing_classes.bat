@echo off
set JAVA_HOME=C:\Users\%USERNAME%\.jdks\corretto-11.0.27
set PATH=%JAVA_HOME%\bin;%PATH%

echo ===================================================
echo Testing Existing Classes with Fixed Path Logic
echo ===================================================
echo.

echo Running Maven test with JaCoCo disabled...
mvn.cmd test -Dtest=trainticket_twostage_test.TrainTicketTwoStageTest_1752552107739.POST_api_v1_adminrouteservice_adminroute_1 -DfailIfNoTests=false -Djacoco.skip=true -Dmaven.jacoco.skip=true -Djacoco.agent.skip=true -Dskip.jacoco=true

echo.
echo ===================================================
echo Checking Results
echo ===================================================

echo Latest surefire report:
for /f %%i in ('dir target\surefire-reports\*.txt /o:d /b 2^>nul') do set "latest=%%i"
if defined latest (
    echo File: !latest!
    echo Content:
    type "target\surefire-reports\!latest!" | findstr /i "run\|error\|failure\|groovy"
)

echo.
echo Allure results:
if exist "target\allure-results" (
    dir target\allure-results
) else (
    echo No Allure results
)

echo.
echo Done!
pause 