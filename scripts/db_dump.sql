-- MySQL dump 10.11
--
-- Host: localhost    Database: widgetDB
-- ------------------------------------------------------
-- Server version	5.0.37-community-nt

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
CREATE TABLE `post` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `user_id` varchar(80) NOT NULL,
  `parent_id` int(10) unsigned default '0',
  `title` varchar(1024) NOT NULL,
  `content` mediumtext,
  `publish_date` datetime NOT NULL,
  `update_date` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'fred',0,'one','this is top level','2007-10-08 13:49:01',NULL),(2,'fred',1,'one.one','this is 1.1 level','2007-10-08 13:49:01',NULL),(3,'Paul',1,'one.two','this is 1.2 level','2007-10-08 13:49:01',NULL),(4,'Paul',3,'one.two.one','this is 1.2.1 level','2007-10-08 13:49:01',NULL),(5,'Paul',3,'one.two.two','this is 1.2.2 level','2007-10-08 13:49:01',NULL),(6,'Paul',5,'one.two.two.one','this is 1.2.2.1 level','2007-10-08 13:49:01',NULL),(7,'Paul',6,'one.two.two.one.one','this is 1.2.2.1.1 level','2007-10-08 13:49:01',NULL),(8,'Paul',7,'one.two.two.one.one.one','this is 1.2.2.1.1.1 level','2007-10-09 13:49:01',NULL),(9,'Paul',6,'one.two.two.one.two','this is 1.2.2.1.2 level','2007-10-08 13:49:01',NULL),(10,'Paul',3,'one.two.three','this is 1.2.3 level','2007-10-08 13:49:01',NULL),(11,'Paul',1,'one.three','this is 1.3 level','2007-10-08 13:49:01',NULL),(12,'Paul',0,'two','this is top level','2007-10-08 13:49:01','2007-10-08 18:29:23'),(13,'Paul',12,'two.one','this 2.1 level','2007-10-08 13:49:01',NULL),(14,'Paul',12,'two.two','this 2.2 level','2007-10-08 13:49:01',NULL),(15,'Paul',14,'two.two.one','this 2.2.1 level','2007-10-08 13:49:01',NULL),(16,'Paul',12,'two.three','this 2.3 level','2007-10-09 13:50:01',NULL);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preference`
--

DROP TABLE IF EXISTS `preference`;
CREATE TABLE `preference` (
  `id` int(11) NOT NULL auto_increment,
  `widget_instance_id` int(11) NOT NULL,
  `dkey` varchar(255) default NULL,
  `dvalue` text,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_composite` (`widget_instance_id`,`dkey`),
  KEY `FKC5E6A8DB4215462E` (`widget_instance_id`),
  CONSTRAINT `FKC5E6A8DB4215462E` FOREIGN KEY (`widget_instance_id`) REFERENCES `widgetinstance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `preference`
--

LOCK TABLES `preference` WRITE;
/*!40000 ALTER TABLE `preference` DISABLE KEYS */;
INSERT INTO `preference` VALUES (94,32,'LDUsername','tenc-learner'),(95,33,'LDUsername','tenc-learner'),(96,34,'LDUsername','tenc-learner'),(97,35,'LDUsername','tenc-learner'),(98,32,'YSSJ5Oa6jJwNzuZw7bG3qBrAS.pl.M.eq.-defaultMaxY','220'),(99,32,'YSSJ5Oa6jJwNzuZw7bG3qBrAS.pl.M.eq.-defaultMaxX','367'),(100,32,'CacheAge','5'),(101,32,'YSSJ5Oa6jJwNzuZw7bG3qBrAS.pl.M.eq.-langcode','en'),(102,32,'YSSJ5Oa6jJwNzuZw7bG3qBrAS.pl.M.eq.-Interface','blue'),(103,32,'checkForUpdatesSetting','true'),(104,32,'FontSize','12'),(105,36,'LDUsername','tenc-teacher'),(106,37,'LDUsername','tenc-teacher'),(107,38,'LDUsername','tenc-teacher'),(108,39,'LDUsername','tenc-teacher'),(109,36,'gKF4mRLAd5huDlXNDkBED3Sv0tM.eq.-defaultMaxX','367'),(110,36,'gKF4mRLAd5huDlXNDkBED3Sv0tM.eq.-defaultMaxY','220'),(111,36,'CacheAge','5'),(112,36,'gKF4mRLAd5huDlXNDkBED3Sv0tM.eq.-langcode','en'),(113,36,'gKF4mRLAd5huDlXNDkBED3Sv0tM.eq.-Interface','blue'),(114,36,'checkForUpdatesSetting','true'),(115,36,'FontSize','12'),(116,40,'LDUsername','tenc-learner'),(117,41,'LDUsername','tenc-learner');
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shareddata`
--

DROP TABLE IF EXISTS `shareddata`;
CREATE TABLE `shareddata` (
  `id` int(11) NOT NULL auto_increment,
  `sharedDataKey` varchar(255) default NULL,
  `dkey` varchar(255) default NULL,
  `dvalue` text,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_composite` (`sharedDataKey`,`dkey`),
  KEY `sharedDataIndex` (`sharedDataKey`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `shareddata`
--

LOCK TABLES `shareddata` WRITE;
/*!40000 ALTER TABLE `shareddata` DISABLE KEYS */;
INSERT INTO `shareddata` VALUES (13,'0-env001-ser001','surname','Sharpe'),(14,'0-env001-ser001','surname2','Jones'),(15,'0-env001-ser001','newfield1','value1'),(16,'1-env-6976ba77-0775-a205-f612-79bc689970cd-service-a9aaf4e8-d536-cc04-b49a-350f2b99f752','defaultChatPresence',''),(17,'1-env-6976ba77-0775-a205-f612-79bc689970cd-service-a9aaf4e8-d536-cc04-b49a-350f2b99f752','defaultChatLog',''),(18,'2-ENV-Team-A-S-Chat-A','defaultChatPresence',''),(19,'2-ENV-Team-A-S-Chat-A','defaultChatLog','');
/*!40000 ALTER TABLE `shareddata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whitelist`
--

DROP TABLE IF EXISTS `whitelist`;
CREATE TABLE `whitelist` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `fUrl` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `whitelist`
--

LOCK TABLES `whitelist` WRITE;
/*!40000 ALTER TABLE `whitelist` DISABLE KEYS */;
INSERT INTO `whitelist` VALUES (1,'http://127.0.0.1'),(2,'http://localhost'),(3,'http://www.reload.ac.uk'),(4,'http://conexn.com'),(5,'http://en.wikipedia.org/wiki'),(6,'http://www.whatsinthehouse.com/widgets/wikiVersion.php'),(7,'http://img.youtube.com');
/*!40000 ALTER TABLE `whitelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widget`
--

DROP TABLE IF EXISTS `widget`;
CREATE TABLE `widget` (
  `id` int(11) NOT NULL auto_increment,
  `widget_name` varchar(255) default NULL,
  `url` varchar(255) default NULL,
  `height` int(11) default NULL,
  `width` int(11) default NULL,
  `maximize` varchar(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `widget`
--

LOCK TABLES `widget` WRITE;
/*!40000 ALTER TABLE `widget` DISABLE KEYS */;
INSERT INTO `widget` VALUES (1,'The default chat widget','/wookie/wservices/default/chat/chat.htm',368,512,'f'),(2,'A discussion tool - better for inexperienced users','http://www.something.com/discuss1',350,440,'f'),(3,'A discussion tool - better for experienced users','/wookie/wservices/default/forum/forum.htm',350,520,'t'),(4,'acmeForum forum software for all','http://www.acme-forum.com/discuss',350,440,'f'),(5,'wikiplus+','http://www.something.com/wiki',350,440,'f'),(6,'YouTube mac widget','/wookie/wservices/mac-widgets/YouTube/YouTube.wdgt/YouTube.html',332,384,'f'),(7,'WikiPedia search mac widget','/wookie/wservices/mac-widgets/wikipedia/Wikipedia.wdgt/wiki.html',400,512,'t');
/*!40000 ALTER TABLE `widget` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgetdefault`
--

DROP TABLE IF EXISTS `widgetdefault`;
CREATE TABLE `widgetdefault` (
  `widgetContext` varchar(255) NOT NULL,
  `widgetId` int(11) default NULL,
  PRIMARY KEY  (`widgetContext`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `widgetdefault`
--

LOCK TABLES `widgetdefault` WRITE;
/*!40000 ALTER TABLE `widgetdefault` DISABLE KEYS */;
INSERT INTO `widgetdefault` VALUES ('chat',1),('discussion',3),('wikipedia',7),('youtube',6);
/*!40000 ALTER TABLE `widgetdefault` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgetinstance`
--

DROP TABLE IF EXISTS `widgetinstance`;
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
  PRIMARY KEY  (`id`),
  KEY `FKEA564ED9E2C2E1E5` (`widget_id`),
  CONSTRAINT `FKEA564ED9E2C2E1E5` FOREIGN KEY (`widget_id`) REFERENCES `widget` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `widgetinstance`
--

LOCK TABLES `widgetinstance` WRITE;
/*!40000 ALTER TABLE `widgetinstance` DISABLE KEYS */;
INSERT INTO `widgetinstance` VALUES (1,'paul','0','env001','ser001','nonce','idkeythingy',1,'F','T','F'),(2,'fred','0','env001','ser001','nonce2','idkeythingy2',1,'F','T','F'),(3,'paul2','0','env001','ser001','nonce-FCA0DF17-4726-F378-C865-CA694E09F62C','BYrhh1q/QQZfCz+2NPSQg1VRou8',1,'F','T','F'),(7,'paul3','0','env001','ser001','nonce-F06D932A-347C-FE9E-8498-B38842E7A785','qnZdkMSTqeQxKdnL6SO1vQbiixY',1,'F','T','F'),(8,'paul4','0','env001','ser001','nonce-489DC24C-608A-87AC-FDA5-979B566909DA','AEO7y3xrVeS3MoqTMQ3yC1QuI/0',1,'F','T','F'),(9,'paul4','0','env0012','ser001','nonce-933D6B01-B0E7-0BE6-31C3-8C99D1D23DF9','/WzSJ6NogQjC5V4mg+19TevEW3I',1,'F','T','F'),(10,'paul4','0','env0013','ser001','nonce-75526880-565A-018A-AD6A-88872856495A','LDmCdc7XhKIebYsoen/x5o/qUQ4',1,'F','T','F'),(11,'paul4','0','env0013','ser0013','nonce-017358AD-7C23-61A6-79D1-D916920451B0','/bzvh3sIh56fWGNAypls/o9jrreIeq',1,'F','T','F'),(12,'paul5','0','env09','ser0013','nonce-A28E9787-6B6D-1C11-43F5-1BFF529B4A8E','9qmUuA15UxbMNqHLmA5N/CaZ8JMeq',1,'F','T','F'),(13,'paul5','0','env091','ser0013','nonce-BB0BB5E2-C5DC-CC7D-239B-1E7889E32347','1k8IplwImHaplCQMZ82ljBBMkzT9weq',1,'F','T','F'),(32,'tenc-learner','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-1e733007-6087-6ad5-921f-4dea778fef8e','nonce-420993AB-BC8D-109D-CDB3-9C881EF491ED','YSSJ5Oa6jJwNzuZw7bG3qBrAS.pl.M.eq.',7,'F','T','F'),(33,'tenc-learner','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-1052a26e-3b59-c845-7e34-72ca400cc07c','nonce-3A98BAE2-5D92-B142-7F9E-9E980FFEB5FA','BbV9YQmrKykfOyquOYLdseGDZD4.eq.',6,'F','T','F'),(34,'tenc-learner','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-fda7bca4-837e-20a9-574f-2b9d65d1e305','nonce-6784EB64-04DD-FDBA-B2F6-F3615C859373','tEQEcmrgnVkW7VVecEgTi7xOD2U.eq.',3,'F','T','F'),(35,'tenc-learner','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-a9aaf4e8-d536-cc04-b49a-350f2b99f752','nonce-A6640C19-13A8-F3F3-947B-0C2BC5F7F8B0','9.pl.mjsgPEj91X7.pl.PLIMXN29vdHfo.eq.',1,'F','T','F'),(36,'tenc-teacher','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-1e733007-6087-6ad5-921f-4dea778fef8e','nonce-B95A7F41-CE55-46A5-79EA-F0E17EFBF97B','gKF4mRLAd5huDlXNDkBED3Sv0tM.eq.',7,'F','T','F'),(37,'tenc-teacher','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-1052a26e-3b59-c845-7e34-72ca400cc07c','nonce-FA7AF47B-48BB-929A-E55A-AD5E1E261241','0tOSaZ7h2Plef5jE8egGI91eePk.eq.',6,'F','T','F'),(38,'tenc-teacher','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-fda7bca4-837e-20a9-574f-2b9d65d1e305','nonce-FB4C4A35-F795-050C-057C-60328F71E7B4','v6G1fyhoHov3cmBsBJ.pl.FHPhGuXY.eq.',3,'F','T','F'),(39,'tenc-teacher','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-a9aaf4e8-d536-cc04-b49a-350f2b99f752','nonce-86800502-57F9-7058-C834-B2D3511859B6','BwtsFkCZ4yOA8yMGUJ1ZZ5l3Jss.eq.',1,'F','T','F'),(40,'tenc-learner','2','ENV-Shared','SERV_act1_forum','nonce-572CA97D-1415-F21E-42EB-854D6DC2147D','lZFUe2x4R1qhiBzQ.pl.eo3wysXOFg.eq.',3,'F','T','F'),(41,'tenc-learner','2','ENV-Team-A','S-Chat-A','nonce-9F12C6D3-0C7D-9AC5-5C9B-33036D6B2DB7','X1vUJSVMWQtCObcsD01dt6.pl.tEWM.eq.',1,'F','T','F');
/*!40000 ALTER TABLE `widgetinstance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgettype`
--

DROP TABLE IF EXISTS `widgettype`;
CREATE TABLE `widgettype` (
  `id` int(11) NOT NULL auto_increment,
  `widget_id` int(11) NOT NULL,
  `widget_context` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKAA9137EE2C2E1E5` (`widget_id`),
  CONSTRAINT `FKAA9137EE2C2E1E5` FOREIGN KEY (`widget_id`) REFERENCES `widget` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `widgettype`
--

LOCK TABLES `widgettype` WRITE;
/*!40000 ALTER TABLE `widgettype` DISABLE KEYS */;
INSERT INTO `widgettype` VALUES (1,1,'chat'),(2,2,'chat'),(3,2,'discussion'),(4,3,'discussion'),(5,3,'wiki'),(6,3,'chat'),(7,4,'forum'),(8,6,'youtube'),(9,7,'wikipedia');
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

-- Dump completed on 2007-10-14  9:49:57
