const express = require('express');
const router = express.Router();

const { requireAuth } = require('../middleware/authMiddleware');
const messageController = require('../controllers/messageController');

router.get(
    '/sessions/:sessionId/messages',
    requireAuth,
    messageController.getMessagesBySession
);

router.post(
    '/sessions/:sessionId/messages',
    requireAuth,
    messageController.sendMessage
);

module.exports = router;
