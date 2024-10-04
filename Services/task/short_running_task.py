# Services/task/short_running_task.py

import time
from Services.logger.logger import log_decorator
from Services.notification.notification_service import send_notification

@log_decorator
def short_running_task():
    print("Starting short-running process...")
    for counting in range(1, 6):
        print(f"Short {counting}")
        time.sleep(1)
    print("Processing completed for Short Running Task.")
    send_notification(title="Notification", message="Hello Redis & Celery From Short Running Process")