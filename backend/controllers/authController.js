const pool = require('../config/db');
const bcrypt = require('bcrypt');
const { signToken } = require("../middleware/authMiddleware");

const SALT_ROUNDS = 10;

exports.signup = async (req, res) => {
    try {
        const { name, email, password, nim, role } = req.body;

        if (!name || !email || !password) {
            return res.status(400).json({ success: false, message: 'Name, email, and password are required' });
        }

        // cek email sudah dipakai
        const [existing] = await pool.query('SELECT id FROM users WHERE email = ?', [email]);
        if (existing.length > 0) {
            return res.status(409).json({ success: false, message: 'Email already registered' });
        }

        const passwordHash = await bcrypt.hash(password, SALT_ROUNDS);

        const userRole = role || 'student';

        const [result] = await pool.query(
            'INSERT INTO users (name, email, password_hash, nim, role) VALUES (?, ?, ?, ?, ?)',
            [name, email, passwordHash, nim || null, userRole]
        );

        const newUser = {
            id: result.insertId,
            name,
            email,
            nim: nim || null,
            role: userRole
        };

        const token = signToken(newUser);

        return res.status(201).json({
            success: true,
            message: 'Signup success',
            token,
            user: newUser
        });

    } catch (err) {
        console.error('Signup error:', err);
        return res.status(500).json({ success: false, message: 'Server error on signup' });
    }
};

exports.login = async (req, res) => {
    try {
        const { email, password } = req.body;

        if (!email || !password) {
            return res.status(400).json({ success: false, message: 'Email and password are required' });
        }

        const [rows] = await pool.query(
            'SELECT id, name, email, password_hash, nim, role FROM users WHERE email = ?',
            [email]
        );

        if (rows.length === 0) {
            return res.status(401).json({ success: false, message: 'Invalid email or password' });
        }

        const user = rows[0];

        const match = await bcrypt.compare(password, user.password_hash);
        if (!match) {
            return res.status(401).json({ success: false, message: 'Invalid email or password' });
        }

        const payloadUser = {
            id: user.id,
            name: user.name,
            email: user.email,
            nim: user.nim,
            role: user.role
        };

        const token = signToken(payloadUser);

        return res.json({
            success: true,
            message: 'Login success',
            token,
            user: payloadUser
        });

    } catch (err) {
        console.error('Login error:', err);
        return res.status(500).json({ success: false, message: 'Server error on login' });
    }
};
