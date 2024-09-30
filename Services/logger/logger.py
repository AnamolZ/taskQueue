import logging

# Configure logging
logging.basicConfig(
    filename='service.log',
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)

def log_decorator(func):
    def wrapper(*args, **kwargs):
        logging.info(f'Calling function {func.__name__} with arguments: {args} {kwargs}')
        
        try:
            result = func(*args, **kwargs)
            logging.info(f'Function {func.__name__} executed successfully.')
            return result
        except Exception as e:
            logging.error(f'Error in function {func.__name__}: {e}')
            raise

    return wrapper
