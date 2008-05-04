@echo off

set prefix=%1
set project=%2

if "%project%"=="" goto setvar
goto doit

:setvar
set prefix=com.becom
set project=%1


:doit
if "%project%"=="" goto input
goto run

:input
set project=
set /p project=请输入项目小写名称:
if "%project%"=="" goto error

set pref=
set /p pref=请输入包前缀[%prefix%]:
if not "%pref%"=="" set prefix=%pref%

:confm
set confm=
set /p confm=是否初始化项目[q表示退出，默认继续]:
if "%confm%"=="q" goto quiet
if "%confm%"=="Q" goto quiet


:run
echo 正在初始化项目...
echo 项目名称:%project%
echo 包前缀:%prefix%
java -jar lib\nest-tools.jar demopackage=%prefix% templateproject=%project% 

%3 %4 %5 %6 %7
echo 初始化结束.
goto end

:error
echo 使用方法:
echo "init <项目小写名称>，如：init test"
echo "或者 init <包前缀> <项目小写名称>，如：init com.becom test"

:end
pause

:quiet
