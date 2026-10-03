@echo off
setlocal
where gradle >nul 2>nul
if %ERRORLEVEL% EQU 0 (gradle %* & exit /b %ERRORLEVEL%)
set CACHE=%USERPROFILE%\.gradle\wrapper\dists\gradle-8.9-bin
set DIST_URL=https://services.gradle.org/distributions/gradle-8.9-bin.zip
if not exist "%CACHE%\gradle-8.9\bin\gradle.bat" (
  where curl >nul 2>nul || (echo Gradle not found and curl is required to bootstrap Gradle 8.9.& exit /b 1)
  if not exist "%CACHE%" mkdir "%CACHE%"
  curl --fail --location --retry 2 "%DIST_URL%" -o "%CACHE%\gradle.zip"
  powershell -NoProfile -Command "Expand-Archive -Force '%CACHE%\gradle.zip' '%CACHE%'"
  del "%CACHE%\gradle.zip"
)
call "%CACHE%\gradle-8.9\bin\gradle.bat" %*
exit /b %ERRORLEVEL%
