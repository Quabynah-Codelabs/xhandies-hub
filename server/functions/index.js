const functions = require('firebase-functions');
const admin = require('firebase-admin');

// Initialize Firebase Admin setup
admin.initializeApp();

// Store new user information
exports.storeUser = functions.auth.user().onCreate((record, context) => {
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

// Send notification to new users
exports.welcomeNewUser = functions.firestore.document('customers/{uid}').onWrite((snapshot, ctx) => {
    // Get the snapshot data
    if (snapshot.before.exists) {
        var oldToken = snapshot.before.data().token;
        var newToken = snapshot.after.data().token;

        // Compare tokens and send notification to the new device
        if (oldToken != newToken) {
            return admin.messaging().sendToDevice(newToken, {
                data: {
                    title: "Welcome to Xhandie's Hub!",
                    message: "Have a wonderful shopping experience",
                    timestamp: `${snapshot.after.updateTime.toDate().getTime()}`,
                    type: 'new-user-token-update'
                }
            }).then(() => {
                return console.log("Notification sent successfully");
            }).catch(err => {
                if (err) {
                    return console.log(err.message);
                }
            });
        }
    } else {
        return console.log("No change in user's token");
    }
});

// Send notification containing user purchase receipts


// Notify users of new items in the catalogues