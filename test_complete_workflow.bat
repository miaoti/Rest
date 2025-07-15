@echo off
set JAVA_HOME=C:\Users\%USERNAME%\.jdks\corretto-11.0.27
set PATH=%JAVA_HOME%\bin;%PATH%

echo ===================================================
echo Complete Test: Generation + Execution with Path Fix
echo ===================================================
echo.

echo Step 1: Running test generation...
mvn.cmd exec:java -Dexec.mainClass="es.us.isa.restest.main.TestGenerationAndExecution" -Dexec.args="src/main/resources/My-Example/trainticket-demo.properties" -q

echo.
echo Step 2: Checking results...
echo Latest generated tests:
for /f %%i in ('dir src\test\java\trainticket_twostage_test\ /o:d /b 2^>nul') do set "latest=%%i"
if defined latest (
    echo Directory: !latest!
    if exist "src\test\java\trainticket_twostage_test\!latest!" (
        echo Test files:
        dir "src\test\java\trainticket_twostage_test\!latest!\*.java" /b 2>nul
    )
)

echo.
echo Step 3: Checking Allure results...
if exist "target\allure-results" (
    echo Allure results:
    dir target\allure-results 2>nul
) else (
    echo No Allure results directory
)

if exist "target\allure-reports" (
    echo Allure reports:
    dir target\allure-reports 2>nul
) else (
    echo No Allure reports directory
)

echo.
echo ===================================================
echo Workflow complete!
echo ===================================================
pause 