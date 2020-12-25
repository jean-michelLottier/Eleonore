-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: eleonore
-- ------------------------------------------------------
-- Server version	5.7.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `actuator`
--

DROP TABLE IF EXISTS `actuator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actuator` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `url` varchar(300) NOT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actuator`
--

LOCK TABLES `actuator` WRITE;
/*!40000 ALTER TABLE `actuator` DISABLE KEYS */;
INSERT INTO `actuator` VALUES (1,'http://localhost:8181');
/*!40000 ALTER TABLE `actuator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actuator_metric`
--

DROP TABLE IF EXISTS `actuator_metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actuator_metric` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `metric` varchar(100) NOT NULL,
                                   `actuator_id` bigint(20) NOT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `actuator_metric_FK` (`actuator_id`),
                                   CONSTRAINT `actuator_metric_FK` FOREIGN KEY (`actuator_id`) REFERENCES `actuator` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actuator_metric`
--

LOCK TABLES `actuator_metric` WRITE;
/*!40000 ALTER TABLE `actuator_metric` DISABLE KEYS */;
INSERT INTO `actuator_metric` VALUES (1,'auditevents',1),(2,'info',1),(3,'beans',1),(4,'caches',1),(5,'scheduledtasks',1);
/*!40000 ALTER TABLE `actuator_metric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authentication`
--

DROP TABLE IF EXISTS `authentication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authentication` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `profile_id` bigint(20) NOT NULL,
                                  `login` varchar(50) NOT NULL,
                                  `password` varchar(100) NOT NULL,
                                  `type` enum('USER','PROJECT','ORGANIZATION') DEFAULT 'USER',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authentication`
--

LOCK TABLES `authentication` WRITE;
/*!40000 ALTER TABLE `authentication` DISABLE KEYS */;
INSERT INTO `authentication` VALUES (1,1,'jml','password','USER');
/*!40000 ALTER TABLE `authentication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `component`
--

DROP TABLE IF EXISTS `component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `component` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `dashboard_id` bigint(20) NOT NULL,
                             `element_id` bigint(20) NOT NULL,
                             `type` enum('SONAR','ACTUATOR') NOT NULL DEFAULT 'SONAR',
                             PRIMARY KEY (`id`),
                             KEY `component_FK` (`dashboard_id`),
                             CONSTRAINT `component_FK` FOREIGN KEY (`dashboard_id`) REFERENCES `dashboard` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `component`
--

LOCK TABLES `component` WRITE;
/*!40000 ALTER TABLE `component` DISABLE KEYS */;
INSERT INTO `component` VALUES (12,1,12,'SONAR'),(13,1,1,'ACTUATOR');
/*!40000 ALTER TABLE `component` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `dashboard_id` bigint(20) NOT NULL,
                            `profile_id` bigint(20) NOT NULL,
                            `owner` tinyint(1) DEFAULT '0',
                            `editable` tinyint(1) DEFAULT '0',
                            `type` enum('USER','PROJECT','ORGANIZATION') DEFAULT 'USER',
                            PRIMARY KEY (`id`),
                            KEY `customer_FK` (`dashboard_id`),
                            KEY `customer_FK_1` (`profile_id`),
                            CONSTRAINT `customer_FK` FOREIGN KEY (`dashboard_id`) REFERENCES `dashboard` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,1,1,1,1,'USER');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dashboard`
--

DROP TABLE IF EXISTS `dashboard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dashboard` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `name` varchar(30) DEFAULT NULL,
                             `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
                             `modified_date` datetime DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dashboard`
--

LOCK TABLES `dashboard` WRITE;
/*!40000 ALTER TABLE `dashboard` DISABLE KEYS */;
INSERT INTO `dashboard` VALUES (1,'sonarboard','2020-09-28 02:51:32','2020-09-28 02:51:32');
/*!40000 ALTER TABLE `dashboard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sonar`
--

DROP TABLE IF EXISTS `sonar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sonar` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `url` varchar(300) NOT NULL,
                         `project_name` varchar(100) DEFAULT NULL,
                         `project_key` varchar(100) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sonar`
--

LOCK TABLES `sonar` WRITE;
/*!40000 ALTER TABLE `sonar` DISABLE KEYS */;
INSERT INTO `sonar` VALUES (12,'http://localhost:9000','eleonore 1','eleonore_board');
/*!40000 ALTER TABLE `sonar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sonar_metric`
--

DROP TABLE IF EXISTS `sonar_metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sonar_metric` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `metric` varchar(100) NOT NULL,
                                `sonar_id` bigint(20) NOT NULL,
                                PRIMARY KEY (`id`),
                                KEY `sonar_metric_FK` (`sonar_id`),
                                CONSTRAINT `sonar_metric_FK` FOREIGN KEY (`sonar_id`) REFERENCES `sonar` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sonar_metric`
--

LOCK TABLES `sonar_metric` WRITE;
/*!40000 ALTER TABLE `sonar_metric` DISABLE KEYS */;
INSERT INTO `sonar_metric` VALUES (79,'bugs',12);
/*!40000 ALTER TABLE `sonar_metric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `authentication_id` bigint(20) NOT NULL,
                         `auth_token` varchar(512) NOT NULL,
                         `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
                         `modified_date` datetime DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`),
                         KEY `token_FK` (`authentication_id`),
                         CONSTRAINT `token_FK` FOREIGN KEY (`authentication_id`) REFERENCES `authentication` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `firstname` varchar(100) NOT NULL,
                        `lastname` varchar(100) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Jean-Michel','Lottier');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'eleonore'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-20  6:22:34
