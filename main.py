from fastapi import FastAPI
from Services.api.api_client import fetch_data
from Services.scheduler.scheduler import run_scheduler

app = FastAPI()

@app.on_event("startup")
async def startup_event():
    fetch_data()
    run_scheduler()