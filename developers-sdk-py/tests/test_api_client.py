import unittest
from sdk.api_client import ApiClient


class TestFlattenBody(unittest.TestCase):
    def setUp(self):
        pass
    def tearDown(self):
        pass


    def test_create_instance(self):
        api_client = ApiClient(base_url="https://test.com")
        self.assertIsNotNone(api_client)
