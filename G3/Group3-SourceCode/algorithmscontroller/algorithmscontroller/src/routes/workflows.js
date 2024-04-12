const _ = require("lodash");
const express = require("express");
const { Workflow, Algorithm, WorkflowAlgorithms, sequelize } = require("../db");
const {
  sendEvent,
  MESSAGE_TYPES,
  OPERATION_TYPES,
} = require("../services/monitoring.service");

const router = express.Router();

/**
 * Builds the standard workflow response object.
 */
const assembleWorkflow = (model) => {
  // transform from sequelize model to plain json object
  const workflow = model.get({ plain: true });

  return {
    ...workflow,
    algorithms: workflow.algorithms.map((algorithm) => ({
      ...algorithm,
      // adds the order to the algorithm object
      order: algorithm.WorkflowAlgorithms.order,
      // removes the many to many mapping form the response
      WorkflowAlgorithms: undefined,
    })),
  };
};

/**
 * Makes sure the informed algorithm ids are valid.
 */
const validateAlgorithmIds = async (algorithmIds, res) => {
  // validates the input array
  if (!algorithmIds || !Array.isArray(algorithmIds) || !algorithmIds.length) {
    res.status(400).json({ message: "The array of algorithm ids is required" });
    return false;
  }

  // ensures the ids exist on the database
  const algorithms = await Algorithm.findAll({
    where: {
      id: algorithmIds,
    },
  });

  // list of ids that were informed but do not exist
  const missingIds = algorithms.filter((a) => !algorithmIds.includes(a.id));
  if (missingIds.length) {
    res
      .status(400)
      .json({ message: `Algorithms not found: ${missingIds.join(", ")}` });
    return false;
  }

  // list is valid
  return true;
};

/**
 * Creates the algorithms mappings for the workflow.
 */
const createAlgorithmsMapping = (workflowId, algorithmIds, transaction) => {
  // make sure to remove duplicate values from algorithmIds
  // and builds the values for the bulk insert
  const values = _.uniq(algorithmIds).map((algorithmId, index) => ({
    workflowId,
    algorithmId,
    order: index + 1,
  }));

  // creates the workflow x algorithms associations
  return WorkflowAlgorithms.bulkCreate(values, { transaction });
};

router.get("/", async (req, res, next) => {
  try {
    const allWorkflows = await Workflow.findAll({
      include: [
        {
          model: Algorithm,
          as: "algorithms",
        },
      ],
    });

    res.json(allWorkflows.map(assembleWorkflow));
    sendEvent(`${allWorkflows.length} workflows returned successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.READ);
  } catch (err) {
    console.error("Error retrieving all workflows", err);
    res.status(500).send(err.message);
    next(err, req, res);
    sendEvent(`Error listing all workflows: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.READ);
  }
});

router.get("/:id", async (req, res, next) => {
  const workflowId = req.params.id;
  try {
    const workflow = await Workflow.findByPk(workflowId, {
      include: [
        {
          model: Algorithm,
          as: "algorithms",
        },
      ],
    });

    if (!workflow) {
      res.status(404).json({ message: "Workflow not found" });
      return;
    }

    res.status(200).send(assembleWorkflow(workflow));
    sendEvent(`Workflow id = ${workflowId} returned successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.READ);
  } catch (err) {
    console.error("Error retrieving the workflow id = " + workflowId, err);
    res.status(500).send(err.message);
    next(err, req, res);
    sendEvent(`Error returning the workflow id = ${workflowId}: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.READ);
  }
});

router.post("/", async (req, res, next) => {
  const algorithmIds = req.body.ids;
  try {
    if (!(await validateAlgorithmIds(algorithmIds, res))) {
      return;
    }

    const workflowId = await sequelize.transaction(async (transaction) => {
      // creates the workflow record
      const workflow = await Workflow.create({}, { transaction });
      await createAlgorithmsMapping(workflow.id, algorithmIds, transaction);
      return workflow.id;
    });

    // returns the newly created record
    const workflow = await Workflow.findByPk(workflowId, {
      include: [
        {
          model: Algorithm,
          as: "algorithms",
        },
      ],
    });

    res.status(200).send(assembleWorkflow(workflow));
    sendEvent(`Workflow id = ${workflowId} created successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.CREATE);
  } catch (err) {
    console.error("Error creating workflow", err);
    res.status(500).send(err.message);
    next(err, req, res);
    sendEvent(`Error creating workflow: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.CREATE);
  }
});

router.put("/:id", async (req, res, next) => {
  const workflowId = req.params.id;
  const { ids: algorithmIds } = req.body;

  try {
    if (!(await validateAlgorithmIds(algorithmIds, res))) {
      return;
    }

    let workflow = await Workflow.findByPk(workflowId);
    if (!workflow) {
      res.status(404).json({ message: "Workflow not found" });
      return;
    }

    await sequelize.transaction(async (transaction) => {
      // removes the existing algorithms mappings for the workflow
      await WorkflowAlgorithms.destroy({
        where: {
          workflowId,
        },
        transaction,
      });

      // line required to manually change the updatedAt date
      workflow.changed("updatedAt", true);

      // creates a new mapping with the received ids
      // updates the updatedAt date of the workflow record
      await Promise.all([
        createAlgorithmsMapping(workflow.id, algorithmIds, transaction),
        workflow.update({ updatedAt: new Date() }, { transaction }),
      ]);
    });

    // returns the updated record
    workflow = await Workflow.findByPk(workflowId, {
      include: [
        {
          model: Algorithm,
          as: "algorithms",
        },
      ],
    });

    res.status(200).send(assembleWorkflow(workflow));
    sendEvent(`Workflow id = ${workflowId} updated successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.UPDATE);
  } catch (err) {
    console.error("Error updating workflow", err);
    res.status(500).send(err.message);
    next(err, req, res);
    sendEvent(`Error updating the workflow id = ${workflowId}: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.UPDATE);
  }
});

router.delete("/:id", async (req, res, next) => {
  const id = req.params.id;
  try {
    let workflow = await Workflow.findByPk(id);
    if (!workflow) {
      res.status(404).json({ message: "Workflow not found" });
      return;
    }

    await Workflow.destroy({ where: { id } });

    res.status(200).json({ message: "Workflow deleted" });
    sendEvent(`Workflow id = ${id} deleted successully`, MESSAGE_TYPES.INFORMATION, OPERATION_TYPES.DELETE);
  } catch (err) {
    console.error("Error deleting workflow", err);
    res.status(500).send(err.message);
    next(err, req, res);
    sendEvent(`Error deleting the workflow id = ${id}: ${err.message}`, MESSAGE_TYPES.WARNING, OPERATION_TYPES.DELETE);
  }
});

module.exports = router;
