const axios = require('axios');
const pool = require('../config/db');

exports.chatWithAI = async (req, res) => {
    const { message, user_Id } = req.body;

    if (!user_Id || !message || !message.trim()) {
        return res.status(400).json({
            status: false,
            message: 'user_Id dan message wajib diisi',
        });
    }

    const conn = await pool.getConnection();

    try {
        // 1. pastikan user ada
        const [users] = await conn.query(
            'SELECT id FROM users WHERE id = ?',
            [user_Id]
        );

        if (users.length === 0) {
            conn.release();
            return res.status(404).json({
                status: false,
                message: 'User tidak ditemukan',
            });
        }

        await conn.beginTransaction();

        // 2. ambil / buat session AI
        const [sessions] = await conn.query(
            `SELECT id FROM counseling_sessions
             WHERE user_id = ? AND session_type = 'ai' AND status = 'ongoing'
             ORDER BY id DESC LIMIT 1`,
            [user_Id]
        );

        let sessionId;
        if (sessions.length === 0) {
            const [result] = await conn.query(
                `INSERT INTO counseling_sessions (user_id, session_type, status)
                 VALUES (?, 'ai', 'ongoing')`,
                [user_Id]
            );
            sessionId = result.insertId;
        } else {
            sessionId = sessions[0].id;
        }

        // 3. simpan pesan user
        await conn.query(
            `INSERT INTO messages (session_id, sender_type, sender_id, text_content)
             VALUES (?, 'user', ?, ?)`,
            [sessionId, user_Id, message]
        );

        // 4. panggil AI DeepSeek
       const aiResponse = await axios.post(
  process.env.DEEPSEEK_API_URL,
  {
    model: 'deepseek-chat',
    messages: [
      {
        role: 'system',
        content:
          'You are a warm, empathetic campus counselor helping Indonesian college students.'
      },
      {
        role: 'user',
        content: message
      }
    ]
  },
  {
    headers: {
      Authorization: `Bearer ${process.env.DEEPSEEK_API_KEY}`,
      'Content-Type': 'application/json'
    }
  }
);


        const aiReply =
            aiResponse?.data?.choices?.[0]?.message?.content ??
            'Maaf, aku lagi kesulitan menjawab sekarang. Coba sebentar lagi ya.';

        // 5. simpan balasan AI
        await conn.query(
            `INSERT INTO messages (session_id, sender_type, text_content)
             VALUES (?, 'ai', ?)`,
            [sessionId, aiReply]
        );

        await conn.commit();
        conn.release();

        return res.json({
            status: true,
            sessionId,
            reply: aiReply,
        });
    } catch (error) {
    await conn.rollback();
    conn.release();

    console.error('AI Chat Error STATUS:', error.response?.status);
    console.error('AI Chat Error DATA:', error.response?.data);
    console.error('AI Chat Error MSG:', error.message);

    const message =
        error.response?.data?.error?.message ||
        'Terjadi kesalahan saat memproses chat AI';

    return res.status(error.response?.status || 500).json({
        status: false,
        message
    });
}

};
