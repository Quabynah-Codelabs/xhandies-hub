const functions = require('firebase-functions');
const admin = require('firebase-admin');

// Initialize Firebase Admin setup
admin.initializeApp();

// Send notification to new users
exports.welcomeNewUser = functions.auth.user().onCreate((record, context) => {
    
});

// Send notification containing user purchase receipts


// Notify users of new items in the catalogues

