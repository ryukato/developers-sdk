import unittest
from sdk.api_client import ApiClient


class TestFlattenBody(unittest.TestCase):
    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_create_instance(self):
        api_client = ApiClient(base_url="https://test-api.blockchain.line.me")
        self.assertIsNotNone(api_client)
        response = api_client.time()
        # TODO fix Service-api-key not found using mock server
        self.assertEqual(4012, response.json()["statusCode"])
