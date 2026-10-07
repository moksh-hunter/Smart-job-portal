@echo off
title Smart Job Portal Backend
echo ===================================================
echo           Starting Smart Job Portal Backend
echo ===================================================
echo.

:: Check Java
if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    set "PATH=C:\Program Files\Java\jdk-21\bin;%PATH%"
)

cd /d "%~dp0"

echo Java Version:
java -version
echo.
echo Starting application with Dev Profile (H2 In-Memory DB)...
echo Once started, open your browser at:
echo http://localhost:8080/swagger-ui/index.html
echo.
echo Press Ctrl+C in this window anytime to stop the server.
echo ===================================================
echo.

call mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

pause
