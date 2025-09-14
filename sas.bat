@echo off
echo [INFO] Setting up environment...

:: Set paths
set "SRC=src"
set "BIN=bin"
set "LIB=lib"
set "CP=%LIB%\*;%BIN%"

:: Create bin directory if it does not exist
if not exist "%BIN%" (
    mkdir "%BIN%"
    echo [INFO] Created '%BIN%' directory.
)

:: Ensure lib directory exists
if not exist "%LIB%" (
    echo [ERROR] 'lib' directory not found!
    pause
    exit /b
)

echo [INFO] Compiling Java source files...

:: Compile Java files
javac -cp "%CP%" -d "%BIN%" ^
%SRC%\Main.java ^
%SRC%\db\DBConnection.java ^
%SRC%\gui\AdminPanel.java ^
%SRC%\gui\AttendanceViewer.java ^
%SRC%\gui\EnrollmentForm.java ^
%SRC%\gui\LoginPage.java ^
%SRC%\qr\QRGenerator.java ^
%SRC%\qr\QRScanner.java ^
%SRC%\model\Student.java ^
%SRC%\model\AttendanceRecord.java

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b
)

echo [INFO] Compilation successful.
echo [INFO] Running application...

:: Run the app
java -cp "%CP%" Main

pause
