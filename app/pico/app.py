import uasyncio
import config
import ujson

class App:
    def __init__(self, wifiManager, ledManager, mqttManager, httpRequester, dhtManager):
        self._wifiManager = wifiManager
        self._ledManager = ledManager
        self._mqttManager = mqttManager
        self._httpRequester = httpRequester
        self._dhtManager = dhtManager
        self._isInitialized = False
        self._mqttClient = None
    
    def _mqtt_callback(self, topic, msg):
        topic = topic.decode('utf-8') 
        message = msg.decode('utf-8')

        if topic == config.LED_TOPIC:
            self._ledManager.react_to_string(message)
    
    async def initialize(self):
        self._ledManager.start_blinking()
        await self._wifiManager.connect()
        self._mqttClient = await self._mqttManager.connect()
        await self._mqttManager.subscribe(config.LED_TOPIC, self._mqtt_callback)
        
        initial_led_response = await self._httpRequester.make_get_request(config.LED_STATE_URL)
        if initial_led_response:
            try:
                initial_led_state = ujson.loads(initial_led_response).get("state")
                self._ledManager.stop_blinking()
                self._ledManager.react_to_string(initial_led_state)
            except ValueError as e:
                print("Failed to decode JSON repsonse:", e)
                self._ledManager.stop_blinking()
        else:
            print("Failed to retrieve initial LED state")
            self._ledManager.stop_blinking()
        
        self._isInitialized = True
    
    async def _listen_for_led_messages(self):
        while True:
            self._mqttClient.check_msg()
            await uasyncio.sleep(2)
    
    async def _publish_sensor_reading(self):
        while True:
            dht_data = await self._dhtManager.get_data()
            if dht_data is not None:
                payload = ujson.dumps(dht_data)
                await self._mqttManager.publish(config.SENSOR_TOPIC, payload)
                await uasyncio.sleep(50)
    
    async def start(self):
        if not self._isInitialized:
            print("App is not initialized! Run app.initalize() before starting")
            return None
        
        uasyncio.create_task(self._listen_for_led_messages())
        uasyncio.create_task(self._publish_sensor_reading())

        while True:
            await uasyncio.sleep(1)


    
           
