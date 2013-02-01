::关闭回显
@echo off

	echo. 删除所有文件
	:: rmdir /s /q apk
	rmdir /s /q res
	rmdir /s /q res
	:: rmdir /s /q assets
	rmdir /s /q bin
	rmdir /s /q src
	rmdir /s /q libs
	rmdir /s /q ext
	del AndroidManifest.xml
	del classes.dex
	del map.txt
	del log.txt
