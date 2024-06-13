import urequests
import uasyncio

class HttpRequester:
    
    async def make_get_request(self, url):
        try:
            print(f"Making GET request to: {url}")
            response = urequests.get(url)
            if response.status_code == 200:
                print(f"Received response: {response.text}")
                return response.text
            else:
                print(f"Failed to get data, status code: {response.status_code}")
                return None
        except Exception as e:
            print(f"Error making get request: {e}")
            return None
       
