INSERT INTO Widget VALUES (1,'Unsupported widget widget','','This widget is a placeholder for when no corresponding widget is found for a given type','Paul Sharples','/wookie/shared/images/defaultwidget.png','/wookie/wservices/notsupported/index.htm','http://notsupported',350,500,'f','v1.0');
INSERT INTO WidgetDefault VALUES ('unsupported',1);
INSERT INTO WidgetService VALUES (1,'unsupported'),(2,'chat'),(3,'games'),(4,'voting'),(5,'weather');
INSERT INTO WidgetType VALUES (1,1,'unsupported');
INSERT INTO Whitelist VALUES (1,'http://127.0.0.1'),(2,'http://localhost'),(3,'http://feeds.bbc.co.uk/weather/feeds/rss');
INSERT INTO ServerFeature VALUES (1,'org.apache.wookie.feature.wave.WaveAPIImpl','http://wave.google.com'),(2,'org.apache.wookie.feature.polling.impl.WookiePollingImpl','http://www.getwookie.org/usefeature/polling');
INSERT INTO ApiKey VALUES (1,'TEST','test@127.0.0.1')