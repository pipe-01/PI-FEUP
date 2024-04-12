const express = require("express");
const cookieParser = require("cookie-parser");
const logger = require("morgan");
const swaggerUi = require("swagger-ui-express");
const actuator = require('express-actuator')

const algorithmsRouter = require("./routes/algorithms");
const workflowRouter = require("./routes/workflows");
const healthRouter = require("./routes/health");
const swaggerDocument = require("./swagger.json");

const {
  sendEvent,
  MESSAGE_TYPES,
  OPERATION_TYPES,
} = require("./services/monitoring.service");

const app = express();

try {
  app.use(logger("dev"));
  app.use(express.json());
  app.use(express.urlencoded({ extended: false }));
  app.use(cookieParser());
  app.use(actuator());

  app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerDocument));
  app.use("/algorithms", algorithmsRouter);
  app.use("/workflows", workflowRouter);
  app.use("/health-check", healthRouter);

  app.use((error, req, res, next) => {
    res.status(error.status || 500);
    res.json({
      error: {
        message: error.message,
      },
    });
  });
} catch (err) {
  sendEvent(`Error starting server: ${err.message}`, MESSAGE_TYPES.FATAL, OPERATION_TYPES.OTHER);
  throw err;
}

module.exports = app;
