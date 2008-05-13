drop database if exists widgetdb;
create database if not exists widgetdb DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use widgetdb;

CREATE TABLE `widget` (
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

CREATE TABLE `widgetinstance` (
  `id` int(11) NOT NULL auto_increment,
  `userId` varchar(255) NOT NULL,
  `runId` varchar(255) NOT NULL,
  `envId` varchar(255) NOT NULL,
  `serviceId` varchar(255) NOT NULL,
  `nonce` varchar(255) default NULL,
  `idKey` varchar(255) NOT NULL,
  `widget_id` int(11) NOT NULL,
  `updated` char(1) default NULL,
  `shown` char(1) default NULL,
  `hidden` char(1) default NULL,
  `locked` char(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKEA564ED9E2C2E1E5` (`widget_id`),
  CONSTRAINT `FKEA564ED9E2C2E1E5` FOREIGN KEY (`widget_id`) REFERENCES `widget` (`id`)
);

CREATE TABLE `preference` (
  `id` int(11) NOT NULL auto_increment,
  `widget_instance_id` int(11) NOT NULL,
  `dkey` varchar(255) default NULL,
  `dvalue` text,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_composite` (`widget_instance_id`,`dkey`),
  KEY `FKC5E6A8DB4215462E` (`widget_instance_id`),
  CONSTRAINT `FKC5E6A8DB4215462E` FOREIGN KEY (`widget_instance_id`) REFERENCES `widgetinstance` (`id`)
);

CREATE TABLE `shareddata` (
  `id` int(11) NOT NULL auto_increment,
  `sharedDataKey` varchar(255) default NULL,
  `dkey` varchar(255) default NULL,
  `dvalue` text,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_composite` (`sharedDataKey`,`dkey`),
  KEY `sharedDataIndex` (`sharedDataKey`)
);

CREATE TABLE `widgetdefault` (
  `widgetContext` varchar(128) NOT NULL,
  `widgetId` int(11) default NULL,
  PRIMARY KEY  (`widgetContext`),
  KEY `FK_widgetdefault_1` (`widgetId`),
  CONSTRAINT `FK_widgetdefault_1` FOREIGN KEY (`widgetId`) REFERENCES `widget` (`id`)
);

CREATE TABLE `widgetservice` (
  `id` int(11) NOT NULL auto_increment,
  `service_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `widgettype` (
  `id` int(11) NOT NULL auto_increment,
  `widget_id` int(11) NOT NULL,
  `widget_context` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKAA9137EE2C2E1E5` (`widget_id`),
  CONSTRAINT `FKAA9137EE2C2E1E5` FOREIGN KEY (`widget_id`) REFERENCES `widget` (`id`)
);

CREATE TABLE `whitelist` (
  `id` int(11) unsigned NOT NULL auto_increment,
  `fUrl` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `post` (
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


INSERT INTO `widget` VALUES (1,'Unsupported widget widget','/wookie/wservices/www.tencompetence.org/widgets/default/notsupported/index.htm',350,500,'f','http://www.tencompetence.org/widgets/default/notsupported','This widget is a placeholder for when no corresponding widget is found for a given type','Paul Sharples','/wookie/shared/images/defaultwidget.png'),(2,'Default chat widget','/wookie/wservices/www.tencompetence.org/widgets/default/chat/chat.htm',358,500,'f','http://www.tencompetence.org/widgets/default/chat','This widget provides a simple chat or Instant messaging facility','Paul Sharples','/wookie/shared/images/chat.png'),(3,'Default discussion/forum widget','/wookie/wservices/www.tencompetence.org/widgets/default/forum/forum.htm',350,520,'t','http://www.tencompetence.org/widgets/default/forum','This widget provides a threaded discussion forum faciltity','Paul Sharples','/wookie/shared/images/forum.png'),(4,'Default vote widget','/wookie/wservices/www.tencompetence.org/widgets/default/vote/vote.htm',350,500,'f','http://www.tencompetence.org/widgets/default/vote','This widget provides a voting facility','Paul Sharples','/wookie/shared/images/vote.png');
INSERT INTO `widgetdefault` VALUES ('unsupported',1),('chat',2),('discussion',3),('forum',3),('vote',4);
INSERT INTO `widgetservice` VALUES (1,'unsupported'),(2,'chat'),(3,'discussion'),(4,'forum'),(5,'vote');
INSERT INTO `widgettype` VALUES (1,1,'unsupported'),(2,2,'chat'),(3,3,'forum'),(4,3,'discussion'),(5,4,'vote');
INSERT INTO `whitelist` VALUES (1,'http://127.0.0.1'),(2,'http://localhost');
