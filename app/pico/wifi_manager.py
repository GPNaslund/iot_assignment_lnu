import network
import config
import uasyncio

class WifiManager:
    def __init__(self):
        self.wlan = network.WLAN(network.STA_IF)
        self.wlan.active(True)

    async def connect(self):
        self.wlan.connect(config.WIFI_SSID, config.WIFI_PASSWORD)
        print(self.wlan.isconnected())
        
        wait = 10
        while wait > 0:
            if self.wlan.status() < 0 or self.wlan.status() >= 3:
                break
            wait -= 1
            print('waiting for connection...')
            await uasyncio.sleep(1)
            
        if self.wlan.status() != 3:
            raise RuntimeError('wifi connection failed')
        else:
            print('connected')
            ip = self.wlan.ifconfig()[0]
            print('network config: ', ip)
            return ip
