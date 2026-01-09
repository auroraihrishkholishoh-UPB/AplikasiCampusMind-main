const pool = require('../config/db');

exports.submitAssessment = async (req, res) => {
    try {
        const userId = req.user.id;
        const { sessionId, rating, feedback } = req.body;

        if (!sessionId || !rating) {
            return res.status(400).json({ success: false, message: 'sessionId and rating are required' });
        }

        if (rating < 1 || rating > 5) {
            return res.status(400).json({ success: false, message: 'Rating must be between 1 and 5' });
        }

        // pastikan sesi milik user
        const [sessions] = await pool.query(
            'SELECT id FROM counseling_sessions WHERE id = ? AND user_id = ?',
            [sessionId, userId]
        );
        if (sessions.length === 0) {
            return res.status(404).json({ success: false, message: 'Session not found or not authorized' });
        }

        await pool.query(
            'INSERT INTO assessments (session_id, user_id, rating, feedback) VALUES (?, ?, ?, ?)',
            [sessionId, userId, rating, feedback || null]
        );

        return res.status(201).json({ success: true, message: 'Assessment submitted' });

    } catch (err) {
        console.error('Submit assessment error:', err);
        return res.status(500).json({ success: false, message: 'Server error on submit assessment' });
    }
};
