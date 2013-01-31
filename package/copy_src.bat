rmdir /s /q apk
md apk

del AndroidManifest.xml /Q
rmdir /s /q res
rmdir /s /q src
rmdir /s /q assets
rmdir /s /q libs



ECHO. LOG--第1步 复制文件res、assets、AndroidManifest.xml、src
ECHO. LOG--复制res\anim
xcopy ..\res\anim\*.* res\anim\ /s /e /Y
ECHO. LOG--复制res\drawable
xcopy ..\res\drawable\*.* res\drawable\ /s /e /Y
ECHO. LOG--复制res\drawable-hdpi
xcopy ..\res\drawable-hdpi\*.* res\drawable-hdpi\ /s /e /Y
ECHO. LOG--复制res\drawable-mdpi
xcopy ..\res\drawable-mdpi\*.* res\drawable-mdpi\ /s /e /Y
ECHO. LOG--复制res\drawable-land
xcopy ..\res\drawable-land\*.* res\drawable-land\ /s /e /Y
ECHO. LOG--复制res\layout
xcopy ..\res\layout\*.* res\layout\ /s /e /Y
ECHO. LOG--复制res\layout-hdpi
xcopy ..\res\layout-hdpi\*.* res\layout-hdpi\ /s /e /Y
ECHO. LOG--复制res\layout-land
xcopy ..\res\layout-land\*.* res\layout-land\ /s /e /Y
ECHO. LOG--复制res\menu
xcopy ..\res\menu\*.* res\menu\ /s /e /Y
ECHO. LOG--复制res\raw
xcopy ..\res\raw\*.* res\raw\ /s /e /Y
ECHO. LOG--复制res\values
xcopy ..\res\values\*.* res\values\ /s /e /Y
ECHO. LOG--复制res\values-en
xcopy ..\res\values-en\*.* res\values-en\ /s /e /Y

if exist ..\res\values-zh-rHK (
	xcopy ..\res\values-zh-rHK\*.* res\values-zh-rHK\ /s /e /Y
)

if exist ..\res\values-zh-rTW (
	xcopy ..\res\values-zh-rTW\*.* res\values-zh-rTW\ /s /e /Y
)

ECHO. LOG--复制res\xml
xcopy ..\res\xml\*.* res\xml\ /s /e /Y

xcopy ..\res\color\*.* res\color\ /s /e /Y

xcopy ..\assets\*.* assets\ /s /e /Y

xcopy ..\libs\*.* libs\ /s /e /Y

if not exist lib (
	mkdir lib
)
xcopy libs\armeabi\*.so lib\armeabi\ /Y
xcopy libs\mips\*.so lib\mips\ /Y
xcopy libs\x86\*.so lib\x86\ /Y

copy ..\AndroidManifest.xml AndroidManifest.xml /Y
xcopy ..\src\*.* src\ /s /e /Y

:: copy keystore
xcopy ..\demokeystore . /Y