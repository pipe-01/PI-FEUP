const express = require("express");
const { sequelize } = require("../db");

const router = express.Router();

router.get("/", async (req, res, next) => {
  try {
    await sequelize.authenticate();
    res.status(200).json({status: "UP"});
  } catch (err) {
    console.log("Error on health check", err);
    res.status(500).json({status: "DOWN"});
  }
});

module.exports = router;
