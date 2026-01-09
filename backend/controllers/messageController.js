const pool = require('../config/db');

/**
 * GET messages by session
 * GET /sessions/:sessionId/messages
 */
exports.getMessagesBySession = async (req, res) => {
    try {
        const sessionId = req.params.sessionId;
        const user = req.user; // { id, role }

        // cek session milik user
        const [sessions] = await pool.query(
            'SELECT id FROM counseling_sessions WHERE id = ? AND user_id = ?',
            [sessionId, user.id]
        );

        if (sessions.length === 0) {
            return res.status(404).json({
                success: false,
                message: 'Session not found or unauthorized'
            });
        }

        const [messages] = await pool.query(
            `SELECT 
                id,
                sender_type,
                message_type,
                text_content,
                audio_url,
                created_at
             FROM messages
             WHERE session_id = ?
             ORDER BY created_at ASC`,
            [sessionId]
        );

        return res.json({
            success: true,
            data: messages
        });

    } catch (err) {
        console.error('Get messages error:', err);
        return res.status(500).json({
            success: false,
            message: 'Server error on get messages'
        });
    }
};

/**
 * SEND message
 * POST /sessions/:sessionId/messages
 */
exports.sendMessage = async (req, res) => {
    try {
        const sessionId = req.params.sessionId;
        const user = req.user; // { id, role }

        const { senderType, messageType, textContent, audioUrl } = req.body;

        // validasi senderType
        if (!['user', 'counselor', 'ai'].includes(senderType)) {
            return res.status(400).json({
                success: false,
                message: 'Invalid senderType'
            });
        }

        // validasi messageType
        if (!['text', 'voice'].includes(messageType)) {
            return res.status(400).json({
                success: false,
                message: 'Invalid messageType'
            });
        }

        // cek session + status
        const [sessions] = await pool.query(
            `SELECT id, status 
             FROM counseling_sessions 
             WHERE id = ? AND user_id = ?`,
            [sessionId, user.id]
        );

        if (sessions.length === 0) {
            return res.status(403).json({
                success: false,
                message: 'Session not found or unauthorized'
            });
        }

        if (sessions[0].status !== 'ongoing') {
            return res.status(400).json({
                success: false,
                message: 'Session already closed'
            });
        }

        // 🔐 role validation
        if (
            (senderType === 'user' && user.role !== 'student') ||
            (senderType === 'counselor' && user.role !== 'counselor') ||
            (senderType === 'ai' && user.role !== 'system')
        ) {
            return res.status(403).json({
                success: false,
                message: 'Invalid sender role'
            });
        }

        const senderId = senderType === 'ai' ? null : user.id;

        const [result] = await pool.query(
            `INSERT INTO messages
            (session_id, sender_type, sender_id, message_type, text_content, audio_url)
            VALUES (?, ?, ?, ?, ?, ?)`,
            [
                sessionId,
                senderType,
                senderId,
                messageType,
                messageType === 'text' ? textContent || null : null,
                messageType === 'voice' ? audioUrl || null : null
            ]
        );

        const [rows] = await pool.query(
            `SELECT 
                id,
                sender_type,
                message_type,
                text_content,
                audio_url,
                created_at
             FROM messages
             WHERE id = ?`,
            [result.insertId]
        );

        return res.status(201).json({
            success: true,
            message: 'Message sent',
            data: rows[0]
        });

    } catch (err) {
        console.error('Send message error:', err);
        return res.status(500).json({
            success: false,
            message: 'Server error on send message'
        });
    }
};
