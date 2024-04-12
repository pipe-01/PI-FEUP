const { v4: uuidv4 } = require("uuid");
const Sequelize = require("sequelize");

const { Model, DataTypes } = Sequelize;

function initWorkflowAlgorithms(sequelize) {
  class WorkflowAlgorithms extends Model {}

  WorkflowAlgorithms.init(
    {
      workflowId: {
        type: DataTypes.UUID,
        primaryKey: true
      },
      algorithmId: {
        type: DataTypes.UUID,
        primaryKey: true
      },
      order: {
        type: DataTypes.INTEGER,
        allowNull: false
      },
    },
    {
      sequelize,
      tableName: "workflow_algorithms",
      underscored: true
    }
  );

  return WorkflowAlgorithms;
}

module.exports = initWorkflowAlgorithms;
