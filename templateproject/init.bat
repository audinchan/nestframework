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
set /p project=��������ĿСд����:
if "%project%"=="" goto error

set pref=
set /p pref=�������ǰ׺[%prefix%]:
if not "%pref%"=="" set prefix=%pref%

:confm
set confm=
set /p confm=�Ƿ��ʼ����Ŀ[q��ʾ�˳���Ĭ�ϼ���]:
if "%confm%"=="q" goto quiet
if "%confm%"=="Q" goto quiet


:run
echo ���ڳ�ʼ����Ŀ...
echo ��Ŀ����:%project%
echo ��ǰ׺:%prefix%
java -jar lib\nest-tools.jar demopackage=%prefix% templateproject=%project% 

%3 %4 %5 %6 %7
echo ��ʼ������.
goto end

:error
echo ʹ�÷���:
echo "init <��ĿСд����>���磺init test"
echo "���� init <��ǰ׺> <��ĿСд����>���磺init com.becom test"

:end
pause

:quiet
