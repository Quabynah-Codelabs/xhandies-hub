const functions = require("firebase-functions");
const admin = require("firebase-admin");

// initialize application
admin.initializeApp();

exports.helloWorld = functions.https.onRequest((req, res) => {
  return console.log("Test API reached");
});
