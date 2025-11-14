call mvn clean compile
call mvn clean package assembly:single -DdescriptorId=chatroom
call mvn dependency:copy-dependencies
pause
java -jar .\target\convocraft-1.0.jar
