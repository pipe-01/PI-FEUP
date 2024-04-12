const kafka = require("kafka-node");

const MESSAGE_TYPES = {
  INFORMATION: "INFORMATION",
  WARNING: "WARNING",
  FATAL: "FATAL",
  DEBUG: "DEBUG",
  OTHER: "OTHER",
};

const OPERATION_TYPES = {
  CREATE: "CREATE",
  UPDATE: "UPDATE",
  DELETE: "DELETE",
  READ: "READ",
  OTHER: "OTHER",
};

const SERVICE_NAME = "algorithmsservice";
const TOPIC_NAME = "cosn_logging";

const KAFKA_HOST = process.env.KAFKA_HOST || "feupcosn2122.fe.up.pt";
const KAFKA_PORT = process.env.KAFKA_PORT || "9092";

const client = new kafka.KafkaClient({
  kafkaHost: `${KAFKA_HOST}:${KAFKA_PORT}`,
});
const producer = new kafka.Producer(client);

function sendEvent(message, messageType, operationType) {
  try {
    const payload = {
      topic: TOPIC_NAME,
      messages: JSON.stringify({
        serviceName: SERVICE_NAME,
        message,
        messageType,
        operationType,
      }),
    };

    producer.send([payload], (err, data) => {
      if (err) {
        console.error("Error pushing message to kafka", err);
      }
      if (data) {
        console.log("Kafka response", data);
      }
    });
  } catch (err) {
    console.error("Error pushing message to kafka", err);
  }
}

module.exports = {
  sendEvent,
  MESSAGE_TYPES,
  OPERATION_TYPES,
};
