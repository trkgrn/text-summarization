from kafka import KafkaProducer
import json


class MessageProducer:
    broker = "127.0.0.1:9092"
    topic = "stage"
    producer = None

    def __init__(self):
        self.producer = KafkaProducer(bootstrap_servers=self.broker,
                                      value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                                      acks='all',
                                      retries=3)

    def send_msg(self, msg):
        print("\n\nsending message to kafka...")
        try:
            future = self.producer.send(self.topic, msg)
            self.producer.flush()
            future.get(timeout=60)
            print("message sent successfully...")
            return {'status_code': 200, 'error': None}
        except Exception as ex:
            return ex


# message_producer = MessageProducer()
# message_producer.send_msg("name=245e68fb-d079-4224-baa9-adb98e029201,stage=STEP_5")
