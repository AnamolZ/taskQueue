from Services.logger.logger import log_decorator

@log_decorator
def reverse(text):
    return text[::-1]
