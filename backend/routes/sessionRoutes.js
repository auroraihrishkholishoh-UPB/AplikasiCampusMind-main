const express = require('express');
const router = express.Router();

const sessionController = require('../controllers/sessionController');
const { requireAuth } = require('../middleware/authMiddleware');

router.post('/', requireAuth, sessionController.createSession);
router.get('/:id', requireAuth, sessionController.getSessionDetail);
router.get('/', requireAuth, sessionController.getUserSessions);


module.exports = router;
