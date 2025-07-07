@echo off
echo ===================================================
echo Testing Two-Stage LLM + Semantic Parameter Generation
echo ===================================================
echo.

echo Cleaning previous test results...
if exist "src\test\java\trainticket" rmdir /s /q "src\test\java\trainticket"
if exist "allure-results" rmdir /s /q "allure-results"
if exist "target\test-results" rmdir /s /q "target\test-results"

echo.
echo Starting test generation with enhanced logging...
echo.

echo Running Maven with two-stage parameter generation...
mvn clean compile exec:java -Dexec.mainClass="es.us.isa.restest.main.TestGenerationAndExecution" -Dexec.args="src/main/resources/My-Example/trainticket-demo.properties" -Dlogging.level.es.us.isa.restest=DEBUG

echo.
echo ===================================================
echo Test generation completed!
echo ===================================================
echo.

echo Generated test files:
if exist "src\test\java\trainticket" (
    dir /b "src\test\java\trainticket\*.java"
) else (
    echo No test files found!
)

echo.
echo Check the console output above for:
echo 1. LLM seed value generation (Stage 1)
echo 2. Semantic expansion details (Stage 2)  
echo 3. Multiple test case variants created
echo 4. Parameter selection based on variant index
echo.

pause 