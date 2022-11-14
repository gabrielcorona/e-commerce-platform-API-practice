-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 14, 2022 at 10:41 PM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `payments_processor2`
--

-- --------------------------------------------------------

--
-- Table structure for table `criteria`
--

CREATE TABLE `criteria` (
  `id` bigint(20) NOT NULL,
  `max_price_template` varchar(255) DEFAULT NULL,
  `min_price_template` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `points_template` varchar(255) DEFAULT NULL,
  `require_attributes` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `criteria`
--

INSERT INTO `criteria` (`id`, `max_price_template`, `min_price_template`, `payment_method`, `points_template`, `require_attributes`) VALUES
(1, '[PRICE]*1.01', '[PRICE]*0.98', 'AMEX', '[PRICE]*0.02', 'LAST4'),
(2, '[PRICE]', '[PRICE]', 'PAYPAY', '[PRICE]*0.01', ''),
(3, '[PRICE]', '[PRICE]*0.95', 'MASTERCARD', '[PRICE]*0.03', 'LAST4'),
(4, '[PRICE]', '[PRICE]*0.95', 'JCB', '[PRICE]*0.05', 'LAST4'),
(5, '[PRICE]', '[PRICE]*0.95', 'VISA', '[PRICE]*0.03', 'LAST4'),
(6, '[PRICE]', '[PRICE]', 'LINE PAY', '[PRICE]*0.01', ''),
(7, '[PRICE]', '[PRICE]*0.9', 'CHEQUE', '0', 'CHEQUE|BANK'),
(8, '[PRICE]', '[PRICE]', 'GRAB PAY', '[PRICE]*0.01', ''),
(9, '[PRICE]', '[PRICE]*0.9', 'CASH', '[PRICE]*0.05', ''),
(10, '[PRICE]', '[PRICE]', 'POINTS', '0', ''),
(11, '[PRICE]*1.02', '[PRICE]', 'CASH_ON_DELIVERY', '[PRICE]*0.05', 'COURIER|YAMATO,SAGAWA'),
(12, '[PRICE]', '[PRICE]', 'BANK TRANSFER', '0', 'BANK');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` bigint(20) NOT NULL,
  `customer_id` varchar(255) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `points` int(11) NOT NULL,
  `price` varchar(255) DEFAULT NULL,
  `price_modifier` float NOT NULL,
  `sales` varchar(255) DEFAULT NULL,
  `additional_item_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `customer_id`, `datetime`, `payment_method`, `points`, `price`, `price_modifier`, `sales`, `additional_item_id`) VALUES
(1, '12345', '2022-09-01 09:00:00', 'VISA', 5, '170.00', 0.95, '161.5', 1),
(2, '12345', '2022-09-01 09:00:00', 'MASTERCARD', 5, '170.00', 0.95, '161.5', NULL),
(3, '12345', '2022-09-01 09:00:00', 'MASTERCARD', 5, '170.00', 0.95, '161.5', NULL),
(4, '12345', '2022-09-01 09:00:00', 'VISA', 5, '170.00', 0.95, '161.5', 2),
(5, '12345', '2022-09-01 09:00:00', 'VISA', 5, '180.00', 0.95, '171.0', 3),
(6, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 1.95, '17.1', 4),
(7, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 1.95, '17.1', NULL),
(8, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 1.95, '17.1', NULL),
(9, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 1.95, '18.0', NULL),
(10, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 0.95, '17.1', NULL),
(11, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 1.95, '18.0', 5),
(12, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 1.95, '18.0', NULL),
(13, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 1.95, '18.0', 6),
(14, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 1.95, '18.0', 7),
(15, '12345', '2022-09-01 09:00:00', 'VISA', 1, '18.00', 1.95, '18.0', 8);

-- --------------------------------------------------------

--
-- Table structure for table `payments_additional`
--

CREATE TABLE `payments_additional` (
  `id` bigint(20) NOT NULL,
  `bank` varchar(255) DEFAULT NULL,
  `cheque` varchar(255) DEFAULT NULL,
  `courier` varchar(255) DEFAULT NULL,
  `last_4` varchar(255) DEFAULT NULL,
  `payment_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payments_additional`
--

INSERT INTO `payments_additional` (`id`, `bank`, `cheque`, `courier`, `last_4`, `payment_id`) VALUES
(1, '', '', '', '154', NULL),
(2, '', '', '', '154', NULL),
(3, '', '', '', '5432', NULL),
(4, '', '', '', '5432', NULL),
(5, '', '', '', '5432', NULL),
(6, '', '', '', '5432', NULL),
(7, '', '', '', '5432', NULL),
(8, '', '', '', '5432', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `criteria`
--
ALTER TABLE `criteria`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKj3cm6oqvev8gcbvhd2b8gu0hr` (`additional_item_id`);

--
-- Indexes for table `payments_additional`
--
ALTER TABLE `payments_additional`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKp0byng7q789vuxkbkc2v1mylv` (`payment_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `criteria`
--
ALTER TABLE `criteria`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `payments_additional`
--
ALTER TABLE `payments_additional`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `FKj3cm6oqvev8gcbvhd2b8gu0hr` FOREIGN KEY (`additional_item_id`) REFERENCES `payments_additional` (`id`);

--
-- Constraints for table `payments_additional`
--
ALTER TABLE `payments_additional`
  ADD CONSTRAINT `FKp0byng7q789vuxkbkc2v1mylv` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
