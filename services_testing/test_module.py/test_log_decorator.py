# services_testing/test_log_decorator.py

import unittest
from unittest.mock import patch
import logging

from Services.logger import log_decorator

class TestLogDecorator(unittest.TestCase):

    @patch('services.logger.logging.info')
    def test_log_decorator_success(self, mock_logging_info):
        @log_decorator
        def testing_function_01(x, y):
            return x + y

        result = testing_function_01(5, 7)

        self.assertEqual(result, 12)
        self.assertTrue(mock_logging_info.called)
        self.assertIn('Calling function testing_function_01 with arguments: (5, 7) {}', [call[0][0] for call in mock_logging_info.call_args_list])
        self.assertIn('Function testing_function_01 executed successfully.', [call[0][0] for call in mock_logging_info.call_args_list])

    @patch('services.logger.logging.info')
    @patch('services.logger.logging.error')
    def test_log_decorator_exception(self, mock_logging_error, mock_logging_info):
        @log_decorator
        def testing_function_01(x, y):
            return x / y

        with self.assertRaises(ZeroDivisionError):
            testing_function_01(5, 0)

        self.assertTrue(mock_logging_info.called)
        self.assertTrue(mock_logging_error.called)
        self.assertIn('Error in function testing_function_01:', [call[0][0] for call in mock_logging_error.call_args_list][0])

if __name__ == '__main__':
    unittest.main()

# python -m unittest discover