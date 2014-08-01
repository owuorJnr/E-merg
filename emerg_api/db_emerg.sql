-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 01, 2014 at 01:17 PM
-- Server version: 5.6.16
-- PHP Version: 5.5.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `db_emerg`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_center_contacts`
--

CREATE TABLE IF NOT EXISTS `tbl_center_contacts` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CENTER_NO` varchar(50) NOT NULL,
  `CONTACT` text NOT NULL,
  `VERIFIED` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `tbl_center_contacts`
--

INSERT INTO `tbl_center_contacts` (`ID`, `CENTER_NO`, `CONTACT`, `VERIFIED`) VALUES
(1, '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_center_services`
--

CREATE TABLE IF NOT EXISTS `tbl_center_services` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CENTER_NO` varchar(50) NOT NULL,
  `SERVICE` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `tbl_center_services`
--

INSERT INTO `tbl_center_services` (`ID`, `CENTER_NO`, `SERVICE`) VALUES
(1, '', '');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_emerg_centers`
--

CREATE TABLE IF NOT EXISTS `tbl_emerg_centers` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CENTER_NO` varchar(50) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `LOC_LAT` double NOT NULL,
  `LOC_LON` double NOT NULL,
  `EMAIL` text NOT NULL,
  `CREATED_AT` datetime NOT NULL,
  `UPDATED_AT` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CENTER_NO` (`CENTER_NO`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `tbl_emerg_centers`
--

INSERT INTO `tbl_emerg_centers` (`ID`, `CENTER_NO`, `NAME`, `LOC_LAT`, `LOC_LON`, `EMAIL`, `CREATED_AT`, `UPDATED_AT`) VALUES
(1, '', '', 0, 0, '', '0000-00-00 00:00:00', '0000-00-00 00:00:00');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
