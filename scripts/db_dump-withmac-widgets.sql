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
  `guid` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `widget`
--

LOCK TABLES `widget` WRITE;
/*!40000 ALTER TABLE `widget` DISABLE KEYS */;
INSERT INTO `widget` VALUES (1,'Unsupported widget widget','/wookie/wservices/www.tencompetence.org/widgets/default/notsupported/index.htm',350,500,'f','http://www.tencompetence.org/widgets/default/notsupported'),(2,'The default chat widget','/wookie/wservices/www.tencompetence.org/widgets/default/chat/chat.htm',368,512,'f','http://www.tencompetence.org/widgets/default/chat'),(3,'The default discussion/forum widget','/wookie/wservices/www.tencompetence.org/widgets/default/forum/forum.htm',350,520,'t','http://www.tencompetence.org/widgets/default/forum'),(6,'YouTube mac widget','/wookie/wservices/mac-widgets/com.conexn.widget.YouTube/YouTube.wdgt/YouTube.html',332,384,'f','com.conexn.widget.YouTube'),(7,'WikiPedia search mac widget','/wookie/wservices/mac-widgets/com.whatsinthehouse.widget.wikipedia/Wikipedia.wdgt/wiki.html',400,512,'t','com.whatsinthehouse.widget.wikipedia');
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
INSERT INTO `widgetdefault` VALUES ('chat',2),('forum',3),('unsupported',1),('youtube',6);
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
INSERT INTO `widgetinstance` VALUES (1,'paul','0','env001','ser001','nonce','idkeythingy',1,'F','T','F'),(2,'fred','0','env001','ser001','nonce2','idkeythingy2',1,'F','T','F'),(3,'paul2','0','env001','ser001','nonce-FCA0DF17-4726-F378-C865-CA694E09F62C','BYrhh1q/QQZfCz+2NPSQg1VRou8',1,'F','T','F'),(7,'paul3','0','env001','ser001','nonce-F06D932A-347C-FE9E-8498-B38842E7A785','qnZdkMSTqeQxKdnL6SO1vQbiixY',1,'F','T','F'),(8,'paul4','0','env001','ser001','nonce-489DC24C-608A-87AC-FDA5-979B566909DA','AEO7y3xrVeS3MoqTMQ3yC1QuI/0',1,'F','T','F'),(9,'paul4','0','env0012','ser001','nonce-933D6B01-B0E7-0BE6-31C3-8C99D1D23DF9','/WzSJ6NogQjC5V4mg+19TevEW3I',1,'F','T','F'),(10,'paul4','0','env0013','ser001','nonce-75526880-565A-018A-AD6A-88872856495A','LDmCdc7XhKIebYsoen/x5o/qUQ4',1,'F','T','F'),(11,'paul4','0','env0013','ser0013','nonce-017358AD-7C23-61A6-79D1-D916920451B0','/bzvh3sIh56fWGNAypls/o9jrreIeq',1,'F','T','F'),(12,'paul5','0','env09','ser0013','nonce-A28E9787-6B6D-1C11-43F5-1BFF529B4A8E','9qmUuA15UxbMNqHLmA5N/CaZ8JMeq',1,'F','T','F'),(13,'paul5','0','env091','ser0013','nonce-BB0BB5E2-C5DC-CC7D-239B-1E7889E32347','1k8IplwImHaplCQMZ82ljBBMkzT9weq',1,'F','T','F'),(32,'tenc-learner','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-1e733007-6087-6ad5-921f-4dea778fef8e','nonce-420993AB-BC8D-109D-CDB3-9C881EF491ED','YSSJ5Oa6jJwNzuZw7bG3qBrAS.pl.M.eq.',7,'F','T','F'),(33,'tenc-learner','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-1052a26e-3b59-c845-7e34-72ca400cc07c','nonce-3A98BAE2-5D92-B142-7F9E-9E980FFEB5FA','BbV9YQmrKykfOyquOYLdseGDZD4.eq.',6,'F','T','F'),(34,'tenc-learner','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-fda7bca4-837e-20a9-574f-2b9d65d1e305','nonce-6784EB64-04DD-FDBA-B2F6-F3615C859373','tEQEcmrgnVkW7VVecEgTi7xOD2U.eq.',3,'F','T','F'),(35,'tenc-learner','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-a9aaf4e8-d536-cc04-b49a-350f2b99f752','nonce-A6640C19-13A8-F3F3-947B-0C2BC5F7F8B0','9.pl.mjsgPEj91X7.pl.PLIMXN29vdHfo.eq.',1,'F','T','F'),(36,'tenc-teacher','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-1e733007-6087-6ad5-921f-4dea778fef8e','nonce-B95A7F41-CE55-46A5-79EA-F0E17EFBF97B','gKF4mRLAd5huDlXNDkBED3Sv0tM.eq.',7,'F','T','F'),(37,'tenc-teacher','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-1052a26e-3b59-c845-7e34-72ca400cc07c','nonce-FA7AF47B-48BB-929A-E55A-AD5E1E261241','0tOSaZ7h2Plef5jE8egGI91eePk.eq.',6,'F','T','F'),(38,'tenc-teacher','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-fda7bca4-837e-20a9-574f-2b9d65d1e305','nonce-FB4C4A35-F795-050C-057C-60328F71E7B4','v6G1fyhoHov3cmBsBJ.pl.FHPhGuXY.eq.',3,'F','T','F'),(39,'tenc-teacher','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-a9aaf4e8-d536-cc04-b49a-350f2b99f752','nonce-86800502-57F9-7058-C834-B2D3511859B6','BwtsFkCZ4yOA8yMGUJ1ZZ5l3Jss.eq.',1,'F','T','F'),(40,'tenc-learner','2','ENV-Shared','SERV_act1_forum','nonce-572CA97D-1415-F21E-42EB-854D6DC2147D','lZFUe2x4R1qhiBzQ.pl.eo3wysXOFg.eq.',3,'F','T','F'),(41,'tenc-learner','2','ENV-Team-A','S-Chat-A','nonce-9F12C6D3-0C7D-9AC5-5C9B-33036D6B2DB7','X1vUJSVMWQtCObcsD01dt6.pl.tEWM.eq.',1,'F','T','F'),(42,'paulie','0','env001','ser001','nonce-439660DA-C929-8A84-FB1B-8848E0F73860','I.pl.3MpS6aSjmRQDW8FfLJT2HsIEY.eq.',1,'F','T','F'),(43,'tenc-learner','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-a9aaf4e8-d536-cc04-b49a-350f2b99f752','nonce-D9D1E021-F5CB-C359-FCFE-F2FEFD8D6057','mu3j3oWuh3KAvjsW0CD6vGhh2ZI.eq.',2,'F','T','F'),(44,'service-user','8','env-2a728703-cbcd-a37e-f34a-1de586454fb4','service-87e413a7-1f65-00d4-faf8-8a8af38e7a9d','nonce-8B3D0A7E-D509-7D86-D8F2-CF468C480A4E','xJwbDWa9AehpgXO24awoXrVs0k4.eq.',2,'F','T','F'),(45,'service-user','8','env-9197693a-b55c-9ddd-a770-2faee47e2c0d','lo-1009f95c-f735-3b1e-b6d3-f2197ccf563f','nonce-46248A46-D745-E7D0-8272-F0FBC848B4BD','9Igu4sWVauL5wRXl32.pl.I4unV//4.eq.',7,'F','T','F'),(46,'@~^&@~^&','0','env001','ser001','nonce-216313E0-E93D-CC20-A711-200D2EF8AF6F','x6LqrHJGSX0QCSnJ3MpV0RuPczE.eq.',2,'F','T','F'),(47,'@~^&@~^&+$%£!\"\'\'','0','env001','ser001','nonce-23A59F6D-B07F-5305-08DC-EB8063335811','zyhXIobAwomYlcxDlGJR37vMK7g.eq.',2,'F','T','F'),(48,'service-user2','8','env-9197693a-b55c-9ddd-a770-2faee47e2c0d','service-5e98fffe-f57f-9b0d-5094-c86434e3d725','nonce-078155DB-51AC-04EF-2FE1-7A69D38410C1','elqFpRqQVYFhhYBJRhh1CMFio74.eq.',3,'F','T','F'),(49,'service-user2','8','env-9197693a-b55c-9ddd-a770-2faee47e2c0d','service-f253bfbc-781e-e409-8f19-b66c35061d81','nonce-0FBBF461-F834-1EBE-7EDA-67B12FC826AC','j0Kddui30KdaG17LEZbuAKu4cZc.eq.',3,'F','T','F'),(50,'service-user2','8','env-2a728703-cbcd-a37e-f34a-1de586454fb4','service-87e413a7-1f65-00d4-faf8-8a8af38e7a9d','nonce-37A1DBBE-87C4-5AB8-8052-F02769BCD159','74uMIRKYXdy7jhgOnOuw28viuMI.eq.',2,'F','T','F'),(51,'service-user','8','env-9197693a-b55c-9ddd-a770-2faee47e2c0d','service-5e98fffe-f57f-9b0d-5094-c86434e3d725','nonce-409F63FF-5699-07F7-EEE2-E784DE48B5E2','EMUkzXmRJMghRMWivkTsToLK9UU.eq.',3,'F','T','F'),(52,'service-user','8','env-9197693a-b55c-9ddd-a770-2faee47e2c0d','service-f253bfbc-781e-e409-8f19-b66c35061d81','nonce-9EF79557-9D98-F4DA-A01E-EC972C5AD7B8','883Vr0aPJjHGvxWkCJht1jfqhuk.eq.',3,'F','T','F'),(53,'service-user','9','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-DAD13CD5-86CD-E1F2-5A8C-17ADA502B5B9','j5Do3g9kim//bVZqyFplP0SbVZY.eq.',2,'F','T','F'),(54,'service-user','9','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-15136CEA-5A51-8518-83BA-4FD095A56F2F','zaidCMZzqncvrB1A/c6r7qmd4b8.eq.',2,'F','T','F'),(55,'service-user','9','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-2D402638-9945-77FF-A331-632438B691F7','5F1taTk5/lTuU1fXJq4nkUZEJnM.eq.',3,'F','T','F'),(56,'service-user','9','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-F5612380-BB5C-C6FB-0A7C-5B4CA7712BE2','eD2VsY5m8uDQKgutkQYj3kD5Upg.eq.',3,'F','T','F'),(57,'service-user2','9','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-F4BA177C-81C5-BFF2-67A1-9B93EE982237','ZXvYLqoFz9UZcHhh/tAUypNE8wU.eq.',2,'F','T','F'),(58,'service-user2','9','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-B288E6F8-F013-4BFC-7111-B1FCE529904F','H0s57ytlMnBwLSr8D/hzqYbtMB4.eq.',2,'F','T','F'),(59,'service-user2','9','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-32e594ba-3a5e-214c-60a3-9a1efc130aeb','nonce-110E93CB-CBA7-B066-FD45-D7CBB5A2F981','Ko0Bq/gGHF3pgznUqGyr0zOItTU.eq.',3,'F','T','F'),(60,'service-user2','9','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-c9474feb-6744-552a-c809-18e687c8c4c8','nonce-8531B356-4CE3-3E0B-DF50-7CFE7A73FA4A','f5awjQDyi.pl.KMQpypfTxyS03n0gs.eq.',3,'F','T','F'),(61,'paul','0','env001','ser001','nonce-7B7C07AB-760B-1059-9A94-F8BAB7AAE15A','oAOLZB4TgQgg2IyiaTsMhYecEhc.eq.',2,'F','T','F'),(62,'tenc-teacher','1','env-6976ba77-0775-a205-f612-79bc689970cd','service-a9aaf4e8-d536-cc04-b49a-350f2b99f752','nonce-667C5345-80B8-1843-7EB3-8514C9DC7F62','0X6hvHuJVA4RXdJ2uY59wI4qiKA.eq.',2,'F','T','F'),(63,'fred','1','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-D80ECD66-A573-90D0-E7C7-6FF3948B9E9F','ONxkiNvGzALOqfRsu5QagRhoNAI.eq.',2,'F','T','F'),(64,'fred','1','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-02A9C5C5-D98F-E446-FE45-B9D3F8306706','M7/p8GPZO5afSk2w1LPlyaZSvZQ.eq.',2,'F','T','F'),(65,'user1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-5E89305B-A53F-77F9-B8A8-21D6EFF20090','46AH/kPTPyID/nGnMtHSnBiG42s.eq.',2,'F','T','F'),(66,'user1','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-74EBE1AF-CB14-9F1B-A5E6-15E0C7B4A0B9','S.pl.KTsPFo0qI/fkfoN18C9N4x7NM.eq.',2,'F','T','F'),(67,'user2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-036dc625-3ba2-effd-4727-53df0c9fe492','nonce-86BA0420-C210-DE0E-D58B-5F99CB3EF44C','UtLMU2LweOx7zOQpOYx7Jg7nPkk.eq.',2,'F','T','F'),(68,'user2','0','env-1c8a4bcb-fe82-ca60-0ac3-b1be323b0745','service-2d04ed18-7d8b-368c-4904-8c33dd692604','nonce-F2ADFFA5-E4DC-D52D-EF69-56B534079EE3','RCsGT9U0dsDkdSSeAPBE/C/7/zo.eq.',2,'F','T','F');
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
INSERT INTO `widgetservice` VALUES (1,'unsupported'),(2,'chat'),(3,'discussion'),(4,'forum'),(5,'wikipedia'),(6,'youtube');
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
INSERT INTO `widgettype` VALUES (1,1,'unsupported'),(2,2,'chat'),(3,3,'forum'),(4,3,'discussion'),(8,6,'youtube'),(9,7,'wikipedia');
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

-- Dump completed on 2007-12-14 10:45:10
