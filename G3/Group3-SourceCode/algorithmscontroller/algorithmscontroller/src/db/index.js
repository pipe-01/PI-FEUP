const { Sequelize } = require("sequelize");

const initAlgorithm = require("./Algorithm");
const initWorkflow = require("./Workflow");
const initWorkflowAlgorithms = require("./WorkflowAlgorithms");

const database = process.env.PG_DB || "cosn";
const user = process.env.PG_USER || "cosn";
const password = process.env.PG_PASSWORD || "cosn";
const host = process.env.PG_HOST || "localhost";
const port = process.env.PG_PORT || "7777";
const sslRequired = process.env.PG_SSL === "true";

// connects to the database
console.log("Connectiong to postgres...");

const options = {
  host,
  port,
  logging: console.log,
  dialect: "postgresql",
  pool: {
    min: 0,
    max: 10,
  },
};

if (sslRequired) {
  options.dialectOptions = {
    ssl: {
      require: true,
      rejectUnauthorized: false,
    },
  };
}

const sequelize = new Sequelize(database, user, password, options);

console.log("Postgres connected!");

const Algorithm = initAlgorithm(sequelize);
const Workflow = initWorkflow(sequelize);
const WorkflowAlgorithms = initWorkflowAlgorithms(sequelize);

Algorithm.belongsToMany(Workflow, {
  through: WorkflowAlgorithms,
  foreignKey: "algorithmId",
  as: "workflows",
});
Workflow.belongsToMany(Algorithm, {
  through: WorkflowAlgorithms,
  foreignKey: "workflowId",
  as: "algorithms",
});

module.exports = {
  sequelize,
  Algorithm,
  Workflow,
  WorkflowAlgorithms,
};
