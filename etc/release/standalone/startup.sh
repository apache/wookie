PROGARGS=
if [ -d ./widgetDatabase ]; then
    PROGARGS="initDB=false"
fi
java -classpath lib/*:build/webapp/wookie/WEB-INF/lib/*:build/webapp/wookie/WEB-INF/classes org.apache.wookie.server.Start $PROGARGS
