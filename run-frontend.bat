@echo off
title Smart Job Portal Frontend (React)
echo ===================================================
echo       Starting Smart Job Portal React Frontend
echo ===================================================
echo.

cd /d "%~dp0frontend"

echo Starting Vite React Dev Server...
echo Once started, open your browser at:
echo http://localhost:5173
echo.
echo Press Ctrl+C in this window anytime to stop the frontend.
echo ===================================================
echo.

npm run dev

pause
