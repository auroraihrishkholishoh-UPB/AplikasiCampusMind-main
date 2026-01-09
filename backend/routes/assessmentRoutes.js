const express = require('express');
const router = express.Router();

const assessmentController = require('../controllers/assessmentController');
const { requireAuth } = require('../middleware/authMiddleware'); // ✅ TANPA S

router.post('/', requireAuth, assessmentController.submitAssessment);

module.exports = router;
