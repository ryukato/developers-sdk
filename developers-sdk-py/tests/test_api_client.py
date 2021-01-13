import unittest
from sdk.api_client import ApiClient, ApiSignatureAuth
from sdk.signature_generator import SignatureGenerator
from dotenv import load_dotenv
from pathlib import Path
import os


class TestApiClient(unittest.TestCase):
    def setUp(self):
        env_path = Path('tests/.env')
        load_dotenv(dotenv_path=env_path, verbose=True)
        pass

    def tearDown(self):
        pass

    def test_create_instance(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)
        response = api_client.time()

        print("response: " + str(response))
        self.assertEqual(1000, response["statusCode"])
