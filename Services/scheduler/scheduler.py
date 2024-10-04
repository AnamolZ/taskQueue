# Services/scheduler/scheduler.py

import schedule
import time
import threading
from Services.task_executor.execute_task import execute_task

def schedule_task():
    task_thread = threading.Thread(target=execute_task)
    task_thread.start()
    print("Queued tasked initiated in background.")

schedule.every(1).minutes.do(schedule_task)

def run_scheduler():
    while True:
        schedule.run_pending()
        time.sleep(1)