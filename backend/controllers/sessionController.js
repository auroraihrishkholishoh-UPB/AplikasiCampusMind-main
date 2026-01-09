const pool = require('../config/db');

exports.createSession = async (req, res) => {
    try {
        const userId = req.user.id;  // dari JWT
        const { sessionType, counselorId } = req.body; // 'human' atau 'ai'

        if (!sessionType || !['human', 'ai'].includes(sessionType)) {
            return res.status(400).json({ success: false, message: 'Invalid sessionType' });
        }

        let counselor_id_value = null;
        if (sessionType === 'human') {
            if (!counselorId) {
                return res.status(400).json({ success: false, message: 'counselorId required for human session' });
            }
            counselor_id_value = counselorId;
        }

        const [result] = await pool.query(
            'INSERT INTO counseling_sessions (user_id, counselor_id, session_type) VALUES (?, ?, ?)',
            [userId, counselor_id_value, sessionType]
        );

        return res.status(201).json({
            success: true,
            message: 'Session created',
            sessionId: result.insertId
        });

    } catch (err) {
        console.error('Create session error:', err);
        return res.status(500).json({ success: false, message: 'Server error on create session' });
    }
};
exports.getUserSessions = async (req, res) => {
    try {
        const userId = req.user.id; // ambil dari JWT

        // ambil semua session user
        const [sessions] = await pool.query(
            `SELECT cs.*, u.name AS counselor_name
             FROM counseling_sessions cs
             LEFT JOIN users u ON cs.counselor_id = u.id
             WHERE cs.user_id = ?
             ORDER BY cs.started_at DESC`,
            [userId]
        );

        // optional: ambil messages dan assessment untuk tiap session
        for (let session of sessions) {
            const [messages] = await pool.query(
                'SELECT * FROM messages WHERE session_id = ? ORDER BY created_at ASC',
                [session.id]
            );
            session.messages = messages;

            const [assessments] = await pool.query(
                'SELECT * FROM assessments WHERE session_id = ? AND user_id = ?',
                [session.id, userId]
            );
            session.assessment = assessments[0] || null;
        }

        return res.json({ success: true, sessions });

    } catch (err) {
        console.error('Get user sessions error:', err);
        return res.status(500).json({ success: false, message: 'Server error on get sessions' });
    }
};

exports.getSessionDetail = async (req, res) => {
    try {
        const sessionId = req.params.id;
        const userId = req.user.id;

        const [sessions] = await pool.query(
            'SELECT * FROM counseling_sessions WHERE id = ? AND user_id = ?',
            [sessionId, userId]
        );

        if (sessions.length === 0) {
            return res.status(404).json({ success: false, message: 'Session not found' });
        }

        const session = sessions[0];

        const [messages] = await pool.query(
            'SELECT * FROM messages WHERE session_id = ? ORDER BY created_at ASC',
            [sessionId]
        );

        return res.json({
            success: true,
            session,
            messages
        });

    } catch (err) {
        console.error('Get session detail error:', err);
        return res.status(500).json({ success: false, message: 'Server error on get session' });
    }
};
