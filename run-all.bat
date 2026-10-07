@echo off
title Smart Job Portal (Full Stack Launcher)
echo ===================================================
echo     Launching Smart Job Portal Backend ^& Frontend
echo ===================================================
echo.

start "Smart Job Portal Backend" cmd /c "%~dp0run.bat"
timeout /t 3 /nobreak >nul
start "Smart Job Portal Frontend (React)" cmd /c "%~dp0run-frontend.bat"

echo Both servers are launching in separate windows!
echo.
echo - Frontend React App: http://localhost:5173
echo - Backend API:        http://localhost:8080/swagger-ui/index.html
echo ===================================================
timeout /t 5
