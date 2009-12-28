INSERT INTO Widget VALUES (1,'Paul Sharples','psharples@bolton.ac.uk','http://iec.bolton.ac.uk','http://notsupported',350,500,'f','v1.0');
INSERT INTO Name VALUES(1, 'Unsupported widget widget','Unsupported',NULL,NULL,1);
INSERT INTO Description VALUES(1, 'This widget is a placeholder for when no corresponding widget is found for a given type',NULL,NULL,1);
INSERT INTO StartFile VALUES(1,'/wookie/wservices/notsupported/index.htm',NULL,NULL,1);
INSERT INTO WidgetDefault VALUES ('unsupported',1);
INSERT INTO WidgetService VALUES (1,'unsupported'),(2,'chat'),(3,'games'),(4,'voting'),(5,'weather');
INSERT INTO WidgetType VALUES (1,1,'unsupported');
INSERT INTO WidgetIcon VALUES (1,'/wookie/shared/images/defaultwidget.png',80,80,'en',1);
INSERT INTO Whitelist VALUES (1,'http://127.0.0.1'),(2,'http://localhost'),(3,'http://feeds.bbc.co.uk/weather/feeds/rss');
INSERT INTO ServerFeature VALUES (1,'org.apache.wookie.feature.wave.WaveAPIImpl','http://wave.google.com'),(2,'org.apache.wookie.feature.polling.impl.WookiePollingImpl','http://www.getwookie.org/usefeature/polling'), (3,'org.apache.wookie.feature.conformance.Test','feature:a9bb79c1');
INSERT INTO ApiKey VALUES (1,'TEST','test@127.0.0.1')