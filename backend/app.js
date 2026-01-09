const express = require('express');
const cors = require('cors');
require('dotenv').config();

const pool = require('./config/db');

const authRoutes = require('./routes/authRoutes');
const sessionRoutes = require('./routes/sessionRoutes');
const messageRoutes = require('./routes/messageRoutes');
const assessmentRoutes = require('./routes/assessmentRoutes');
const userRoutes = require('./routes/userRoutes');
const aiRoutes = require('./routes/aiRoutes');
const historyRoutes = require('./routes/historyRoutes');

const app = express();

// ===== MIDDLEWARE =====
app.use(cors());
app.use(express.json());

// ===== STATIC FILE =====
app.use('/uploads', express.static('uploads'));

// ===== DB TEST =====
pool.getConnection()
    .then(conn => {
        console.log('✅ MySQL connected');
        conn.release();
    })
    .catch(err => {
        console.error('❌ MySQL connection error:', err.message);
    });

// ===== ROOT =====
app.get('/', (req, res) => {
    res.json({ message: 'CampusMind API is running' });
});

// ===== ROUTES (FINAL & KONSISTEN) =====
app.use('/api/auth', authRoutes);
app.use('/api/sessions', sessionRoutes);
app.use('/api/messages', messageRoutes);
app.use('/api/assessments', assessmentRoutes);
app.use('/api/users', userRoutes);
app.use('/api/ai', aiRoutes);
app.use('/api', historyRoutes);

// ===== START SERVER =====
const PORT = process.env.PORT || 5000;
app.listen(PORT, '0.0.0.0', () => {
    console.log(`🚀 Server running on port ${PORT}`);
});
