const express = require('express');
const router = express.Router();
const { chatWithAI } = require('../controllers/aiController');

router.post('/message', chatWithAI);

module.exports = router;
