import json
import logging

from decouple import config
from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers=[config('KAFKA_SERVER')],
                         value_serializer=lambda m: json.dumps(m).encode('ascii'))

logger = logging.getLogger('Logger')


def on_send_success(record_metadata):
    logger.info(record_metadata)


def on_send_error(error):
    logger.error(error)


def log_to_kafka(log_type, operation_type, message, service_name):
    producer.send('cosn_logging', {
        'messageType': log_type,
        'operationType': operation_type,
        'message': message,
        'serviceName': service_name
    }).add_callback(on_send_success).add_errback(on_send_error)
