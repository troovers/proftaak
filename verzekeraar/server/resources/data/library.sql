-- ----------------------------------------------------- 
-- IVP4 Bibliotheek Informatie Systeem
-- Worked example
--
-- Robin Schellius, juli 2014

-- Database setup:
--
-- de datase connectie gebruikt de volgende login:
-- username: biblio1
-- password: boekje

-- -----------------------------------------------------

DROP SCHEMA IF EXISTS `library` ;
CREATE SCHEMA IF NOT EXISTS `library` DEFAULT CHARACTER SET latin1 ;
USE `library`;

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE IF NOT EXISTS `book` (
  `ISBN` int(11) NOT NULL,
  `Title` varchar(64) NOT NULL,
  `Author` varchar(32) NOT NULL,
  `Edition` int(11) NOT NULL,
  PRIMARY KEY (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `copy`
--

DROP TABLE IF EXISTS `copy`;
CREATE TABLE IF NOT EXISTS `copy` (
  `CopyID` int(11) NOT NULL,
  `LendingPeriod` int(11) NOT NULL,
  `BookISBN` int(11) NOT NULL,
  PRIMARY KEY (`CopyID`),
  KEY `BookISBN` (`BookISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
CREATE TABLE IF NOT EXISTS `loan` (
  `ReturnDate` date NOT NULL,
  `MembershipNr` int(11) NOT NULL,
  `CopyID` int(11) NOT NULL,
  PRIMARY KEY (`ReturnDate`,`MembershipNr`,`CopyID`),
  KEY `MembershipNr` (`MembershipNr`),
  KEY `CopyID` (`CopyID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
CREATE TABLE IF NOT EXISTS `member` (
  `MembershipNumber` int(11) NOT NULL,
  `FirstName` varchar(32) NOT NULL,
  `LastName` varchar(32) NOT NULL,
  `Street` varchar(32) NOT NULL,
  `HouseNumber` varchar(4) NOT NULL,
  `City` varchar(32) NOT NULL,
  `PhoneNumber` varchar(16) NOT NULL,
  `EmailAddress` varchar(32) NOT NULL,
  `Fine` double NOT NULL,
  PRIMARY KEY (`MembershipNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `ReservationDate` date NOT NULL,
  `MembershipNumber` int(11) NOT NULL,
  `BookISBN` int(11) NOT NULL,
  PRIMARY KEY (`ReservationDate`,`MembershipNumber`,`BookISBN`),
  KEY `MembershipNumber` (`MembershipNumber`),
  KEY `BookISBN` (`BookISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `copy`
--
ALTER TABLE `copy`
  ADD CONSTRAINT `copy_ibfk_1` 
  FOREIGN KEY (`BookISBN`) REFERENCES `book` (`ISBN`) 
  ON UPDATE CASCADE;

--
-- Constraints for table `loan`
--
ALTER TABLE `loan`
  ADD CONSTRAINT `loan_ibfk_1` 
  FOREIGN KEY (`MembershipNr`) REFERENCES `member` (`MembershipNumber`) 
  ON UPDATE CASCADE,
  ADD CONSTRAINT `loan_ibfk_2` 
  FOREIGN KEY (`CopyID`) REFERENCES `copy` (`CopyID`) 
  ON UPDATE CASCADE;

--
-- Constraints for table `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `reservation_ibfk_1` 
  FOREIGN KEY (`MembershipNumber`) REFERENCES `member` (`MembershipNumber`) 
  ON DELETE CASCADE 
  ON UPDATE CASCADE,
  
  ADD CONSTRAINT `reservation_ibfk_2` 
  FOREIGN KEY (`BookISBN`) REFERENCES `book` (`ISBN`) 
  ON UPDATE CASCADE;

  
-- Gebruiker aanmaken
CREATE USER 'biblio1'@'localhost' IDENTIFIED BY 'boekje';

-- Rechten toekennen aan deze gebruiker
GRANT ALL ON `library`.* TO 'biblio1'@'localhost';
-- GRANT SELECT ON `testview`.`dames` TO 'biblio1'@'localhost';

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`ISBN`, `Title`, `Author`, `Edition`) VALUES
(1111, 'Het leven is vurrukkulluk', 'Remco Campert', 1),
(2222, 'De Ontdekking van de Hemel', 'Harry Mulisch', 5);

--
-- Dumping data for table `member`
--

INSERT INTO `member` (`MembershipNumber`, `FirstName`, `LastName`, `Street`, `HouseNumber`, `City`, `PhoneNumber`, `EmailAddress`, `Fine`) VALUES
(2000, 'Erco', 'Argante', 'Hogeschoollaan', '1', 'Breda', '0765231234', 'e.argante@avans.nl', 0),
(2001, 'Karel', 'Van Dam', 'Buitendijk', '63', 'Breda', '0765223489', 'info@server.com', 0),
(2002, 'Marieke', 'Van Dam', 'Binnenlaan', '4', 'Breda', '0765223489', 'info@server.com', 0),
(2003, 'Eveline', 'Van Dam', 'Markt', '65', 'Breda', '0765223489', 'info@server.com', 0),
(2004, 'Sjoerd', 'Van Dam', 'Dijklaan', '112', 'Breda', '0765223489', 'info@server.com', 0),
(2005, 'Jan', 'Montizaan', 'Straatnaam', '1', 'Breda', '0765223489', 'info@server.com', 0);

--
-- Dumping data for table `copy`
--

INSERT INTO `copy` (`CopyID`, `LendingPeriod`, `BookISBN`) VALUES
(10001, 5, 2222),
(10002, 21, 1111);

--
-- Dumping data for table `loan`
--

INSERT INTO `loan` (`ReturnDate`, `MembershipNr`, `CopyID`) VALUES
('2013-10-16', 2000, 10001);


--
-- Dumping data for table `reservation`
--

INSERT INTO `reservation` (`ReservationDate`, `MembershipNumber`, `BookISBN`) VALUES
('2013-09-29', 2001, 1111);
