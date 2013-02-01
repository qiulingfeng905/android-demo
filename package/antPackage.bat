::*********************************
::*功能：打包
::*作者：captain_miao
::*时间：2012-12-23
::*限制：时间格式会导致语法错误，设置成：yyyy-MM-dd
::*********************************

::关闭回显
@echo off
title package_android
color 0A

set Obscure=%1

if "%Obscure%"=="" (
	set Obscure=Y
	echo default way: Obscure=Y.
)

if %Obscure%==N (
	echo: ***********************************************************
	echo: *    0、测试版本
	echo: *
	echo: *    1、Debug模式
	echo: *
	echo: *    2、打印Log模式
	echo: *
	echo: *    3、混淆模式
	echo: ***********************************************************
)else (
	echo: ***********************************************************
	echo: *    0、正式版本
	echo: *
	echo: *    1、关闭Debug模式
	echo: *
	echo: *    2、关闭Log打印
	echo: *
	echo: *    3、混淆模式
	echo: ***********************************************************
)



set /a Error_Number=0

::删除源码
call delete.bat > log.txt
call :checkStatus %errorlevel% "call delete.bat"

:: 拷贝源码
call copy_src.bat >> log.txt
call :checkStatus %errorlevel% "call copy_src.bat"


::默认android2.2.jar版本

echo compile classex.dex
rem 设置打包的文件和目录
for /f "tokens=1-4 delims=-/ " %%i IN ("%date%") DO (
	set year=%%i
	set month=%%j
	set day=%%k
)


for /f "tokens=1-4 delims=:." %%i IN ("%time%") DO (
	set hour=%%i
	set minute=%%j
	set second=%%k
	set centisecond=%%l
)


set formatDate=%year%-%month%-%day%
set currTime=%hour%%minute%%second%
rem 去空格
set "formatDate=%formatDate: =%"
set "currTime=%currTime: =%"
rem set formatDate=%date:~0,4%-%date:~5,2%-%date:~8,2%
rem set currTime=%time:~0,2%%time:~3,2%%time:~6,2%
set ttidFile=ttid_list.txt
set fileDir=apk\%formatDate%

call ant -f  build_compile.xml
call :checkStatus %errorlevel% "ant -f build_compile.xml"



echo replace ttid and sign
::版本从ttid_list.txt中读取
set version=0
set release=release
if  %Obscure%==N (
	set release=debug
)

if exist %ttidFile% (
	for /f "delims=" %%i in (%ttidFile%)  do (

			set version=%%i
			echo version=%%i
			goto x1
	)
	:x1
	for /f "skip=1 delims=" %%i in (%ttidFile%)  do (
			echo channel=%%i_%version%
			copy ..\assets\ttid.dat assets\ttid.dat /Y
			ant -f fast_ttid_config.xml -Dversion.ttid=%%i
			aapt p -f -u -M AndroidManifest.xml -S res -A assets -I android.jar -F apk/demo_%%i_%version%.apk
			aapt a apk/demo_%%i_%version%.apk classes.dex
			::add lib to apk
			aapt a apk/demo_%%i_%version%.apk lib/armeabi/*.so

			jarsigner -digestalg SHA1 -sigalg MD5withRSA -verbose -storepass caoqinmiao -keystore demokeystore -signedjar apk/demo_signed_%%i_%version%.apk apk/demo_%%i_%version%.apk demokeystore
			::call :checkStatus %errorlevel% "jarsigner -sign apk/demo_signed_%%i_%version%.apk"
			zipalign -v -f 4 apk/demo_signed_%%i_%version%.apk apk/demo_signed_align_%%i_%version%.apk
			::call :checkStatus %errorlevel% "jarsigner -zipalign apk/demo_signed_%%i_%version%.apk"
			jarsigner -verify apk/demo_signed_align_%%i_%version%.apk

			rename apk\demo_signed_align_%%i_%version%.apk %%i_demo_android_%version%.apk
			echo package:apk\%%i_demo_android_%version%.apk
			del "apk\demo_%%i_%version%.apk"
			del "apk\demo_signed_%%i_%version%.apk"



			if not exist %fileDir% (
				mkdir %fileDir%
			)
			copy apk\%%i_demo_android_%version%.apk "%fileDir%\%%i_demo_android_%version%_%currTime%.apk" /Y

	)
)

goto OK

:checkStatus
(
    if "%1%" == "0" (

        echo success: %2

    ) else (
		color 0c
        echo fail: %2, the Error Number: %1%
        goto :FIN

    )
    exit /b 0
)

:FIN
(
    echo "error,exit..."
	goto DONE
)


:OK
(
	echo success
	goto DONE
)
:DONE
(
	echo "package done...."
    exit /b 0
)
@pause
