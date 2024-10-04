import schedule
import time
import threading
from Services.task.push_notification import push_notification

def schedule_task():
    # Create a new thread to execute the task in the background
    task_thread = threading.Thread(target=push_notification, args=('Notification',"Hello Redis & Celery"))
    task_thread.start()
    print("Task initiated in background.")

# Schedule the push notification function to run every minute
schedule.every(1).minutes.do(schedule_task)

def run_scheduler():
    while True:
        schedule.run_pending()
        time.sleep(1)