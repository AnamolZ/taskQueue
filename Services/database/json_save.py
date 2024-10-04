# Services/database/json_save.py 

import json
from Services.logger.logger import log_decorator

@log_decorator
def save_to_json(data, filename='response.json'):
    if data is None:
        print("No data to save.")
        return
    with open(filename, 'w', encoding='utf-8') as file_object:
        json.dump(data, file_object, ensure_ascii=False, indent=4)
    print(f"Data saved to {filename}")