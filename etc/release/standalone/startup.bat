@echo off
set DIRNAME=.\
set PROGARGS=
if exist %DIRNAME%\widgetDatabase (
	set PROGARGS="initDB=false"
)
java -classpath ./lib/*;./build/webapp/wookie/WEB-INF/lib/*;./build/webapp/wookie/WEB-INF/classes org.apache.wookie.server.Start %PROGARGS%