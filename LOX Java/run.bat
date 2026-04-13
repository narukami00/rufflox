@echo off
javac com/compilerdesign/lox/*.java
if %errorlevel% neq 0 exit /b %errorlevel%
java com.compilerdesign.lox.Lox %1
