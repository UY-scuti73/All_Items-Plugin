@echo off
setlocal

for %%I in ("%~dp0..") do set "ROOT_DIR=%%~fI"
set "OUTPUT_DIR=%ROOT_DIR%\Output Files"

echo "%OUTPUT_DIR%"

if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

copy /Y "%ROOT_DIR%\All_Items\target\all-items-1.0-SNAPSHOT.jar" "%OUTPUT_DIR%\All_Items.jar"
copy /Y "%ROOT_DIR%\All_Items_Lite\target\all-items-1.0-SNAPSHOT.jar" "%OUTPUT_DIR%\All_Items_Lite.jar"
copy /Y "%ROOT_DIR%\All_Mobs_Lite\target\all-items-1.0-SNAPSHOT.jar" "%OUTPUT_DIR%\All_Mobs_Lite.jar"
copy /Y "%ROOT_DIR%\All_Items_Randomized\target\all-items-1.0-SNAPSHOT.jar" "%OUTPUT_DIR%\All_Items_Randomized.jar"

echo Done copying jars.