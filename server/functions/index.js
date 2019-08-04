const functions = require("firebase-functions");
const admin = require("firebase-admin");
const api = require('./api');

// initialize application
admin.initializeApp();

// Payment API
exports.payment = functions.https.onRequest(api);
