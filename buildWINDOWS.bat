cd %~dp0
cd "Database\Dictionary"
start cmd.exe /k "mvnw spring-boot^:run"
cd %~dp0
cd "Database\GameDB"
start cmd.exe /k "mvnw spring-boot^:run"
cd %~dp0
cd "Server Backend"
start cmd.exe /k "npm "start" run"
cd %~dp0
cd "Stats Backend"
start cmd.exe /k "npm "start" run"