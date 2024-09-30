import schedule
import time
import threading
from Services.task.tasks import reverse

def schedule_task():
    # Create a new thread to execute the task in the background
    task_thread = threading.Thread(target=reverse, args=("Hello Redis & Celery",))
    task_thread.start()
    print("Task initiated in background.")

# Schedule the reverse function to run every minute
schedule.every(1).minutes.do(schedule_task)

def run_scheduler():
    while True:
        schedule.run_pending()
        time.sleep(1)
