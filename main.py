from Services.scheduler.scheduler import run_scheduler
from Services.api.api_client import fetch_data

if __name__ == "__main__":
    fetch_data()
    run_scheduler()