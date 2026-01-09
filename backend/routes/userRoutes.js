const express = require('express');
const router = express.Router();
const multer = require('multer');
const path = require('path');
const userController = require('../controllers/userController');

// ===== MULTER CONFIG =====
const storage = multer.diskStorage({
  destination: 'uploads/avatars',
  filename: (req, file, cb) => {
    cb(null, Date.now() + path.extname(file.originalname));
  }
});

const upload = multer({ storage });

// ===== ROUTES =====
router.post('/profile', userController.getProfile);
router.post('/update-profile', userController.updateProfile); // ✅ TAMBAH INI
router.post('/upload-avatar', upload.single('avatar'), userController.uploadAvatar);

module.exports = router;
