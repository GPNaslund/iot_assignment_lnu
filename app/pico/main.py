from app import App
from wifi_manager import WifiManager
from led_manager import LedManager
from mqtt_manager import MqttManager
from http_requester import HttpRequester
from dht22_manager import Dht22Manager
import uasyncio

async def main():
    wifiManager = WifiManager()
    ledManager = LedManager()
    mqttManager = MqttManager()
    httpRequester = HttpRequester()
    dhtManager = Dht22Manager(22)

    app = App(wifiManager, ledManager, mqttManager, httpRequester, dhtManager)

    await app.initialize()
    await app.start()

uasyncio.run(main())
