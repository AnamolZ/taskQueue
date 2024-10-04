# Celery Redis Queue

## Overview

This project is designed to handle long-running tasks and send notifications to users through different channels (desktop, android). The system enables task execution in the background using a **distributed task queue**, ensuring tasks are processed efficiently and asynchronously.

### Why This Project Was Built

Modern applications often need to perform background operations such as sending notifications, processing large datasets, or handling user-generated tasks. These tasks can be resource-intensive and time-consuming, impacting the application's overall performance if executed in the main thread. This project was built to:

- Offload long-running tasks to background workers.
- Send notifications through multiple channels asynchronously.
- Improve scalability and performance for high-demand applications.
- Demonstrate advanced concepts like **distributed task queues** and **message brokering**.

### How It Works

1. **Task Queueing with Celery**:
   - When a user triggers a task (e.g., via an API request), the task is added to a **Redis queue**.
   - Celery workers continuously monitor this queue and process tasks in the background without blocking the main application.
   
2. **Notification System**:
   - The project allows sending notifications to users through different channels like desktop notifications and Pushbullet.
   - Each task can trigger a notification after execution, allowing users to receive updates on the task's completion.
   
3. **Task Scheduling**:
   - The system can schedule tasks at specific intervals (e.g., every minute) using the **Schedule** library. This enables automation and periodic task execution.
   
4. **Logging and Monitoring**:
   - The system uses Python’s built-in `logging` module to log every function call and execution, ensuring transparency and easy debugging.
   - Every task is logged with detailed information about success or failure, helping track and troubleshoot issues.

### Project Structure

```
Celery Redis Queue
│   main.py
│   README.md
│   .env
|   .gitignore
│   requirements.txt
└───Services
    ├───api
    │   └───api_client.py
    ├───config
    │   └───celery_config.py
    ├───database
    │   └───json_save.py
    ├───logger
    │   └───logger.py
    ├───notification
    │   └───notification_service.py
    │   └───send_notification.py
    ├───scheduler
    │   └───scheduler.py
    ├───task
    │   ├───long_running_task.py
    │   └───short_running_task.py
    └───task_executor
        └───execute_task.py
```

### Advanced Concepts Used

1. **Asynchronous Task Queue with Celery**:
   - Celery allows the system to handle multiple tasks concurrently, distributing work across workers and improving the system's efficiency. Tasks are processed in the background, enabling the main application to remain responsive.
   
2. **Redis as a Message Broker**:
   - Redis is used as the message broker for Celery. It queues tasks, ensuring they are handled properly even under heavy load or if the system experiences temporary outages.
   
3. **Task Scheduling**:
   - The system uses **Schedule** to automate task execution at fixed intervals (e.g., running tasks every minute). This is useful for periodic maintenance tasks or sending recurring notifications.
   
4. **Multi-channel Notifications**:
   - The notification system is built to support multiple channels (desktop, Pushbullet). This flexibility allows the system to adapt to different user preferences and use cases.

5. **Logging with Decorators**:
   - A logging decorator is applied to functions to automatically log when they are called, their success, or any errors. This improves debugging and monitoring without cluttering the business logic.
   
6. **Environment Variable Management**:
   - The **dotenv** package is used to securely manage API tokens and other sensitive information, ensuring they are not hard-coded into the application.

### Setup and Installation

#### Prerequisites
- **Python 3.9+**
- **Redis** installed and running locally
- **Pushbullet API key** (if Pushbullet notifications are required)

#### Installation Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/AnamolZ/celery_redis_queue.git
   cd celery_redis_queue
   ```

2. **Create and activate a virtual environment**:
   ```bash
   python -m venv venv
   venv\Scripts\activate
   ```

3. **Install dependencies**:
   ```bash
   pip install -r requirements.txt
   ```

4. **Set up environment variables**:
   Create a `.env` file in the root directory:
   ```bash
   PUSHBULLET_TOKEN=<Your_Pushbullet_Token>
   ```

5. **Start Redis** (if not already running):
   ```bash
   redis-server
   ```

6. **Run Celery worker**:
   In a new terminal, navigate to the project directory and run:
   ```bash
   celery -A Services.config.celery_config.app worker --loglevel=info
   ```

7. **Start the FastAPI server**:
   ```bash
   uvicorn main:app --host 127.0.0.1 --port 8000 --reload
   ```

8. **Schedule tasks**:
   In another terminal, run the task scheduler:
   ```bash
   python main.py
   ```

### Contribution

If you have ideas for new features, improvements, or advanced system integrations, feel free to contribute. Whether it's enhancing task management, adding new notification channels, or optimizing performance, your expertise is valued.
