const app = require("express")();
const parser = require("body-parser");

// Setup server
app.use(parser.json());
app.use(parser.urlencoded({ extended: true }));

// Mock payment system
app.post("/checkout", (req, res) => {
  var body = req.body;
  if (body) {
    console.log(`Request body: ${body}`);

    // Return response
    return res.status(201).json({
      message: "Payment was successful",
      result: body,
      error: false
    });
  } else {
    return res.status(401).json({
      message: "Cannot perform this function",
      result: body,
      error: true
    });
  }
});

// Server started
// app.listen(process.env.PORT || 9900, () => {
//   console.log("Server started successfully...");
// });

// Export module
module.exports = app;
