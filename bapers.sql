-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 13, 2024 at 02:29 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bapers`
--

-- --------------------------------------------------------

--
-- Table structure for table `cardpayment`
--

CREATE TABLE `cardpayment` (
  `cardType` varchar(255) DEFAULT NULL,
  `lastFourDigits` int(4) DEFAULT NULL,
  `cardExpiryDate` date DEFAULT NULL,
  `paymentID` int(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cardpayment`
--

INSERT INTO `cardpayment` (`cardType`, `lastFourDigits`, `cardExpiryDate`, `paymentID`) VALUES
('Visa', 1234, '2021-03-25', 1);

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customerID` int(10) NOT NULL,
  `customerName` varchar(255) DEFAULT NULL,
  `contactName` varchar(255) DEFAULT NULL,
  `customerAddress` varchar(255) DEFAULT NULL,
  `customerPhone` varchar(15) DEFAULT NULL,
  `customerEmail` varchar(255) DEFAULT NULL,
  `accountType` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customerID`, `customerName`, `contactName`, `customerAddress`, `customerPhone`, `customerEmail`, `accountType`) VALUES
(1, 'Jimi Joloaso', 'Jimbles Notronbo', 'The Streets', '00788888888', 'wdwf23134212@gmail.com', 'Valued'),
(2, 'Jeff', 'Jeff Corp.', 'Jeffington Road', '00705998987', '9uihiij@gmail.com', 'Valued'),
(3, 'Tester', 'Testing Corp.', 'Testers Lane', '200390', '', 'Non-Valued'),
(4, 'TestCustomer', 'customerName', 'place', '1020402', '', 'Non-Valued');

-- --------------------------------------------------------

--
-- Table structure for table `fixed_discounts`
--

CREATE TABLE `fixed_discounts` (
  `customerID` int(7) NOT NULL,
  `fixed_rate` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fixed_discounts`
--

INSERT INTO `fixed_discounts` (`customerID`, `fixed_rate`) VALUES
(1, 30.4);

-- --------------------------------------------------------

--
-- Table structure for table `flexible_discounts`
--

CREATE TABLE `flexible_discounts` (
  `customerID` int(7) NOT NULL,
  `lower_bound` float NOT NULL,
  `upper_bound` float DEFAULT NULL,
  `discountrate` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `flexible_discounts`
--

INSERT INTO `flexible_discounts` (`customerID`, `lower_bound`, `upper_bound`, `discountrate`) VALUES
(1, 100, 200, 30.03),
(1, 200, 300, NULL),
(2, 100, 200, 4);

-- --------------------------------------------------------

--
-- Table structure for table `job`
--

CREATE TABLE `job` (
  `jobID` int(10) NOT NULL,
  `UrgencyLvl` varchar(255) DEFAULT NULL,
  `Price` float DEFAULT NULL,
  `Status` varchar(255) DEFAULT NULL,
  `Deadline` timestamp(6) NULL DEFAULT NULL,
  `jobDesc` varchar(255) DEFAULT NULL,
  `taskamount` int(7) DEFAULT NULL,
  `amountcompleted` int(7) DEFAULT NULL,
  `completionDate` timestamp NULL DEFAULT NULL,
  `customerID` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `job`
--

INSERT INTO `job` (`jobID`, `UrgencyLvl`, `Price`, `Status`, `Deadline`, `jobDesc`, `taskamount`, `amountcompleted`, `completionDate`, `customerID`) VALUES
(1, 'High', 14, 'Unpaid', '2021-03-29 09:00:00.000000', 'Test Job Desc', 8, 8, '2024-08-13 10:26:43', 2),
(4, 'High Urgency', 111.2, 'Unpaid', '2021-03-23 01:06:42.000000', '', 2, 2, '2024-08-13 10:46:17', 4),
(5, 'Normal', 14.4, 'Paid', '2021-03-24 18:14:25.000000', '', 1, 1, '2024-08-12 18:10:42', 2),
(6, 'Normal', 40.3, 'Paid', '2021-03-24 18:15:32.000000', 'Tester Special', 1, 0, NULL, 2),
(7, 'High Urgency', 30.6, 'Unpaid', '2021-03-24 18:46:26.000000', '', 1, 1, '2021-03-24 15:53:20', 4),
(8, 'Urgent', 183.96, 'Unpaid', '2021-03-25 20:00:00.000000', '', 5, 2, NULL, 3),
(9, 'Urgent', 80.505, 'Paid', '2021-03-25 20:30:41.000000', 'Tester Special Detail', 2, 2, '2021-03-25 14:31:43', 3),
(10, 'Normal', 29.7, 'Unpaid', '2021-03-26 14:58:43.000000', 'test', 2, 0, NULL, 2),
(11, 'Urgent', 35.19, 'Unpaid', '2021-03-25 21:06:57.000000', '', 2, 2, '2021-03-25 15:08:23', 2);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `paymentID` int(7) NOT NULL,
  `amount` int(10) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `customerID` int(10) DEFAULT NULL,
  `jobID` int(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`paymentID`, `amount`, `date`, `type`, `customerID`, `jobID`) VALUES
(1, 81, '2021-03-25 14:36:26', 'Card', 3, 9);

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `StaffID` int(10) NOT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`StaffID`, `Name`, `email`, `password`, `role`) VALUES
(1, 'Marcel', 'jimi@hotmail.com', 'offman', 'Technician'),
(2, 'test', 'test', 'test', 'Office Manager'),
(3, 'test', 'test', 'test', 'Technician'),
(4, 'o', 'o', 'o', 'Office Manager'),
(5, '55', '55@gmail.com', '55', 'Receptionist');

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE `task` (
  `taskID` int(10) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `startTime` timestamp(6) NULL DEFAULT NULL,
  `completionTime` timestamp(6) NULL DEFAULT NULL,
  `jobID` int(10) DEFAULT NULL,
  `StaffID` int(10) DEFAULT NULL,
  `task_detailID` int(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`taskID`, `department`, `status`, `startTime`, `completionTime`, `jobID`, `StaffID`, `task_detailID`) VALUES
(1, 'testdep', 'Completed', '2024-08-12 18:17:33.000000', '2024-08-13 10:26:43.000000', 1, 4, 3),
(2, 'Test DEPART', 'Completed', '2021-03-25 02:19:19.000000', '2024-08-13 09:51:56.000000', 1, 4, 1),
(3, 'Based Dept.', 'Completed', '2024-08-13 10:33:39.000000', '2024-08-13 10:33:45.000000', 4, 4, 1),
(4, 'BIPL Depot.', 'Completed', '2024-08-13 10:46:13.000000', '2024-08-13 10:46:17.000000', 4, 4, 3),
(5, 'testlocale', 'In-Progress', '2024-08-12 17:12:47.000000', '2024-08-12 18:10:42.000000', 5, 4, 5),
(6, 'Based Dept.', 'Not Started', NULL, NULL, 6, NULL, 1),
(7, 'BIPL Depot.', 'Completed', '2021-03-24 15:53:16.000000', '2021-03-24 15:53:20.000000', 7, 1, 3),
(8, 'Based Dept.', 'Not Started', NULL, NULL, 8, NULL, 1),
(9, 'Based Dept.', 'Not Started', NULL, NULL, 8, NULL, 1),
(10, 'Alt Inc.', 'Not Started', NULL, NULL, 8, NULL, 2),
(11, 'BIPL Depot.', 'Completed', '2021-03-25 15:07:32.000000', '2021-03-25 15:07:35.000000', 8, 1, 3),
(12, 'Alt Inc.', 'Completed', '2021-03-25 14:00:51.000000', '2021-03-25 14:01:00.000000', 8, 1, 2),
(13, 'Based Dept.', 'Completed', '2021-03-25 14:31:25.000000', '2021-03-25 14:31:31.000000', 9, 1, 1),
(14, 'Alt Inc.', 'Not Started', NULL, '2021-03-25 14:31:43.000000', 9, 1, 2),
(15, 'BIPL Depot.', 'Not Started', NULL, NULL, 10, NULL, 3),
(16, 'testlocale', 'Not Started', NULL, NULL, 10, NULL, 5),
(17, 'Alt Inc.', 'Not Started', NULL, '2021-03-25 15:08:03.000000', 11, 1, 2),
(18, 'Tayos house', 'Not Started', NULL, '2021-03-25 15:08:23.000000', 11, 1, 7);

-- --------------------------------------------------------

--
-- Table structure for table `taskdetails`
--

CREATE TABLE `taskdetails` (
  `task_detailID` int(7) NOT NULL,
  `price` float DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `duration` int(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `taskdetails`
--

INSERT INTO `taskdetails` (`task_detailID`, `price`, `description`, `location`, `duration`) VALUES
(1, 40.3, 'This task is a default Procedure', 'Based Dept.', 2),
(2, 13.37, 'This task is an Alternate Procedure', 'Alt Inc.', 4),
(3, 15.3, 'This task creates a new Task Detail', 'BIPL Depot.', 40),
(5, 14.4, 'Testing Testing 2', 'testlocale', 130),
(6, 13.05, 'SAFEE TASK', 'Croydon', 34),
(7, 10.09, 'TayoTask', 'Tayos house', 40);

-- --------------------------------------------------------

--
-- Table structure for table `variable_discounts`
--

CREATE TABLE `variable_discounts` (
  `customerID` int(7) NOT NULL,
  `task_detailID` int(7) NOT NULL,
  `task_discount` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `variable_discounts`
--

INSERT INTO `variable_discounts` (`customerID`, `task_detailID`, `task_discount`) VALUES
(1, 1, 30.4),
(1, 2, 30.07),
(1, 6, 10);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cardpayment`
--
ALTER TABLE `cardpayment`
  ADD PRIMARY KEY (`paymentID`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customerID`);

--
-- Indexes for table `fixed_discounts`
--
ALTER TABLE `fixed_discounts`
  ADD PRIMARY KEY (`customerID`);

--
-- Indexes for table `flexible_discounts`
--
ALTER TABLE `flexible_discounts`
  ADD PRIMARY KEY (`customerID`,`lower_bound`);

--
-- Indexes for table `job`
--
ALTER TABLE `job`
  ADD PRIMARY KEY (`jobID`),
  ADD KEY `customerID` (`customerID`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`paymentID`),
  ADD KEY `customerID` (`customerID`),
  ADD KEY `jobID` (`jobID`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`StaffID`);

--
-- Indexes for table `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`taskID`),
  ADD KEY `jobID` (`jobID`),
  ADD KEY `StaffID` (`StaffID`),
  ADD KEY `task_detailID` (`task_detailID`);

--
-- Indexes for table `taskdetails`
--
ALTER TABLE `taskdetails`
  ADD PRIMARY KEY (`task_detailID`);

--
-- Indexes for table `variable_discounts`
--
ALTER TABLE `variable_discounts`
  ADD PRIMARY KEY (`customerID`,`task_detailID`),
  ADD KEY `task_detailID` (`task_detailID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cardpayment`
--
ALTER TABLE `cardpayment`
  ADD CONSTRAINT `cardpayment_ibfk_1` FOREIGN KEY (`paymentID`) REFERENCES `payment` (`paymentID`);

--
-- Constraints for table `fixed_discounts`
--
ALTER TABLE `fixed_discounts`
  ADD CONSTRAINT `fixed_discounts_ibfk_1` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`);

--
-- Constraints for table `flexible_discounts`
--
ALTER TABLE `flexible_discounts`
  ADD CONSTRAINT `flexible_discounts_ibfk_1` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`);

--
-- Constraints for table `job`
--
ALTER TABLE `job`
  ADD CONSTRAINT `job_ibfk_1` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`);

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`),
  ADD CONSTRAINT `payment_ibfk_2` FOREIGN KEY (`jobID`) REFERENCES `job` (`jobID`);

--
-- Constraints for table `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `task_ibfk_1` FOREIGN KEY (`jobID`) REFERENCES `job` (`jobID`),
  ADD CONSTRAINT `task_ibfk_2` FOREIGN KEY (`StaffID`) REFERENCES `staff` (`StaffID`),
  ADD CONSTRAINT `task_ibfk_3` FOREIGN KEY (`task_detailID`) REFERENCES `taskdetails` (`task_detailID`);

--
-- Constraints for table `variable_discounts`
--
ALTER TABLE `variable_discounts`
  ADD CONSTRAINT `variable_discounts_ibfk_1` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`),
  ADD CONSTRAINT `variable_discounts_ibfk_2` FOREIGN KEY (`task_detailID`) REFERENCES `taskdetails` (`task_detailID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
