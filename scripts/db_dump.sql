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
  `sharedDataKey` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
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
INSERT INTO `whitelist` VALUES (1,'http://127.0.0.1'),(2,'http://localhost');
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
  `guid` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `widget`
--

LOCK TABLES `widget` WRITE;
/*!40000 ALTER TABLE `widget` DISABLE KEYS */;
INSERT INTO `widget` VALUES (1,'Unsupported widget widget','/wookie/wservices/www.tencompetence.org/widgets/default/notsupported/index.htm',350,500,'f','http://www.tencompetence.org/widgets/default/notsupported'),(2,'The default chat widget','/wookie/wservices/www.tencompetence.org/widgets/default/chat/chat.htm',368,512,'f','http://www.tencompetence.org/widgets/default/chat'),(3,'The default discussion/forum widget','/wookie/wservices/www.tencompetence.org/widgets/default/forum/forum.htm',350,520,'t','http://www.tencompetence.org/widgets/default/forum');
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
INSERT INTO `widgetdefault` VALUES ('chat',2),('discussion',3),('forum',3),('unsupported',1);
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
/*!40000 ALTER TABLE `widgetinstance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgetservice`
--

DROP TABLE IF EXISTS `widgetservice`;
CREATE TABLE `widgetservice` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `service_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
INSERT INTO `widgettype` VALUES (1,1,'unsupported'),(2,2,'chat'),(3,3,'forum'),(4,3,'discussion');
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

-- Dump completed on 2007-12-14 10:48:19
