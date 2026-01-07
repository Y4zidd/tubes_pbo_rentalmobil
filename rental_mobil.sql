-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 07, 2026 at 12:04 PM
-- Server version: 8.4.3
-- PHP Version: 8.3.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rental_mobil`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `idUser` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`idUser`) VALUES
(1);

-- --------------------------------------------------------

--
-- Table structure for table `konsumen`
--

CREATE TABLE `konsumen` (
  `noKTP` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `namaKonsumen` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tempatTinggal` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nomorTelepon` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `konsumen`
--

INSERT INTO `konsumen` (`noKTP`, `namaKonsumen`, `tempatTinggal`, `nomorTelepon`) VALUES
('123456789', 'zidan', 'bandung selatan', '087751869558'),
('3174052010950002', 'Siti Aminah', 'Perum. Griya Asri Blok B2, Bandung', '081398765432'),
('3201123456780001', 'Budi Santoso', 'Jl. Merdeka No. 45, Jakarta Pusat', '081234567890'),
('3404016512920004', 'Rina Kartika', 'Jl. Sudirman Kav. 5, Yogyakarta', '087855667788'),
('3515081505880003', 'Andi Pratama', 'Jl. Diponegoro No. 12, Surabaya', '085712340987'),
('5103021103900005', 'Dewa Gede', 'Jl. Sunset Road No. 88, Denpasar', '082133445566');

-- --------------------------------------------------------

--
-- Table structure for table `merek`
--

CREATE TABLE `merek` (
  `idMerek` int NOT NULL,
  `namaMerek` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `merek`
--

INSERT INTO `merek` (`idMerek`, `namaMerek`) VALUES
(2, 'Daihatsu'),
(4, 'Honda'),
(5, 'Mitsubishi'),
(3, 'Nissan'),
(1, 'Toyota');

-- --------------------------------------------------------

--
-- Table structure for table `mobil`
--

CREATE TABLE `mobil` (
  `idMobil` int NOT NULL,
  `idMerek` int NOT NULL,
  `namaMobil` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipeMobil` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `platNomor` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tahun` year NOT NULL,
  `hargaSewaHarian` int NOT NULL,
  `gambarPath` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('TERSEDIA','DISEWA','SERVICE','NONAKTIF') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TERSEDIA'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `mobil`
--

INSERT INTO `mobil` (`idMobil`, `idMerek`, `namaMobil`, `tipeMobil`, `platNomor`, `tahun`, `hargaSewaHarian`, `gambarPath`, `status`) VALUES
(10, 1, 'Avanza Veloz', 'MPV', 'B 1234 KJA', '2024', 450000, NULL, 'TERSEDIA'),
(11, 4, 'Brio Satya', 'City Car', 'D 5678 XY', '2023', 300000, NULL, 'TERSEDIA'),
(12, 5, 'Xpander', 'MPV', 'B 2291 POA', '2025', 500000, NULL, 'TERSEDIA'),
(13, 2, 'Sigra', 'MPV', 'F 1011 AB', '2023', 350000, NULL, 'TERSEDIA'),
(15, 3, 'Grand Livina', 'MPV', 'B 1990 CIS', '2022', 400000, NULL, 'TERSEDIA'),
(16, 1, 'Innova Zenix', 'MPV', 'B 8899 RFS', '2025', 750000, NULL, 'TERSEDIA'),
(17, 4, 'HR-V', 'SUV', 'AB 4455 LM', '2024', 600000, NULL, 'TERSEDIA'),
(18, 5, 'Pajero Sport', 'SUV', 'B 1122 PJR', '2025', 1200000, NULL, 'TERSEDIA'),
(19, 2, 'Terios', 'SUV', 'D 3344 GHI', '2024', 450000, NULL, 'TERSEDIA'),
(20, 3, 'Magnite', 'SUV', 'F 6789 UY', '2023', 400000, 'C:\\Users\\Yazid\\Pictures\\images.jpg', 'DISEWA'),
(21, 1, 'inova 123', 'SUV', 'B 1234 HJ', '2023', 250000, 'C:\\Users\\Yazid\\Pictures\\erd.jpeg', 'TERSEDIA');

-- --------------------------------------------------------

--
-- Table structure for table `petugas`
--

CREATE TABLE `petugas` (
  `idUser` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `petugas`
--

INSERT INTO `petugas` (`idUser`) VALUES
(4),
(5);

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `idTransaksi` int NOT NULL,
  `noKTP` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `idMobil` int NOT NULL,
  `idUserPetugas` int NOT NULL,
  `tglSewa` date NOT NULL,
  `tglKembaliRencana` date DEFAULT NULL,
  `tglKembali` date DEFAULT NULL,
  `denda` int NOT NULL DEFAULT '0',
  `status` enum('DISEWA','SELESAI','BATAL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DISEWA',
  `totalBayar` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`idTransaksi`, `noKTP`, `idMobil`, `idUserPetugas`, `tglSewa`, `tglKembaliRencana`, `tglKembali`, `denda`, `status`, `totalBayar`) VALUES
(2, '3515081505880003', 18, 4, '2026-01-06', '2026-01-14', '2026-01-06', 0, 'SELESAI', 9600000),
(3, '3515081505880003', 16, 4, '2026-01-06', '2026-01-07', '2026-01-09', 100000, 'SELESAI', 750000),
(4, '123456789', 21, 4, '2026-01-06', '2026-01-08', '2026-01-09', 50000, 'SELESAI', 500000),
(5, '123456789', 20, 4, '2026-01-07', '2026-01-16', NULL, 0, 'DISEWA', 3600000);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `idUser` int NOT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `namaLengkap` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `level` enum('ADMIN','PETUGAS') COLLATE utf8mb4_unicode_ci NOT NULL,
  `photoPath` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`idUser`, `username`, `password`, `namaLengkap`, `level`, `photoPath`) VALUES
(1, 'admin', 'admin123', 'Administrator', 'ADMIN', 'C:\\Users\\Yazid\\Downloads\\6325625561967344108.jpg'),
(4, 'yazid', 'Yazide.123', 'Yazid Istiqlal Adhy Fadhillah', 'PETUGAS', 'C:\\Users\\Yazid\\Downloads\\29848041.jpg'),
(5, 'setiadi', 'setiadi123', 'Setiadi Agusnawan', 'PETUGAS', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`idUser`);

--
-- Indexes for table `konsumen`
--
ALTER TABLE `konsumen`
  ADD PRIMARY KEY (`noKTP`);

--
-- Indexes for table `merek`
--
ALTER TABLE `merek`
  ADD PRIMARY KEY (`idMerek`),
  ADD UNIQUE KEY `namaMerek` (`namaMerek`);

--
-- Indexes for table `mobil`
--
ALTER TABLE `mobil`
  ADD PRIMARY KEY (`idMobil`),
  ADD UNIQUE KEY `platNomor` (`platNomor`),
  ADD KEY `fk_mobil_merek` (`idMerek`);

--
-- Indexes for table `petugas`
--
ALTER TABLE `petugas`
  ADD PRIMARY KEY (`idUser`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`idTransaksi`),
  ADD KEY `fk_transaksi_konsumen` (`noKTP`),
  ADD KEY `fk_transaksi_mobil` (`idMobil`),
  ADD KEY `fk_transaksi_petugas` (`idUserPetugas`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `merek`
--
ALTER TABLE `merek`
  MODIFY `idMerek` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `mobil`
--
ALTER TABLE `mobil`
  MODIFY `idMobil` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `idTransaksi` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `idUser` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `fk_admin_user` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `mobil`
--
ALTER TABLE `mobil`
  ADD CONSTRAINT `fk_mobil_merek` FOREIGN KEY (`idMerek`) REFERENCES `merek` (`idMerek`);

--
-- Constraints for table `petugas`
--
ALTER TABLE `petugas`
  ADD CONSTRAINT `fk_petugas_user` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `fk_transaksi_konsumen` FOREIGN KEY (`noKTP`) REFERENCES `konsumen` (`noKTP`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_transaksi_mobil` FOREIGN KEY (`idMobil`) REFERENCES `mobil` (`idMobil`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_transaksi_petugas` FOREIGN KEY (`idUserPetugas`) REFERENCES `petugas` (`idUser`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
