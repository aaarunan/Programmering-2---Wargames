@echo off
echo Packaging the application...
call mvn clean package shade:shade -Dmaven.javadoc.skip=true || goto ERROR
echo Creating installer... this may take a while!
cd target
call jpackage --input . --name Wargames --main-jar .\..\out\artifacts\Wargames_jar\Wargames.jar --main-class edu.ntnu.arunang.Main --type exe --win-dir-chooser --vendor arunang --copyright arunang --win-menu --win-shortcut --win-upgrade-uuid 863281a1-ac73-44de-a77f-d0e6dd1fcd77 --app-version 1.0.0  || goto ERROR
echo Installer successfully created! Check the 'target' folder...
echo Press any key to exit...
:ERROR
echo Creating the installer was NOT successful!
echo Press any key to exit...
pause >nul
cd ..
exit /b 2
