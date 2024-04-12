const { v4: uuidv4 } = require("uuid");
const Sequelize = require("sequelize");

const { Model, DataTypes } = Sequelize;

function initWorkflow(sequelize) {
  class Workflow extends Model {}

  Workflow.init(
    {
      id: {
        type: DataTypes.UUID,
        defaultValue: uuidv4,
        primaryKey: true
      }
    },
    {
      sequelize,
      tableName: "workflows",
      underscored: true
    }
  );

  return Workflow;
}

module.exports = initWorkflow;
