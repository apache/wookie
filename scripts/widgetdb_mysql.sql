drop database if exists widgetdb;
create database if not exists widgetdb DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use widgetdb;

CREATE TABLE `Widget` (
  `id` int(11) NOT NULL auto_increment,
  `widget_title` varchar(255) default NULL,
  `url` varchar(255) default NULL,
  `height` int(11) default NULL,
  `width` int(11) default NULL,
  `maximize` varchar(1) default NULL,
  `guid` varchar(255) NOT NULL,
  `widget_description` varchar(255) default NULL,
  `widget_author` varchar(255) default NULL,
  `widget_icon_location` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `WidgetInstance` (
  `id` int(11) NOT NULL auto_increment,
  `userId` varchar(255) NOT NULL,  
  `sharedDataKey` varchar(255) default NULL,
  `nonce` varchar(255) default NULL,
  `idKey` varchar(255) NOT NULL,
  `opensocialToken` longtext NOT NULL,
  `widget_id` int(11) NOT NULL,
  `updated` char(1) default NULL,
  `shown` char(1) default NULL,
  `hidden` char(1) default NULL,
  `locked` char(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKEA564ED9E2C2E1E5` (`widget_id`),
  CONSTRAINT `FKEA564ED9E2C2E1E5` FOREIGN KEY (`widget_id`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `Preference` (
  `id` int(11) NOT NULL auto_increment,
  `widget_instance_id` int(11) NOT NULL,
  `dkey` varchar(255) default NULL,
  `dvalue` longtext,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_composite` (`widget_instance_id`,`dkey`),
  KEY `FKC5E6A8DB4215462E` (`widget_instance_id`),
  CONSTRAINT `FKC5E6A8DB4215462E` FOREIGN KEY (`widget_instance_id`) REFERENCES `WidgetInstance` (`id`)
);

CREATE TABLE `SharedData` (
  `id` int(11) NOT NULL auto_increment,
  `sharedDataKey` varchar(255) default NULL,
  `dkey` varchar(255) default NULL,
  `dvalue` longtext,
  `widgetGuid` varchar (255) NOT NULL,
  PRIMARY KEY  (`id`), 
  KEY `sharedDataIndex` (`sharedDataKey`)
);

CREATE TABLE `WidgetDefault` (
  `widgetContext` varchar(128) NOT NULL,
  `widgetId` int(11) default NULL,
  PRIMARY KEY  (`widgetContext`),
  KEY `FK_widgetdefault_1` (`widgetId`),
  CONSTRAINT `FK_widgetdefault_1` FOREIGN KEY (`widgetId`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `WidgetService` (
  `id` int(11) NOT NULL auto_increment,
  `service_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `WidgetType` (
  `id` int(11) NOT NULL auto_increment,
  `widget_id` int(11) NOT NULL,
  `widget_context` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKAA9137EE2C2E1E5` (`widget_id`),
  CONSTRAINT `FKAA9137EE2C2E1E5` FOREIGN KEY (`widget_id`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `Whitelist` (
  `id` int(11) unsigned NOT NULL auto_increment,
  `fUrl` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `Post` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` varchar(80) NOT NULL,
  `parent_id` int(11) default '0',
  `title` varchar(255) NOT NULL,
  `content` mediumtext,
  `publish_date` datetime NOT NULL,
  `update_date` datetime default NULL,
  `sharedDataKey` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
);


INSERT INTO `Widget` VALUES (1,'Unsupported widget widget','/wookie/wservices/www.tencompetence.org/widgets/default/notsupported/index.htm',350,500,'f','http://www.tencompetence.org/widgets/default/notsupported','This widget is a placeholder for when no corresponding widget is found for a given type','Paul Sharples','/wookie/shared/images/defaultwidget.png'),(2,'Default chat widget','/wookie/wservices/www.tencompetence.org/widgets/default/chat/index.htm',358,500,'f','http://www.tencompetence.org/widgets/default/chat','This widget provides a simple chat or Instant messaging facility','Paul Sharples','/wookie/shared/images/chat.png'),(3,'Default discussion/forum widget','/wookie/wservices/www.tencompetence.org/widgets/default/forum/index.htm',350,520,'t','http://www.tencompetence.org/widgets/default/forum','This widget provides a threaded discussion forum facility','Paul Sharples','/wookie/shared/images/forum.png'),(4,'Default vote widget','/wookie/wservices/www.tencompetence.org/widgets/default/vote/index.htm',350,500,'f','http://www.tencompetence.org/widgets/default/vote','This widget provides a voting facility','Paul Sharples','/wookie/shared/images/vote.png'),(5,'Natter','/wookie/wservices/www.getwookie.org/widgets/natter/index.htm',383,255,'F','http://www.getwookie.org/widgets/natter','A simple chat widget','Scott Wilson','/wookie/wservices/www.getwookie.org/widgets/natter/Icon.png'),(6,'Weather','/wookie/wservices/www.getwookie.org/widgets/weather/index.htm',125,125,'F','http://www.getwookie.org/widgets/weather','A simple weather widget','Scott Wilson','/wookie/wservices/www.getwookie.org/widgets/weather/icon.png');
INSERT INTO `WidgetDefault` VALUES ('unsupported',1),('chat',2),('discussion',3),('forum',3),('vote',4),('natter',5),('weather',6);
INSERT INTO `WidgetService` VALUES (1,'unsupported'),(2,'chat'),(3,'discussion'),(4,'forum'),(5,'vote'),(6,'natter'),(7,'weather');
INSERT INTO `WidgetType` VALUES (1,1,'unsupported'),(2,2,'chat'),(3,3,'forum'),(4,3,'discussion'),(5,4,'vote'),(6,5,'natter'),(7,6,'weather');
INSERT INTO `Whitelist` VALUES (1,'http://127.0.0.1'),(2,'http://localhost'),(3,'http://feeds.bbc.co.uk/weather/feeds/rss');
