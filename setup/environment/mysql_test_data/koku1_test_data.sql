-- MySQL dump 10.13  Distrib 5.5.8, for Win32 (x86)
--
-- Host: localhost    Database: koku1
-- ------------------------------------------------------
-- Server version	5.5.8

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (2,2,'virallinen','Testitie 1','TAMPERE','1000','PL','FI','2011-01-01','2011-01-01'),(3,3,'virallinen','Testitie 1','TAMPERE','1000','PL','FI','2011-01-01','2011-01-01'),(4,4,'virallinen','Testitie 1','TAMPERE','1000','PL','FI','2011-01-01','2011-01-01'),(5,6,'virallinen','Testitie 1','TAMPERE','1000','PL','FI','2011-01-01','2011-01-01'),(6,7,'virallinen','Testitie 1','TAMPERE','1000','PL','FI','2011-01-01','2011-01-01');
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community`
--

LOCK TABLES `community` WRITE;
/*!40000 ALTER TABLE `community` DISABLE KEYS */;
INSERT INTO `community` VALUES (14,'guardian_community','guardian_community_1',0),(15,'family_community','family_community_1',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_member`
--

LOCK TABLES `community_member` WRITE;
/*!40000 ALTER TABLE `community_member` DISABLE KEYS */;
INSERT INTO `community_member` VALUES (5,14,1,'010101-1010','guardian'),(6,14,2,'111111-1111','dependant'),(7,14,3,'222222-2222','dependant'),(8,14,4,'333333-3333','dependant'),(9,15,1,'010101-1010','father'),(10,15,2,'111111-1111','dependant'),(11,15,3,'222222-2222','dependant'),(12,15,4,'333333-3333','dependant');
/*!40000 ALTER TABLE `community_member` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (2,'elossa','1981-01-01','020202-2020','1981-01-01','Peltola','Piritta','Piritta Pia','FI','TRE','FI',0,0),(3,'elossa','1981-01-01','010101-1010','1981-01-01','Peltola','Pekka','Pekka Pertti','FI','TRE','FI',0,0),(4,'elossa','2000-01-01','111111-1111','2000-01-01','Peltola','Tero','Tero Tapani','FI','TRE','FI',0,0),(6,'elossa','2000-01-01','222222-2222','2000-01-01','Peltola','Tiina','Tiina Terhi','FI','TRE','FI',0,0),(7,'elossa','2000-01-01','333333-3333','2000-01-01','Peltola','Pauliina','Pauliina','FI','TRE','FI',0,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `electronic_contact_info`
--

LOCK TABLES `electronic_contact_info` WRITE;
/*!40000 ALTER TABLE `electronic_contact_info` DISABLE KEYS */;
INSERT INTO `electronic_contact_info` VALUES (2,2,'sahkoposti','piritta.peltola@testi.fi'),(3,3,'sahkoposti','pekka.peltola@testi.net'),(4,4,'sahkoposti','tero.peltola@testi.fi'),(5,6,'sahkoposti','tiina.peltola@testi.fi'),(6,7,'sahkoposti','pauliina.peltola@testi.fi');
/*!40000 ALTER TABLE `electronic_contact_info` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_number`
--

LOCK TABLES `phone_number` WRITE;
/*!40000 ALTER TABLE `phone_number` DISABLE KEYS */;
INSERT INTO `phone_number` VALUES (2,2,'kotipuhelin','gsm','0401234568'),(3,3,'kotipuhelin','gsm','0401234567'),(4,4,'kotipuhelin','gsm','0401234567'),(5,6,'kotipuhelin','gsm','0401234567'),(6,7,'kotipuhelin','gsm','0401234567');
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

-- Dump completed on 2011-09-21 10:11:07
