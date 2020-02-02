@echo off
set d=%~dp0
java -Xmx400m -cp "%d%myapp.jar;%d%libs/mylib.jar" my.main.Class %*
