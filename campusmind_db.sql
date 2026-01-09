-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 09 Jan 2026 pada 11.02
-- Versi server: 10.1.38-MariaDB
-- Versi PHP: 5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
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
  `feedback` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `assessments`
--

INSERT INTO `assessments` (`id`, `session_id`, `user_id`, `rating`, `feedback`, `created_at`) VALUES
(1, 5, 2, 5, 'bagus', '2026-01-09 03:13:00');

-- --------------------------------------------------------

--
-- Struktur dari tabel `counseling_sessions`
--

CREATE TABLE `counseling_sessions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `counselor_id` int(11) DEFAULT NULL,
  `session_type` enum('human','ai') COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('ongoing','finished','cancelled') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ongoing',
  `started_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ended_at` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `counseling_sessions`
--

INSERT INTO `counseling_sessions` (`id`, `user_id`, `counselor_id`, `session_type`, `status`, `started_at`, `ended_at`, `created_at`) VALUES
(5, 2, NULL, 'ai', 'finished', '2026-01-09 10:07:39', '2026-01-09 10:12:00', '2026-01-09 03:07:39');

-- --------------------------------------------------------

--
-- Struktur dari tabel `messages`
--

CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL,
  `session_id` int(11) NOT NULL,
  `sender_type` enum('user','counselor','ai') COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `message_type` enum('text','voice') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'text',
  `text_content` text COLLATE utf8mb4_unicode_ci,
  `audio_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `messages`
--

INSERT INTO `messages` (`id`, `session_id`, `sender_type`, `sender_id`, `message_type`, `text_content`, `audio_url`, `created_at`) VALUES
(5, 5, 'user', 2, 'text', 'capek', NULL, '2026-01-09 03:07:39'),
(6, 5, 'ai', NULL, 'text', 'Aduh, pasti hari ini banyak banget yang kamu lalui ya. Rasanya berat sekali sampai capek begini. Coba cerita, apa yang bikin kamu merasa begitu lelah?', NULL, '2026-01-09 03:07:42'),
(7, 5, 'user', 2, 'text', 'gatau kenapa', NULL, '2026-01-09 03:07:57'),
(8, 5, 'ai', NULL, 'text', 'Halo, aku di sini untuk mendengarkan. Bisa ceritakan lebih banyak tentang apa yang kamu rasakan? Apa ada sesuatu yang sedang mengganggu pikiranmu?', NULL, '2026-01-09 03:08:00');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nim` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` enum('student','counselor','admin') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'student',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `avatar`, `password_hash`, `nim`, `role`, `created_at`, `updated_at`) VALUES
(1, 'Noval Suprayoga', 'novalsuprayoga@gmail.com', NULL, '$2b$10$DyT7ufPEGyrOZAGayAv2MuAKXeC1BUw/uYmHycStSLUdI67teFSX.', '312410305', 'student', '2025-11-30 14:50:40', '2025-11-30 14:50:40'),
(2, 'aurora', 'aurora@gmail.com', 'foto1.jpg', '$2b$10$jkBpLwf4SuFRLU32APGJUuVUu/AdwB5ECE0vuQHLYfEur8ooQTqBG', '12345678', 'student', '2025-12-03 10:16:07', '2026-01-09 01:18:10');

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `counseling_sessions`
--
ALTER TABLE `counseling_sessions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `messages`
--
ALTER TABLE `messages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

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
