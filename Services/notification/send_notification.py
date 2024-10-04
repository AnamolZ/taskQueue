# Services/notification/send_notification.py

import requests
import json

def send_notification(title: str, message: str):
    url = "http://127.0.0.1:8000/enqueue_task"
    headers = {"Content-Type": "application/json"}
    payload = {
        "title": title,
        "message": message
    }
    try:
        response = requests.post(url, headers=headers, data=json.dumps(payload))
        if response.status_code == 200:
            result = response.json()
            print(f"Notification triggered successfully: {result}")
        else:
            print(f"Failed to trigger notification: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"Error occurred: {e}")