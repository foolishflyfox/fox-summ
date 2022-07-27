import threading
import time
class Demo:
    def __init__(self):
        self.cnt = 100
    def process(self):
        while True:
            self.cnt -= 1
            print("cnt = {}".format(self.cnt))
            time.sleep(1)
    def start(self):
        threading.Timer(0, self.process).start()

demo = Demo()
demo.start()
