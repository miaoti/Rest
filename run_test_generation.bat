@echo off
echo Starting Train-ticket Two-Stage Test Generation...
echo.

REM Run java command directly (System.exit should handle termination)
java -cp "target/classes;target/dependency/*" es.us.isa.restest.main.TestGenerationAndExecution src/main/resources/My-Example/trainticket-demo.properties

REM Check if timeout occurred
if %ERRORLEVEL% == 0 (
    echo.
    echo Test generation completed successfully!
) else (
    echo.
    echo Test generation timed out or failed. Checking generated files...
)

REM Show generated test files
echo.
echo Generated test files:
dir src\test\java\TrainTicket* /B 2>nul

REM Generate Allure report if results exist
if exist "target\allure-results\*.json" (
    echo.
    echo Generating Allure report...
    allure\bin\allure.bat generate target\allure-results --clean -o target\allure-report
    echo Allure report generated in target\allure-report
    echo Open target\allure-report\index.html to view the report
) else (
    echo.
    echo No Allure results found - skipping report generation
)

echo.
echo Process complete.
pause 