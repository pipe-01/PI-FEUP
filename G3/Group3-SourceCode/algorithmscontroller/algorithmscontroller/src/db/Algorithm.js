const { v4: uuidv4 } = require("uuid");
const Sequelize = require("sequelize");

const { Model, DataTypes } = Sequelize;

function initAlgorithm(sequelize) {
  class Algorithm extends Model {}

  
  Algorithm.init(
    {
      id: {
        type: DataTypes.UUID,
        defaultValue: uuidv4,
        primaryKey: true,
      },
      name: {
        type: DataTypes.STRING,
        allowNull: false,
      },
      description: {
        type: DataTypes.STRING,
        allowNull: true,
      },
      command: {
        type: DataTypes.STRING,
        allowNull: false,
      },
    },
    {
      sequelize,
      tableName: "algorithms",
      underscored: true
    }
  );

  return Algorithm;
}

module.exports = initAlgorithm;
