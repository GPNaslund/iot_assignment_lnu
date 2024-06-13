#Parts of this class was reused by code provided in the project at assignment.

from umqtt.simple import MQTTClient
import config
import uasyncio

class MqttManager:
    def __init__(self):
        self.client_id = config.CLIENT_ID
        self.client = MQTTClient(self.client_id, config.MQTT_HOST, 
                                 user=config.MQTT_USERNAME, 
                                 password=config.MQTT_KEY, port=1883)
        
    async def connect(self):
        while True:
            try:
                self.client.connect()
                print("Connected to MQTT broker")
                break
            except Exception as e:
                print(f"Connection failed: {e}, retrying...")
                await uasyncio.sleep(5)
        return self.client

    async def publish(self, topic, data):
        while True:
            try:
                self.client.publish(topic, str(data), retain=False, qos=1)
                print(f"Published to {topic}: {data}")
                break
            except Exception as e:
                print(f"Publish failed: {e}, retrying...")
                await uasyncio.sleep(5)
        
    async def subscribe(self, topic, callback):
        self.client.set_callback(callback)
        while True:
            try:
                self.client.subscribe(topic)
                print(f"Subscribed to {topic}")
                break
            except Exception as e:
                print(f"Subscribe failed: {e}, retrying...")
                await uasyncio.sleep(5)
    
    async def wait_for_message(self):
        while True:
            try:
                self.client.check_msg()
            except Exception as e:
                print(f"Error checking messages: {e}")
            await uasyncio.sleep(1)
