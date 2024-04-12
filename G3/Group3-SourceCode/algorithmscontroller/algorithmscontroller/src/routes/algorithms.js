const express = require("express");
const { Algorithm } = require("../db");
const {
  sendEvent,
  MESSAGE_TYPES,
  OPERATION_TYPES,
} = require("../services/monitoring.service");

const router = express.Router();

router.get("/", async (req, res, next) => {
  try {
    const allAlgorithms = await Algorithm.findAll();
    res.json(allAlgorithms);
    sendEvent(`${allAlgorithms.length} algorithms returned successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.READ);
  } catch (err) {
    console.error("Error retrieving all algorithms", err);
    res.status(500).send(err.message);
    next(err, req, res);
    sendEvent(`Error listing all algorithms: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.READ);
  }
});

router.get("/:id", async (req, res, next) => {
  const id = req.params.id;
  try {
    const algorithm = await Algorithm.findByPk(id);
    if (!algorithm) {
      res.status(404).json({ message: "Algorithm not found" });
      return;
    }
    res.json(algorithm);
    sendEvent(`Algorithm id = ${id} returned successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.READ);
  } catch (err) {
    console.error("Error retrieving the algorithm id = " + id, err);
    res.status(500).send(err.message);
    next(err, req, res);
    sendEvent(`Error returning the algorithm id = ${id}: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.READ);
  }
});

router.post("/", async (req, res, next) => {
  try {
    const newAlgorithm = await Algorithm.create({
      name: req.body.name,
      description: req.body.description,
      command: req.body.command,
      createdAt: new Date(),
      updatedAt: new Date(),
    });
    res.status(200).send(newAlgorithm);
    sendEvent(`Algorithm id = ${newAlgorithm.id} created successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.CREATE);
  } catch (err) {
    console.error("Error in insert new algorithm", err);
    res.status(500).send(err.message);
    next(err, req, res);
    sendEvent(`Error creating algorithm: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.CREATE);
  }
});

// Updates an algorithm
router.put("/:id", async (req, res, next) => {
  const id = req.params.id;
  const { name, command, description } = req.body;
  try {
    const algorithm = await Algorithm.findByPk(id);
    if (!algorithm) {
      res.status(404).json({ message: "Algorithm not found" });
      return;
    }

    if (name) {
      algorithm.name = req.body.name;
    }

    if (command) {
      algorithm.command = req.body.command;
    }

    if (description) {
      algorithm.description = req.body.description;
    }

    if (name || command || description) {
      algorithm.changed("updatedAt", true);
      algorithm.updatedAt = new Date();
      await algorithm.save();
    }

    res.status(200).json(algorithm);
    sendEvent(`Algorithm id = ${id} updated successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.UPDATE);
  } catch (err) {
    console.error("Error in insert new algorithm", err);
    next(err, req, res);
    sendEvent(`Error updating the algorithm id = ${id}: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.UPDATE);
  }
});

// Deletes an algorithm
router.delete("/:id", async (req, res, next) => {
  const id = req.params.id;
  try {
    const algorithm = await Algorithm.findByPk(id);

    if (!algorithm) {
      res.status(404).json({ message: "Algorithm not found" });
      return;
    }

    await Algorithm.destroy({ where: { id } });

    res.status(200).json({ message: "Deleted Successfully" });
    sendEvent(`Algorithm id = ${id} deleted successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.DELETE);
  } catch (err) {
    console.error("Error in insert new algorithm", err);
    res.status(500).send(err.message);
    next(err, req, res);
    sendEvent(`Error deleting the algorithm id = ${id}: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.DELETE);
  }
});

module.exports = router;
