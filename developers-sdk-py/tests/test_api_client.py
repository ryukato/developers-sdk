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

        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_update_fungible_token(self):
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
            "meta": "Strong sword is strong"
        }

        response = api_client.update_fungible_token("a48f097b", "00000001", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_update_non_fungible_token_type(self):
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
            "meta": "New meta is comming"
        }

        response = api_client.update_non_fungible_token_type("a48f097b", "10000001", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_update_non_fungible_token(self):
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
            "name": "gtf6Ul2xNFKsSEwt",
            "meta": "Burning"
        }

        response = api_client.update_non_fungible_token("a48f097b", "10000001", "00000001", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_create_fungible_token(self):
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
            "name": "4W1Vj9U8tYf"
        }

        response = api_client.create_fungible_token("a48f097b", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_mint_fungible_token(self):
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
            "toAddress": "tlink1yfccn4kscn5kr7vadk8vgx385lfrymr8thwaqg",
            "amount": "5113980"
        }

        response = api_client.mint_fungible_token("a48f097b", "00000001", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_burn_fungible_token(self):
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
            "amount": "1234"
        }

        response = api_client.burn_fungible_token("a48f097b", "00000001", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_create_non_fungible_token(self):
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
            "name": "yVvznw2RICXtz11Lw",
            "meta": "235v234r01234"
        }

        response = api_client.create_non_fungible_token("a48f097b", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_mint_non_fungible_token(self):
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
            "toAddress": "tlink1wxxfe3etmaxv8hvrdxfwveewrcynynhlnm0jkn",
            "name": "Nnq8Eda",
            "meta": "5y4bh"
        }

        response = api_client.mint_non_fungible_token("a48f097b", "10000001", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_multi_mint_non_fungible_token(self):
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
            "toAddress": "tlink1tmek3n5ak85tsqcc0wkdh6clk9th6xwf073sxm",
            "mintList": [
                {
                    "tokenType": "10000001",
                    "name": "WGk",
                    "meta": "5y4bh"
                },
                {
                    "tokenType": "10000001",
                    "name": "aoU"
                }
            ]
        }

        response = api_client.multi_mint_non_fungible_token("a48f097b", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_burn_non_fungible_token(self):
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
            "fromAddress": "tlink1yg7r3nv45qy84jhh94sdrvlsrsrgrf20wa6vaz"
        }

        response = api_client.burn_non_fungible_token("a48f097b", "10000001", "00000001", request_body)

        print("response: " + str(response))
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_call_service_wallets(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)
        response = api_client.service_wallets()

        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_service_wallet_detail(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = "tlink12d9vmcgvgdc0c6wdc3ggdaz7q4n8zc0m6pxlza"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_detail(test_wallet_address)

        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_service_wallet_transactions(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = "tlink12d9vmcgvgdc0c6wdc3ggdaz7q4n8zc0m6pxlza"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_transactions(wallet_address=test_wallet_address, limit=10, page=1, order_by="desc")

        self.assertEqual(1000, response["statusCode"])
