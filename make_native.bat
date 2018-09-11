@echo off

REM x64 build
REM create local environment
setlocal

REM Set VC paths
call vcvarsall.bat amd64

REM Move to build directory
cd src\cpp

REM build
call make.bat

REM copy DIR to correct place
mkdir ..\..\res\win32-x86-64\
copy libwmi.dll ..\..\res\win32-x86-64\

REM exit local environment
endlocal

REM x86 build
REM create local environment
setlocal

REM Set VC paths
call vcvarsall.bat amd64_x86

REM Move to build directory
cd src\cpp

REM build
call make.bat

REM copy DIR to correct place
mkdir ..\..\res\win32-x86\
copy libwmi.dll ..\..\res\win32-x86\

REM exit local environment
endlocal