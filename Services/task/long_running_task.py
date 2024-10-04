# Services/task/long_running_task.py

import time
from Services.logger.logger import log_decorator
from Services.notification.notification_service import send_notification

@log_decorator
def long_running_task():
    print("Starting long-running process...")
    for counting in range(1, 11):
        print(f"Long {counting}")
        time.sleep(2)
    print("Processing completed for Long Running Task.")
    send_notification(title="Notification", message="Hello Redis & Celery From Long Running Process")