
---

### File Structure:

```
notification_system/
│
├── app/
│   ├── main.py                  # FastAPI app with Celery task queue
│   ├── celery_worker.py          # Celery worker for background tasks
│   ├── notification_service.py   # Notification services (desktop, pushbullet)
│
├── client/
│   ├── trigger_notification.py   # Client to trigger notifications via API
│
├── assets/
│   └── notification_icon.ico     # Icon for desktop notification
│
├── .env                          # Environment variables
├── .gitignore                    # Git ignore file
├── requirements.txt              # Python dependencies
```
---

### Instructions to Run the System:

1. **Install dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

2. **Start Redis server:**
   Ensure Redis is running on your machine.

3. **Run Celery worker:**
   ```bash
   celery -A app.celery_worker.celery_app worker --loglevel=info --pool=solo
   ```

4. **Run FastAPI app:**
   ```bash
   uvicorn app.main:app --reload
   ```

5. **Trigger a notification (from `client/trigger_notification.py`):**
   ```bash
   python client/trigger_notification.py
   ```

---