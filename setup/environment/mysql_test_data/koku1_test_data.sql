-- MySQL dump 10.13  Distrib 5.5.11, for Linux (x86_64)
--
-- Host: localhost    Database: koku1
-- ------------------------------------------------------
-- Server version	5.5.11

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
-- Current Database: `koku1`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `koku1` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `koku1`;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20) unsigned NOT NULL,
  `type` varchar(30) NOT NULL,
  `street_address` varchar(30) DEFAULT NULL,
  `postal_district` varchar(30) DEFAULT NULL,
  `postal_code` varchar(5) DEFAULT NULL,
  `po_box` varchar(30) DEFAULT NULL,
  `country_code` varchar(5) DEFAULT NULL,
  `valid_from` date DEFAULT NULL,
  `valid_to` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`,`type`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,1,'virallinen','Kuntakatu 11 B 50','Tampere','01450',NULL,'fi',NULL,NULL),(2,2,'virallinen','Kuntakatu 11 B 50','Tampere','01450',NULL,'fi',NULL,NULL),(3,3,'virallinen','Kuntakatu 11 B 50','Tampere','01450',NULL,'fi',NULL,NULL),(4,4,'virallinen','Lunnitie 15','Vantaa','00810',NULL,'fi',NULL,NULL),(5,6,'virallinen','Hämeenkatu 3 B 2','Tampere','00510',NULL,'fi',NULL,NULL),(6,7,'virallinen','Kuntakatu 11 B 50','Tampere','01450',NULL,'fi',NULL,NULL),(7,8,'virallinen','Hämeenkatu 3','Tampere','01450',NULL,'fi',NULL,NULL),(8,9,'virallinen','Hämeenkatu 4','Tampere','01450',NULL,'fi',NULL,NULL),(9,10,'virallinen','Hämeenkatu 5','Tampere','01450',NULL,'fi',NULL,NULL),(10,11,'virallinen','Hämeenkatu 6','Tampere','01450',NULL,'fi',NULL,NULL);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community`
--

DROP TABLE IF EXISTS `community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(30) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community`
--

LOCK TABLES `community` WRITE;
/*!40000 ALTER TABLE `community` DISABLE KEYS */;
INSERT INTO `community` VALUES (1,'guardian_community','kallen huoltajuusyhteisö',0),(2,'guardian_community','keijon huoltajuusyhteisö',0),(3,'guardian_community','kirsin huoltajuusyhteisö',0);
/*!40000 ALTER TABLE `community` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_member`
--

DROP TABLE IF EXISTS `community_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community_member` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `community_id` bigint(20) unsigned NOT NULL,
  `member_id` bigint(20) unsigned DEFAULT NULL,
  `member_pic` varchar(11) NOT NULL,
  `role` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `community_id` (`community_id`,`member_pic`),
  CONSTRAINT `community_member_ibfk_1` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_member`
--

LOCK TABLES `community_member` WRITE;
/*!40000 ALTER TABLE `community_member` DISABLE KEYS */;
INSERT INTO `community_member` VALUES (1,1,NULL,'111111-1111','guardian'),(2,1,NULL,'444444-4444','dependant'),(3,2,NULL,'131313-1313','guardian'),(4,2,NULL,'555555-5555','dependant'),(5,3,NULL,'222222-2222','guardian'),(6,3,NULL,'444444-4444','dependant'),(7,3,NULL,'555555-5555','dependant');
/*!40000 ALTER TABLE `community_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_membership_request`
--

DROP TABLE IF EXISTS `community_membership_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community_membership_request` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `community_id` bigint(20) unsigned NOT NULL,
  `member_role` varchar(30) NOT NULL,
  `member_pic` varchar(11) NOT NULL,
  `requester_pic` varchar(11) NOT NULL,
  `created` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `community_id` (`community_id`),
  CONSTRAINT `community_membership_request_ibfk_1` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_membership_request`
--

LOCK TABLES `community_membership_request` WRITE;
/*!40000 ALTER TABLE `community_membership_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `community_membership_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `status` varchar(30) NOT NULL,
  `status_date` date NOT NULL,
  `pic` varchar(11) NOT NULL,
  `birth_date` date NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `first_names` varchar(50) NOT NULL,
  `nationality` varchar(3) NOT NULL,
  `municipality` varchar(10) NOT NULL,
  `language` varchar(10) NOT NULL,
  `turvakielto` tinyint(1) NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pic` (`pic`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'elossa','2011-09-28','444444-4444','2006-09-30','Kuntalainen','Kaisa','Kaisa','FI','011','FI',0,0),(2,'elossa','2011-09-28','111111-1111','1976-03-04','Kuntalainen','Kalle','Kalle','FI','011','FI',0,0),(3,'elossa','2011-09-28','555555-5555','2008-06-07','Kuntalainen','Kauko','Kauko','FI','011','FI',0,0),(4,'elossa','2011-09-28','131313-1313','1970-01-22','Keinonen','Keijo','Keijo','FI','055','FI',0,0),(6,'elossa','2011-09-28','333333-3333','1951-05-06','Kuntalainen','Kerttu','Kerttu','FI','011','FI',0,0),(7,'elossa','2011-09-28','222222-2222','1980-02-03','Kuntalainen','Kirsi','Kirsi','FI','011','FI',0,0),(8,'elossa','2011-09-28','141414-1414','2008-04-02','Lahtinen','Liisa','Liisa','FI','011','FI',0,0),(9,'elossa','2011-09-28','151515-1515','2008-05-09','Salminen','Sami','Sami','FI','011','FI',0,0),(10,'elossa','2011-09-28','161616-1616','2008-05-22','Suhonen','Sanni','Sanni','FI','011','FI',0,0),(11,'elossa','2011-09-28','171717-1717','2008-06-18','Simonen','Sulo','Sulo','FI','011','FI',0,0);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `electronic_contact_info`
--

DROP TABLE IF EXISTS `electronic_contact_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `electronic_contact_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20) unsigned NOT NULL,
  `type` varchar(30) NOT NULL,
  `contact` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`,`type`),
  CONSTRAINT `electronic_contact_info_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `electronic_contact_info`
--

LOCK TABLES `electronic_contact_info` WRITE;
/*!40000 ALTER TABLE `electronic_contact_info` DISABLE KEYS */;
INSERT INTO `electronic_contact_info` VALUES (1,1,'email','kaisa.kuntalainen@kunpo.tpe.fi'),(2,2,'email','kalle.kuntalainen@kunpo.tpe.fi'),(3,4,'email','keijo.keinonen@keinonen.fi'),(4,6,'email','kerttu.kuntalainen@kunpo.tpe.fi'),(5,7,'email','kirsi.kuntalainen@kunpo.tpe.fi');
/*!40000 ALTER TABLE `electronic_contact_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membership_approval`
--

DROP TABLE IF EXISTS `membership_approval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `membership_approval` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `membership_request_id` bigint(20) unsigned NOT NULL,
  `approver_pic` varchar(11) NOT NULL,
  `status` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `membership_request_id` (`membership_request_id`),
  CONSTRAINT `membership_approval_ibfk_1` FOREIGN KEY (`membership_request_id`) REFERENCES `community_membership_request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membership_approval`
--

LOCK TABLES `membership_approval` WRITE;
/*!40000 ALTER TABLE `membership_approval` DISABLE KEYS */;
/*!40000 ALTER TABLE `membership_approval` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_number`
--

DROP TABLE IF EXISTS `phone_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_number` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20) unsigned NOT NULL,
  `type` varchar(30) NOT NULL,
  `class` varchar(30) NOT NULL,
  `number` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`,`type`),
  CONSTRAINT `phone_number_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_number`
--

LOCK TABLES `phone_number` WRITE;
/*!40000 ALTER TABLE `phone_number` DISABLE KEYS */;
INSERT INTO `phone_number` VALUES (1,1,'kotipuhelin','gsm','0403333255'),(2,2,'kotipuhelin','gsm','0403333244'),(3,4,'kotipuhelin','gsm','0503333299'),(4,6,'kotipuhelin','gsm','0403333277'),(5,7,'kotipuhelin','gsm','0403333222');
/*!40000 ALTER TABLE `phone_number` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-09-29  1:12:36
