from fastapi import FastAPI
from Services.api.api_client import fetch_data
from Services.scheduler.scheduler import run_scheduler
import threading

app = FastAPI()

@app.on_event("startup")
async def startup_event():
    fetch_data()
    threading.Thread(target=run_scheduler, daemon=True).start()
