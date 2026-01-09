const pool = require('../config/db');

exports.getUserHistory = async (req, res) => {
    try {
        const userId = req.user.id;

        const [rows] = await pool.query(`
            SELECT 
                s.id,
                s.session_type,
                s.status,
                s.created_at AS started_at,
                (
                    SELECT 
                        COALESCE(m.text_content, 'Voice message')
                    FROM messages m
                    WHERE m.session_id = s.id
                    ORDER BY m.created_at DESC
                    LIMIT 1
                ) AS last_message
            FROM counseling_sessions s
            WHERE s.user_id = ?
            ORDER BY s.created_at DESC
        `, [userId]);

        return res.json({
            success: true,
            data: rows
        });

    } catch (err) {
        console.error('Get history error:', err);
        return res.status(500).json({
            success: false,
            message: 'Server error on get history'
        });
    }
};
