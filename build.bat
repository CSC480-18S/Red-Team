@echo off

cd %~dp0\"Database\Dictionary"
start cmd.exe /c "mvnw spring-boot^:run"

cd %~dp0\"Database\GameDB"
start cmd.exe /c "mvnw spring-boot^:run"

echo Waiting to start server backend
echo Press enter when both databases have completed starting
pause

cd %~dp0\"Server Backend"
start cmd.exe /c "npm "start" run"

cd %~dp0\"Stats Backend"
start cmd.exe /c "npm "start" run"