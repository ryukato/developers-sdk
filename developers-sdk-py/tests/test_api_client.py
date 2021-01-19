import unittest
from sdk.api_client import ApiClient, ApiSignatureAuth
from sdk.signature_generator import SignatureGenerator
from dotenv import load_dotenv
from pathlib import Path
import os


class TestApiClient(unittest.TestCase):
    def setUp(self):
        env_path = Path("tests/.env")
        load_dotenv(dotenv_path=env_path, verbose=True)
        pass

    def tearDown(self):
        pass

    def test_create_instance_and_call_time_api(self):
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

    def test_create_instance_and_call_service_detail_api(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)
        response = api_client.service_detail("4b3f17ea-b667-4a31-a173-f10edc2c02ee")

        print("response: " + str(response))
        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_service_tokens(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)
        response = api_client.service_tokens()

        print("response: " + str(response))
        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_service_token_detail(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)
        response = api_client.service_token_detail("a48f097b")

        print("response: " + str(response))
        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_update_service_token_detail(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "name": "dTudb9Hq5i2ieHyJFo6o",
            "meta": "bdfssdfasd"
        }
        response = api_client.update_service_token_detail("a48f097b", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_mint_service_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "amount": "1249051"
        }
        response = api_client.mint_service_token("a48f097b", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_burn_service_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "amount": "31"
        }
        response = api_client.burn_service_token("a48f097b", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_burn_from_service_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "fromAddress": "tlink15lz4v6kclnxmm04kdj639ewetykh4rpf9cqcz6",
            "amount": "31"
        }

        response = api_client.burn_from_service_token("a48f097b", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_service_token_holders(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_token_holders("a48f097b")

        print("response: " + str(response))
        self.assertEqual(1000, response["statusCode"])
