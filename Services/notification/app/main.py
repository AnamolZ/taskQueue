# app/main.py
from fastapi import FastAPI
from pydantic import BaseModel
from app.celery_worker import celery_app
from app.notification_service import send_desktop_notification, send_pushbullet_notification
import os

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