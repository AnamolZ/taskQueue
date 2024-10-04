# app/notification_service.py
import os
from plyer import notification
from pushbullet import Pushbullet
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

PUSHBULLET_TOKEN = os.getenv('PUSHBULLET_TOKEN')

def send_desktop_notification(title: str, message: str, icon_path: str = None):
    notification.notify(
        title=title,
        message=message,
        app_name='Task Notifier',
        app_icon=icon_path if icon_path else None,
        timeout=10
    )

def send_pushbullet_notification(title: str, message: str):
    pushbullet_client = Pushbullet(PUSHBULLET_TOKEN)
    pushbullet_client.push_note(title, message)