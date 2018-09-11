@echo off

echo Building native...
call make_native.bat

echo Building java...
call make_java.bat

echo Build complete.
