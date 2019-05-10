const functions = require('firebase-functions');
const admin = require('firebase-admin');

// Initialize Firebase Admin setup
admin.initializeApp();

// Send notification to new users
exports.welcomeNewUser = functions.auth.user().onCreate((record, context) => {
    // Get the user login credentials
    var username = record.displayName;
    var email = record.email;
    var key = record.uid;
    var timestamp = context.timestamp;

    // Store user's information in the database
    return admin.firestore().collection('customers').doc(key)
        .set({
            email,
            username,
            key,
            timestamp
        }).then(() => {
            return console.log('new customer information was stored in the database successfully');
        }).catch(error => {
            if (error) {
                return console.log(error.message);
            }
        });
});

// Send notification containing user purchase receipts


// Notify users of new items in the catalogues