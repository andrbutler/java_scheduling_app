CREATE TABLE `user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `active` tinyint(4) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;


CREATE TABLE `country` (
  `countryId` int(10) NOT NULL AUTO_INCREMENT,
  `country` varchar(50) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`countryId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

CREATE TABLE `city` (
  `cityId` int(10) NOT NULL AUTO_INCREMENT,
  `city` varchar(50) NOT NULL,
  `countryId` int(10) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`cityId`),
  KEY `countryId` (`countryId`),
  CONSTRAINT `city_ibfk_1` FOREIGN KEY (`countryId`) REFERENCES `country` (`countryId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;


CREATE TABLE `address` (
  `addressId` int(10) NOT NULL AUTO_INCREMENT,
  `address` varchar(50) NOT NULL,
  `address2` varchar(50) NOT NULL,
  `cityId` int(10) NOT NULL,
  `postalCode` varchar(10) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`addressId`),
  KEY `cityId` (`cityId`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`cityId`) REFERENCES `city` (`cityId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

CREATE TABLE `customer` (
  `customerId` int(10) NOT NULL AUTO_INCREMENT,
  `customerName` varchar(45) NOT NULL,
  `addressId` int(10) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`customerId`),
  KEY `addressId` (`addressId`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`addressId`) REFERENCES `address` (`addressId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;


CREATE TABLE `appointment` (
  `appointmentId` int(10) NOT NULL AUTO_INCREMENT,
  `customerId` int(10) NOT NULL,
  `userId` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `location` text NOT NULL,
  `contact` text NOT NULL,
  `type` text NOT NULL,
  `url` varchar(255) NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`appointmentId`),
  KEY `userId` (`userId`),
  KEY `appointment_ibfk_1` (`customerId`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`),
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

INSERT INTO `country` VALUES 
(1,'US','2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(2,'Canada','2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(3,'Norway','2019-01-01 00:00:00','test','2019-01-01 00:00:00','test');
--
-- populate table `city`
--
INSERT INTO `city` VALUES 
(1,'New York',1,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(2,'Los Angeles',1,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(3,'Toronto',2,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(4,'Vancouver',2,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(5,'Oslo',3,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test');
--
-- populate table `address`
--
INSERT INTO `address` VALUES 
(1,'123 Main','',1,'11111','555-1212','2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(2,'123 Elm','',3,'11112','555-1213','2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(3,'123 Oak','',5,'11113','555-1214','2019-01-01 00:00:00','test','2019-01-01 00:00:00','test');
--
-- populate table `customer`
--
INSERT INTO `customer` VALUES 
(1,'John Doe',1,1,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(2,'Alfred Jones',2,1,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test'),
(3,'Alice Smith',3,1,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test');
--
-- populate table `user`
--
INSERT INTO `user` VALUES 
(1,'test','test',1,'2019-01-01 00:00:00','test','2019-01-01 00:00:00','test');


