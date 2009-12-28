drop database if exists widgetdb;
create database if not exists widgetdb DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use widgetdb;

CREATE TABLE `Widget` (
  `id` int(11) NOT NULL auto_increment,
  `height` int(11) default NULL,
  `width` int(11) default NULL,
  `maximize` varchar(1) default NULL,
  `guid` varchar(255) NOT NULL,
  `widget_author` varchar(255) default NULL,
  `widget_author_email` varchar(320) default NULL,
  `widget_author_href` text default NULL,
  `widget_version` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `WidgetIcon` (
  `id` int(11) NOT NULL auto_increment,
  `src` text default NULL,
  `height` int(11) default NULL,
  `width` int(11) default NULL,
  `lang` varchar(255) default NULL,
  `widget_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKwidgeticonwidget` (`widget_id`),
  CONSTRAINT `FKwidgeticonwidgetc` FOREIGN KEY (`widget_id`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `License` (
  `id` int(11) NOT NULL auto_increment,
  `href` text default NULL,
  `text` longtext default NULL,
  `dir` varchar(255) default NULL,
  `lang` varchar(255) default NULL,
  `widget_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKwidgetlicensewidget` (`widget_id`),
  CONSTRAINT `FKwidgetlicensewidgetc` FOREIGN KEY (`widget_id`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `Name` (
  `id` int(11) NOT NULL auto_increment,
  `name` longtext default NULL,
  `shortName` varchar(255) default NULL,
  `lang` varchar(255) default NULL,
  `dir` varchar(255) default NULL,
  `widget_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKwidgetnamewidget` (`widget_id`),
  CONSTRAINT `FKwidgetnamewidgetc` FOREIGN KEY (`widget_id`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `Description` (
  `id` int(11) NOT NULL auto_increment,
  `content` longtext default NULL,
  `lang` varchar(255) default NULL,
  `dir` varchar(255) default NULL,
  `widget_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKwidgetdescriptionwidget` (`widget_id`),
  CONSTRAINT `FKwidgetdescriptionwidgetc` FOREIGN KEY (`widget_id`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `StartFile` (
  `id` int(11) NOT NULL auto_increment,
  `url` longtext default NULL,
  `charset` varchar(255) default NULL,
  `lang` varchar(255) default NULL,
  `widget_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKwidgetstartwidget` (`widget_id`),
  CONSTRAINT `FKwidgetstartwidgetc` FOREIGN KEY (`widget_id`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `WidgetInstance` (
  `id` int(11) NOT NULL auto_increment,
  `apikey` varchar(255) NOT NULL,
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

CREATE TABLE `PreferenceDefault` (
    `id` int(11) NOT NULL auto_increment,
    `widget_id` int(11) NOT NULL,
    `preference` varchar(255) NOT NULL,
    `value` varchar(1024) NOT NULL,
    `readOnly` char(1) default 'f',
    PRIMARY KEY(`id`),
    KEY `FK_preferencesdefault_1` (`widget_id`),
    CONSTRAINT `FK_preferencesdefault_1` FOREIGN KEY (`widget_id`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `Participant` (
  `id` int(11) NOT NULL auto_increment,
  `participant_id` varchar(255) NOT NULL default '',
  `participant_display_name` varchar(255) NOT NULL default '',
  `participant_thumbnail_url` varchar(1024) default '',
  `sharedDataKey` varchar(255) NOT NULL default '',
  `widgetGuid` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
);

CREATE TABLE `Preference` (
  `id` int(11) NOT NULL auto_increment,
  `widget_instance_id` int(11) NOT NULL,
  `dkey` varchar(255) default NULL,
  `dvalue` longtext,
  `readOnly` char(1) default'f',
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

CREATE TABLE `ApiKey` (
  `id` int(11) NOT NULL auto_increment,
  `value` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
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

CREATE TABLE `Feature` (
	`id` int(11) NOT NULL auto_increment,
	`featureName` varchar(255) NOT NULL,
	`required` char(1) default 'T',
	`widget_id` int(11) NOT NULL,
	PRIMARY KEY  (`id`),
	KEY `FKEASDD9E2C2E1E5` (`widget_id`),
	CONSTRAINT `FKEASDD9E2C2E1E5` FOREIGN KEY (`widget_id`) REFERENCES `Widget` (`id`)
);

CREATE TABLE `Param` (
  `id` int(11) NOT NULL auto_increment,
  `feature_id` int(11) NOT NULL,
  `parameterName` varchar(255) NOT NULL,
  `parameterValue` varchar(255) NOT NULL,  
  PRIMARY KEY  (`id`),
  KEY `FKAA9137E222222` (`feature_id`),
  CONSTRAINT `FKAA9137E222222` FOREIGN KEY (`feature_id`) REFERENCES `Feature` (`id`)
);

CREATE TABLE `ServerFeature` (
	`id` int(11) NOT NULL auto_increment,
	`featureName` varchar(255) NOT NULL,
	`className` varchar(255) NOT NULL,
	PRIMARY KEY  (`id`)
);

CREATE TABLE `Token` (
	`id` int(11) NOT NULL auto_increment,
	`requestUrl` varchar(255) NOT NULL,
	`authzUrl` varchar(255) NOT NULL,
	`requestToken` varchar(255) NOT NULL,
	`accessToken` varchar(255) NOT NULL,
	`tokenSecret` varchar(255) NOT NULL,
	`widget_instance_id` int(11) NOT NULL,
    KEY `TKWID` (`widget_instance_id`),
    CONSTRAINT `FKTKWID` FOREIGN KEY (`widget_instance_id`) REFERENCES `WidgetInstance` (`id`),
	PRIMARY KEY  (`id`)    
);


INSERT INTO `Widget` VALUES (1,350,500,'f','http://notsupported','Paul Sharples','p.sharples@bolton.ac.uk','http://iec.bolton.ac.uk', 'v1.0');
INSERT INTO `Name` VALUES(1, 'Unsupported widget widget','Unsupported',NULL,NULL,1);
INSERT INTO `Description` VALUES(1, 'This widget is a placeholder for when no corresponding widget is found for a given type',NULL,NULL,1);
INSERT INTO `StartFile` VALUES(1,'/wookie/wservices/notsupported/index.htm',NULL,NULL,1);
INSERT INTO `WidgetDefault` VALUES ('unsupported',1);
INSERT INTO `WidgetService` VALUES (1,'unsupported'),(2,'chat'),(3,'games'),(4,'voting'),(5,'weather');
INSERT INTO `WidgetType` VALUES (1,1,'unsupported');
INSERT INTO `WidgetIcon` VALUES (1,'/wookie/shared/images/defaultwidget.png',80,80,'en',1);
INSERT INTO `Whitelist` VALUES (1,'http://127.0.0.1'),(2,'http://localhost'),(3,'http://feeds.bbc.co.uk/weather/feeds/rss');
INSERT INTO `ServerFeature` VALUES (1,'http://wave.google.com','org.apache.wookie.feature.wave.WaveAPIImpl'),(2,'http://www.getwookie.org/usefeature/polling','org.apache.wookie.feature.polling.impl.WookiePollingImpl'),(3,'feature:a9bb79c1','org.apache.wookie.feature.conformance.Test');
INSERT INTO `ApiKey` VALUES (1,'TEST','test@127.0.0.1');