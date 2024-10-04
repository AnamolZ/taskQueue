# Services/notification/notification_service.py

import os
from fastapi import FastAPI
from pydantic import BaseModel
from celery import Celery
from plyer import notification
from pushbullet import Pushbullet
from dotenv import load_dotenv

load_dotenv()

celery_app = Celery('tasks', broker='redis://localhost:6379/0')
PUSHBULLET_TOKEN = os.getenv('PUSHBULLET_TOKEN')
app = FastAPI()

class NotificationTask(BaseModel):
    title: str
    message: str

@app.post("/enqueue_task")
def enqueue_task(task: NotificationTask):
    celery_task = process_task.delay(task.title, task.message)
    return {"task_id": celery_task.id, "status": "Task added to queue."}

@celery_app.task
def process_task(title: str, message: str):
    icon_path = os.path.join(os.getcwd(), 'assets', 'notification_icon.ico')
    send_desktop_notification(title, message, icon_path)
    send_pushbullet_notification(title, message)
    return "Notification sent"

def send_desktop_notification(title: str, message: str):
    icon_path = os.path.join(os.getcwd(), 'assets', 'notification_icon.ico')
    notification.notify(
        title=title,
        message=message,
        app_name='Task Notifier',
        app_icon=icon_path if os.path.exists(icon_path) else None,
        timeout=10
    )

def send_pushbullet_notification(title: str, message: str):
    pushbullet_client = Pushbullet(PUSHBULLET_TOKEN)
    pushbullet_client.push_note(title, message)

def send_notification(title: str, message: str):
    send_desktop_notification(title, message)
    send_pushbullet_notification(title, message)