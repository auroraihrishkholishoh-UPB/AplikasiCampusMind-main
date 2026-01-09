const pool = require('../config/db');

// ================= UPDATE PROFILE =================
exports.updateProfile = async (req, res) => {
  try {
    const { id, name, email } = req.body;

    if (!id || !name || !email) {
      return res.status(400).json({
        message: 'ID, name, dan email wajib diisi'
      });
    }

    const [emailCheck] = await pool.query(
      'SELECT id FROM users WHERE email = ? AND id != ?',
      [email, id]
    );

    if (emailCheck.length > 0) {
      return res.status(409).json({
        message: 'Email sudah digunakan'
      });
    }

    await pool.query(
      'UPDATE users SET name = ?, email = ? WHERE id = ?',
      [name, email, id]
    );

    res.json({
      success: true,
      message: 'Profil berhasil diperbarui'
    });

  } catch (error) {
    console.error('updateProfile error:', error);
    res.status(500).json({ message: 'Server error' });
  }
};

// ================= GET PROFILE =================
exports.getProfile = async (req, res) => {
  try {
    const { id } = req.body;

    if (!id) {
      return res.status(400).json({ message: 'User ID is required' });
    }

    const [rows] = await pool.query(
      'SELECT id, name, email, nim, role, avatar FROM users WHERE id = ?',
      [id]
    );

    if (rows.length === 0) {
      return res.status(404).json({ message: 'User not found' });
    }

    res.json(rows[0]);

  } catch (error) {
    console.error('getProfile error:', error);
    res.status(500).json({ message: 'Server error' });
  }
};

// ================= UPLOAD AVATAR =================
exports.uploadAvatar = async (req, res) => {
  try {
    const { userId } = req.body;

    if (!userId) {
      return res.status(400).json({ message: 'User ID is required' });
    }

    if (!req.file) {
      return res.status(400).json({ message: 'Avatar file is required' });
    }

    const avatarPath = `/uploads/avatars/${req.file.filename}`;

    await pool.query(
      'UPDATE users SET avatar = ? WHERE id = ?',
      [avatarPath, userId]
    );

    res.json({
      success: true,
      message: 'Avatar updated successfully',
      avatar: avatarPath
    });

  } catch (error) {
    console.error('uploadAvatar error:', error);
    res.status(500).json({ message: 'Server error' });
  }
};
