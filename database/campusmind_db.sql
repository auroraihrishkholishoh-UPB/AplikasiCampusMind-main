-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 02 Des 2025 pada 13.53
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `campusmind_db`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `assessments`
--

CREATE TABLE `assessments` (
  `id` int(11) NOT NULL,
  `session_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `rating` tinyint(4) NOT NULL,
  `feedback` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `counseling_sessions`
--

CREATE TABLE `counseling_sessions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `counselor_id` int(11) DEFAULT NULL,
  `session_type` enum('human','ai') NOT NULL,
  `status` enum('ongoing','finished','cancelled') NOT NULL DEFAULT 'ongoing',
  `started_at` datetime NOT NULL DEFAULT current_timestamp(),
  `ended_at` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `messages`
--

CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL,
  `session_id` int(11) NOT NULL,
  `sender_type` enum('user','counselor','ai') NOT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `message_type` enum('text','voice') NOT NULL DEFAULT 'text',
  `text_content` text DEFAULT NULL,
  `audio_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `nim` varchar(30) DEFAULT NULL,
  `role` enum('student','counselor','admin') NOT NULL DEFAULT 'student',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password_hash`, `nim`, `role`, `created_at`, `updated_at`) VALUES
(1, 'Noval Suprayoga', 'novalsuprayoga@gmail.com', '$2b$10$DyT7ufPEGyrOZAGayAv2MuAKXeC1BUw/uYmHycStSLUdI67teFSX.', '312410305', 'student', '2025-11-30 14:50:40', '2025-11-30 14:50:40');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `assessments`
--
ALTER TABLE `assessments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_assess_session` (`session_id`),
  ADD KEY `fk_assess_user` (`user_id`);

--
-- Indeks untuk tabel `counseling_sessions`
--
ALTER TABLE `counseling_sessions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_sessions_user` (`user_id`),
  ADD KEY `fk_sessions_counselor` (`counselor_id`);

--
-- Indeks untuk tabel `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_messages_session` (`session_id`),
  ADD KEY `idx_messages_sender` (`sender_id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `assessments`
--
ALTER TABLE `assessments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `counseling_sessions`
--
ALTER TABLE `counseling_sessions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `messages`
--
ALTER TABLE `messages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `assessments`
--
ALTER TABLE `assessments`
  ADD CONSTRAINT `fk_assess_session` FOREIGN KEY (`session_id`) REFERENCES `counseling_sessions` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_assess_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `counseling_sessions`
--
ALTER TABLE `counseling_sessions`
  ADD CONSTRAINT `fk_sessions_counselor` FOREIGN KEY (`counselor_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_sessions_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_messages_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_messages_session` FOREIGN KEY (`session_id`) REFERENCES `counseling_sessions` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
