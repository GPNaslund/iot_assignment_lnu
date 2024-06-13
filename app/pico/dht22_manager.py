from machine import Pin
from time import sleep
import dht
import uasyncio

# USED PIN WAS 22


class Dht22Manager:
    def __init__(self, pin_number):
        self._sensor = dht.DHT22(Pin(pin_number))
    

    async def get_data(self):
      try:
        await uasyncio.sleep(2)
        self._sensor.measure()
        temp = self._sensor.temperature()
        hum = self._sensor.humidity()
        return {
          "temperature": temp,
          "humidity": hum,
        }
      except OSError as e:
        print("Failed to read sensor.")
        return None

