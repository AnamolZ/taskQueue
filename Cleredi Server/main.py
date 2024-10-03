from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

counter = 0

class NotificationRequest(BaseModel):
    title: str
    message: str

@app.get("/get-notification")
async def get_notification():
    global counter
    notification = {
        "title": "Notification",
        "message": f"Celery Redis Queue Task Completed + {counter}"
    }
    counter += 1
    return notification
