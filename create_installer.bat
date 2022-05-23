@echo off
echo Packaging the application...
call mvn clean package javafx:jlink -Dmaven.javadoc.skip=true || goto ERROR
echo Creating installer... this may take a while!
cd target
call jpackage --input .\wargames --runtime-image .\wargames --name wargames --main-jar ..\Wargames-1.0-SNAPSHOT.jar --main-class edu.ntnu.arunang.Main --icon .\classes\gui\media\logo.ico --type exe --win-dir-chooser --win-menu --win-upgrade-uuid 5e74916a-9723-45e9-b809-445dcbadd133 --app-version 1.0.0  || goto ERROR
cd ..
echo Installer successfully created! Check the 'target' folder...
echo Press any key to exit...
:ERROR
echo Could NOT create an installer! (Is WIX Toolset installed?)
echo Press any key to exit...
pause >nul
cd ..
exit /b 2
