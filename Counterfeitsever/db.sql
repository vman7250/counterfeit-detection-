/*
SQLyog Community v12.4.3 (64 bit)
MySQL - 5.1.54-community : Database - counterfeit
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`counterfeit` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `counterfeit`;

/*Table structure for table `lotinfo` */

DROP TABLE IF EXISTS `lotinfo`;

CREATE TABLE `lotinfo` (
  `lotid` varchar(200) NOT NULL,
  `expirydate` varchar(200) DEFAULT NULL,
  `medicinename` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`lotid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `lotinfo` */

/*Table structure for table `meddetail` */

DROP TABLE IF EXISTS `meddetail`;

CREATE TABLE `meddetail` (
  `medicinename` varchar(50) NOT NULL,
  `manufacturer` varchar(200) DEFAULT NULL,
  `email` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`medicinename`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `meddetail` */

/*Table structure for table `statistics` */

DROP TABLE IF EXISTS `statistics`;

CREATE TABLE `statistics` (
  `medicine` varchar(200) DEFAULT NULL,
  `place` varchar(200) DEFAULT NULL,
  `date` varchar(200) DEFAULT NULL,
  `time` varchar(200) DEFAULT NULL,
  `detail` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `statistics` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
