import socketserver
import struct
import random
import threading
from sense_hat import SenseHat


class PiServer(socketserver.BaseRequestHandler):
    def handle(self):


        sense = SenseHat()
        sense.show_message("Sending")

        sense.clear()
        temp = sense.get_temperature()
        print(type(temp))
        print(temp)


        sense.clear()
        pressure = sense.get_pressure()
        print(type(pressure))
        print(pressure)

        sense.clear()
        humidity = sense.get_humidity()
        print(type(humidity))
        print(humidity)

        try:
            self.request.settimeout(10)
            MESSAGE = struct.pack('!fff', temp, pressure, humidity)
            self.request.sendto(MESSAGE, self.client_address)
            self.request.close()

        except Exception as ex:
            self.request.close()
            return

if __name__ == "__main__":
    HOST, PORT = '10.18.232.161', 12235
    sense = SenseHat()
    sense.show_message("Starting...")
    server = socketserver.ThreadingTCPServer((HOST, PORT), PiServer, bind_and_activate=True)
    server_thread = threading.Thread(target=server.serve_forever())
    server_thread.daemon = True
    server_thread.start()
