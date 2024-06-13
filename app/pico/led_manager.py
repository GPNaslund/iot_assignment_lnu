from machine import Pin
import uasyncio

class LedManager:
    def __init__(self):
        self._led = Pin("LED", Pin.OUT)
        self._blinking = False
        self._blink_task = None
    
    def turn_on(self):
        self._led.value(1)
    
    def turn_off(self):
        self._led.value(0)

    def react_to_string(self, message):
        print(f"LedManager should react to: {message}")
        if message is None:
            print("LED command was None")
            self.turn_off()
        elif message.upper() == "ON":
            self.turn_on()
        elif message.upper() == "OFF":
            self.turn_off()
        else:
            print("Unkown LED command recieved")
            self.turn_off()
    
    async def _blink(self):
        while self._blinking:
            self._led.value(not self._led.value())
            await uasyncio.sleep(0.5)

    def start_blinking(self):
        if not self._blinking:
            self._blinking = True
            self._blink_task = uasyncio.create_task(self._blink())
    
    def stop_blinking(self):
        if self._blinking:
            self._blinking = False
            if self._blink_task:
                self._blink_task.cancel()
                self._blink_task = None
            self.turn_off()
