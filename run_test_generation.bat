@echo off
echo Running RESTest Generation...
java -cp "target/classes;src/main/resources;target/lib/*" es.us.isa.restest.main.TestGenerationAndExecution
echo Done. 