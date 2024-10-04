# Services/task_executor/execute_task.py

from Services.task.long_running_task import long_running_task
from Services.task.short_running_task import short_running_task

def execute_task():
    long_running_task()
    short_running_task()