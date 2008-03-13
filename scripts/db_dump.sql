-- MySQL dump 10.11
--
-- Host: localhost    Database: widgetDB
-- ------------------------------------------------------
-- Server version	5.0.51a-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `post` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `user_id` varchar(80) NOT NULL,
  `parent_id` int(11) default '0',
  `title` varchar(1024) NOT NULL,
  `content` mediumtext,
  `publish_date` datetime NOT NULL,
  `update_date` datetime default NULL,
  `sharedDataKey` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'fred',0,'dcd','cvcvdfvdf','2008-07-06 00:00:00',NULL,'dfdfd'),(5,'dd',-1,'dcd','dfdf','2008-07-08 00:00:00',NULL,'xcxc'),(6,'l2',-1,'dfdfdfdfd','ddfdf','2008-03-11 12:48:41',NULL,'0-env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745-service-32e594ba-3a5e-214c-60a3-9a1efc130aeb');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preference`
--

DROP TABLE IF EXISTS `preference`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `preference` (
  `id` int(11) NOT NULL auto_increment,
  `widget_instance_id` int(11) NOT NULL,
  `dkey` varchar(255) default NULL,
  `dvalue` text,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_composite` (`widget_instance_id`,`dkey`),
  KEY `FKC5E6A8DB4215462E` (`widget_instance_id`),
  CONSTRAINT `FKC5E6A8DB4215462E` FOREIGN KEY (`widget_instance_id`) REFERENCES `widgetinstance` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `preference`
--

LOCK TABLES `preference` WRITE;
/*!40000 ALTER TABLE `preference` DISABLE KEYS */;
INSERT INTO `preference` VALUES (37,39,'LDUsername','tenc-teacher'),(38,40,'LDUsername','tenc-teacher'),(39,41,'LDUsername','tenc-teacher'),(40,42,'LDUsername','tenc-teacher'),(41,43,'LDUsername','tenc-learner'),(42,44,'LDUsername','tenc-learner'),(43,45,'LDUsername','tenc-learner'),(44,46,'LDUsername','tenc-learner'),(45,48,'LDUsername','t2'),(46,47,'LDUsername','t1'),(47,49,'LDUsername','t2'),(48,50,'LDUsername','t1'),(49,51,'LDUsername','t2'),(50,52,'LDUsername','t1'),(51,53,'LDUsername','t2'),(52,54,'LDUsername','t1'),(53,55,'LDUsername','l4'),(54,56,'LDUsername','l4'),(55,57,'LDUsername','l4'),(56,58,'LDUsername','l4'),(57,59,'LDUsername','l3'),(58,60,'LDUsername','l3'),(59,61,'LDUsername','l3'),(60,62,'LDUsername','l3'),(61,63,'LDUsername','l1'),(62,64,'LDUsername','l1'),(63,65,'LDUsername','l1'),(64,66,'LDUsername','l1'),(65,67,'LDUsername','paul'),(66,68,'LDUsername','l2'),(67,69,'LDUsername','l2'),(68,70,'LDUsername','l2'),(69,71,'LDUsername','l2');
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shareddata`
--

DROP TABLE IF EXISTS `shareddata`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `shareddata` (
  `id` int(11) NOT NULL auto_increment,
  `sharedDataKey` varchar(255) default NULL,
  `dkey` varchar(255) default NULL,
  `dvalue` text,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_composite` (`sharedDataKey`,`dkey`),
  KEY `sharedDataIndex` (`sharedDataKey`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `shareddata`
--

LOCK TABLES `shareddata` WRITE;
/*!40000 ALTER TABLE `shareddata` DISABLE KEYS */;
INSERT INTO `shareddata` VALUES (7,'0-env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745-service-036dc625-3ba2-effd-4727-53df0c9fe492','defaultChatPresence','<member>l4<member><member>tenc-learner<member><member>l3<member><member>l2<member>'),(8,'0-env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745-service-036dc625-3ba2-effd-4727-53df0c9fe492','defaultChatLog','<chat>tenc-teacher: dfgdfgd<chat><chat><User tenc-teacher has LOCKED the chatroom><chat><chat><User tenc-teacher has UNLOCKED the chatroom><chat><chat><User tenc-teacher has joined the chatroom><chat><chat><User l1 has joined the chatroom><chat><chat><User l4 has joined the chatroom><chat><chat><User tenc-learner has joined the chatroom><chat><chat><User l3 has joined the chatroom><chat><chat><User l2 has joined the chatroom><chat><chat><User l2 has left the chatroom><chat><chat><User tenc-teacher has left the chatroom><chat><chat><User l1 has left the chatroom><chat>'),(10,'0-env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745-service-036dc625-3ba2-effd-4727-53df0c9fe492','isLocked','false'),(11,'0-env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745-service-2d04ed18-7d8b-368c-4904-8c33dd692604','defaultChatLog','<chat><User tenc-teacher has joined the chatroom><chat><chat>tenc-teacher: ccc<chat><chat><User tenc-learner has joined the chatroom><chat><chat>tenc-teacher: xxx<chat><chat><User tenc-learner has LOCKED the chatroom><chat><chat><User tenc-learner has joined the chatroom><chat><chat><User tenc-teacher has joined the chatroom><chat><chat><User tenc-learner has LOCKED the chatroom><chat><chat><User tenc-learner has joined the chatroom><chat><chat><User tenc-learner has joined the chatroom><chat><chat>tenc-learner: fdg df gdf gd fg dfg df g df gffgfgfgfgfgf g fg f gfg f g fg f gfgggggggggggggg<chat><chat><User tenc-learner has left the chatroom><chat><chat><User tenc-learner has joined the chatroom><chat><chat><User l1 has joined the chatroom><chat><chat><User tenc-teacher has joined the chatroom><chat><chat><User l3 has joined the chatroom><chat><chat><User l2 has joined the chatroom><chat><chat><User l3 has left the chatroom><chat><chat><User l2 has left the chatroom><chat><chat><User tenc-teacher has left the chatroom><chat>'),(12,'0-env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745-service-2d04ed18-7d8b-368c-4904-8c33dd692604','defaultChatPresence','<member>l1<member><member>tenc-teacher<member><member>l3<member><member>l2<member>'),(13,'0-env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745-service-2d04ed18-7d8b-368c-4904-8c33dd692604','isLocked','false');
/*!40000 ALTER TABLE `shareddata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whitelist`
--

DROP TABLE IF EXISTS `whitelist`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `whitelist` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `fUrl` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `whitelist`
--

LOCK TABLES `whitelist` WRITE;
/*!40000 ALTER TABLE `whitelist` DISABLE KEYS */;
INSERT INTO `whitelist` VALUES (1,'http://127.0.0.1'),(2,'http://localhost');
/*!40000 ALTER TABLE `whitelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widget`
--

DROP TABLE IF EXISTS `widget`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `widget`
--

LOCK TABLES `widget` WRITE;
/*!40000 ALTER TABLE `widget` DISABLE KEYS */;
INSERT INTO `widget` VALUES (1,'Unsupported widget widget','/wookie/wservices/www.tencompetence.org/widgets/default/notsupported/index.htm',350,500,'f','http://www.tencompetence.org/widgets/default/notsupported','This widget is a placeholder for when no corresponding widget is found for a given type','Paul Sharples','/wookie/shared/images/defaultwidget.png'),(2,'Default chat widget','/wookie/wservices/www.tencompetence.org/widgets/default/chat/chat.htm',358,500,'f','http://www.tencompetence.org/widgets/default/chat','This widget provides a simple chat or Instant messaging facility','Paul Sharples','/wookie/shared/images/chat.png'),(3,'Default discussion/forum widget','/wookie/wservices/www.tencompetence.org/widgets/default/forum/forum.htm',350,520,'t','http://www.tencompetence.org/widgets/default/forum','This widget provides a threaded discussion forum faciltity','Paul Sharples','/wookie/shared/images/forum.png');
/*!40000 ALTER TABLE `widget` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgetdefault`
--

DROP TABLE IF EXISTS `widgetdefault`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `widgetdefault` (
  `widgetContext` varchar(255) NOT NULL,
  `widgetId` int(11) default NULL,
  PRIMARY KEY  (`widgetContext`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `widgetdefault`
--

LOCK TABLES `widgetdefault` WRITE;
/*!40000 ALTER TABLE `widgetdefault` DISABLE KEYS */;
INSERT INTO `widgetdefault` VALUES ('chat',2),('discussion',3),('forum',3),('unsupported',1);
/*!40000 ALTER TABLE `widgetdefault` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgetinstance`
--

DROP TABLE IF EXISTS `widgetinstance`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `widgetinstance`
--

LOCK TABLES `widgetinstance` WRITE;
/*!40000 ALTER TABLE `widgetinstance` DISABLE KEYS */;
INSERT INTO `widgetinstance` VALUES (39,'tenc-teacher','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-A4457FB2-EBCB-4BDE-FE04-702B71368B03','lnknS.pl.En8jgmQNGD3dqgd/bakHU.eq.',2,'F','T','F','F'),(40,'tenc-teacher','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-A7D23584-3450-06EC-15A5-CE58C44F8AE4','zAT/Zmv05pa9Mrshh/uq8k42y7E.eq.',2,'F','T','F','F'),(41,'tenc-teacher','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-AC0B8544-4DEB-A66A-1125-7093C467CCD9','/dCVFjbKppU9U1AlvDP5bdC1dfI.eq.',3,'F','T','F','F'),(42,'tenc-teacher','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-406DF471-5C53-C680-DFD5-EA99C1A5A996','odTqgtEGHe3HziVHhF4J30dBRz4.eq.',3,'F','T','F','F'),(43,'tenc-learner','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-06D168A1-EFB3-1057-E131-0DBB6B1E3DDC','YEPij0sjggV0a42Xir2JvVaZPvA.eq.',2,'F','T','F','F'),(44,'tenc-learner','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-08DA8A09-7D4C-BC2A-16A7-3A27435C145C','MAoSmZreNs8d.pl.St/zVneINhttTs.eq.',2,'F','T','F','F'),(45,'tenc-learner','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-A2419F96-4E43-B960-DE1A-E1D949A08914','VNqqB7EIhShuB1ys4DMmbSDiWzU.eq.',3,'F','T','F','F'),(46,'tenc-learner','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-142254D5-DE72-802A-16D7-D720559C0C71','ONh8JirAVNVh73YneNI7SaCLsOA.eq.',3,'F','T','F','F'),(47,'t1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-502C5ADC-5D56-8F63-87AC-3A0F1291CFFA','dc6ZomWP8HxOF00hCBa6XCEnHAc.eq.',2,'F','T','F','F'),(48,'t2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-2197CC7F-13AC-E25A-9B7E-7A676E9F2FCD','AneoDS4oK1R5v/ci2YnkMD3TWbQ.eq.',2,'F','T','F','F'),(49,'t2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-84ECC99A-9ADB-A315-0C64-A56D53E2C053','sMsyeUdRHrRsr6rbfbaS3pRGgRk.eq.',2,'F','T','F','F'),(50,'t1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-5B68D291-AB92-53F5-7DB2-90FB396B8023','i68rw9u/ikNE.pl.2l0OvQbqkjAfAI.eq.',2,'F','T','F','F'),(51,'t2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-346F6D9A-38E3-A818-36FB-224EBE20E1CE','GTwCflu4C4KLr/58hWrqXadT/c8.eq.',3,'F','T','F','F'),(52,'t1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-741B81E2-4D5E-DB9E-EEB0-B58F621D4EC2','V1Xcrl2orTlZ99EMJDaNw97ec7Y.eq.',3,'F','T','F','F'),(53,'t2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-B7461CA5-FDDF-39F4-E7DE-42FDE4F80386','q1/Zi4qoYuLGoe71trYHecexEDA.eq.',3,'F','T','F','F'),(54,'t1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-F68DD8F4-C178-D292-7961-0D130E56E789','DNqlhEXASf5Do8dI5i0LfUwNGF8.eq.',3,'F','T','F','F'),(55,'l4','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-60EDD4EA-81CB-F0D2-7695-AD204B0924B8','u6l/s2yPoEE6lYdp1gH4ZwbTRIc.eq.',2,'F','T','F','F'),(56,'l4','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-743B89F6-0E7B-984C-1E67-0E63BA2C72D1','m6Nvw9GDjnAMAIjnV3W5X5.pl.m7ak.eq.',2,'F','T','F','F'),(57,'l4','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-3BCD8D6A-9424-7B66-D569-3CB505CE689C','K9wGqA4di/JOvGLZDCwg5IUwU9Q.eq.',3,'F','T','F','F'),(58,'l4','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-CDC0E73C-9719-89C1-EC5C-87E2494803CA','TbbNuxGT4RtBAe7w46yBEeY0KDs.eq.',3,'F','T','F','F'),(59,'l3','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-C0485AD4-0B96-0540-AFF6-8196A9397307','xHtBtdfCDSjpFqwRmT4dG9mw7ZM.eq.',2,'F','T','F','F'),(60,'l3','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-2220A58A-5688-CD1A-7EF9-619775286F49','kMtSObSaE5Q4z4.pl.7dKyYrFCQVB8.eq.',2,'F','T','F','F'),(61,'l3','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-A9101DB8-98EF-A360-EB78-8FFC48AFCE83','i5THZgqzzG07RzxmcXhUTSiMmT4.eq.',3,'F','T','F','F'),(62,'l3','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-FA33469D-03B1-EED3-6956-C75E735B600A','YMyy6Z0uuWIPB4FnpIQ3.pl.Rhzrms.eq.',3,'F','T','F','F'),(63,'l1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-17AC845F-83FB-2C67-4920-134D1860BCF5','HWhVIvDi70qmTxgCGc9DkELGjjE.eq.',2,'F','T','F','F'),(64,'l1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-E622DFDF-594C-0339-0A43-A90FB5DEBD74','I7zfou2SL8r6tFMtz1M/x.pl.NTF6M.eq.',2,'F','T','F','F'),(65,'l1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-08841F46-CCF3-1D5E-FCC0-3F07D39A08F0','98Mwqp6yxhIp.pl.rBXGilqTu59je8.eq.',3,'F','T','F','F'),(66,'l1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-ED75DE5F-0ABD-1A87-366C-698D965A6819','vbVYY2GEWQaA5AA3YA0BbDiABjs.eq.',3,'F','T','F','F'),(67,'paul','0','env001','ser001','nonce-92A60547-C900-4039-AB76-8E67E4310A98','qR3Y1bPpBK.pl.YIA0QULw.pl.XquScr0.eq.',2,'F','T','F','F'),(68,'l2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-A80083DF-49F5-CEB7-A92D-20BAB5BB1EBD','3JSeIOVhVk4HP75JZcXeJ12wxXg.eq.',2,'F','T','F','F'),(69,'l2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-019AEC95-561D-DB75-052B-57D4A4020D46','a24XEWJh7JcZXinj598mGCqDI8w.eq.',2,'F','T','F','F'),(70,'l2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-4029F036-20A6-0B1F-1E8F-7E25A79FD851','GwaovAA2f3OK91xj6QOwSBnEimg.eq.',3,'F','T','F','F'),(71,'l2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-A9807484-5216-70F7-E760-5DE3E11CB540','EybSYwRgk7fJNrY3IxbuKrbz99c.eq.',3,'F','T','F','F');
/*!40000 ALTER TABLE `widgetinstance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgetservice`
--

DROP TABLE IF EXISTS `widgetservice`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `widgetservice` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `service_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `widgetservice`
--

LOCK TABLES `widgetservice` WRITE;
/*!40000 ALTER TABLE `widgetservice` DISABLE KEYS */;
INSERT INTO `widgetservice` VALUES (1,'unsupported'),(2,'chat'),(3,'discussion'),(4,'forum');
/*!40000 ALTER TABLE `widgetservice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgettype`
--

DROP TABLE IF EXISTS `widgettype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `widgettype` (
  `id` int(11) NOT NULL auto_increment,
  `widget_id` int(11) NOT NULL,
  `widget_context` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKAA9137EE2C2E1E5` (`widget_id`),
  CONSTRAINT `FKAA9137EE2C2E1E5` FOREIGN KEY (`widget_id`) REFERENCES `widget` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `widgettype`
--

LOCK TABLES `widgettype` WRITE;
/*!40000 ALTER TABLE `widgettype` DISABLE KEYS */;
INSERT INTO `widgettype` VALUES (1,1,'unsupported'),(2,2,'chat'),(3,3,'forum'),(6,3,'discussion');
/*!40000 ALTER TABLE `widgettype` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2008-03-11 12:49:53
