const jwt = require("jsonwebtoken");

/**
 * Ambil token dari header:
 * Authorization: Bearer <token>
 */
function getTokenFromHeader(req) {
    const authHeader = req.headers.authorization || req.headers.Authorization;
    if (!authHeader) return null;

    const parts = authHeader.split(" ");
    if (parts.length !== 2) return null;

    const [scheme, token] = parts;
    if (!/^Bearer$/i.test(scheme)) return null;

    return token;
}

/**
 * Middleware: Wajib login (token harus valid)
 * - kalau valid -> req.user terisi payload token
 */
function requireAuth(req, res, next) {
    const token = getTokenFromHeader(req);

    if (!token) {
        return res.status(401).json({
            status: false,
            message: "Unauthorized: token tidak ditemukan",
        });
    }

    try {
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        req.user = decoded; // { id, email, role, iat, exp }
        return next();
    } catch (err) {
        return res.status(401).json({
            status: false,
            message: "Unauthorized: token tidak valid / expired",
        });
    }
}

/**
 * Middleware: Optional login
 * - token ada & valid -> req.user terisi
 * - token tidak ada -> lanjut tanpa error (req.user undefined)
 */
function optionalAuth(req, res, next) {
    const token = getTokenFromHeader(req);
    if (!token) return next();

    try {
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        req.user = decoded;
    } catch (err) {
        // token invalid? anggap sebagai guest, lanjut aja
    }
    return next();
}

/**
 * Middleware: Role-based authorization
 * Pakai setelah requireAuth
 * contoh: requireRole("admin"), requireRole(["admin","counselor"])
 */
function requireRole(roles) {
    const allowed = Array.isArray(roles) ? roles : [roles];

    return (req, res, next) => {
        if (!req.user) {
            return res.status(401).json({
                status: false,
                message: "Unauthorized: token diperlukan",
            });
        }

        const userRole = req.user.role || "user";
        if (!allowed.includes(userRole)) {
            return res.status(403).json({
                status: false,
                message: "Forbidden: akses ditolak",
            });
        }

        return next();
    };
}

/**
 * Helper: generate JWT token (dipakai di controller login)
 */
function signToken(payload) {
    // payload minimal: { id, email, role }
    return jwt.sign(payload, process.env.JWT_SECRET, {
        expiresIn: process.env.JWT_EXPIRES_IN || "1d",
    });
}

module.exports = {
    requireAuth,
    optionalAuth,
    requireRole,
    signToken,
};
