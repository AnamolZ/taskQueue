# Services/api/api_client.py

import requests
from Services.logger.logger import log_decorator
from Services.database.json_save import save_to_json

API_URL = 'https://jsonplaceholder.typicode.com/posts'

@log_decorator
def fetch_data():
    try:
        response = requests.get(API_URL)
        response.raise_for_status()
        data = response.json()
        print("Data fetched from API.")
        
        # save_to_json(data)
        return data

    except requests.exceptions.RequestException as e:
        print(f"API request failed: {e}")
        return None