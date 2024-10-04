# client/trigger_notification.py
import requests
import json

def trigger_notification(title: str, message: str):
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

if __name__ == "__main__":
    task_title = "Task Queue Notification"
    task_message = "Queued Task Has Been Finished"
    
    trigger_notification(task_title, task_message)
