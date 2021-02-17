import unittest
from sdk.api_client import ApiClient, ApiSignatureAuth
from sdk.signature_generator import SignatureGenerator
from dotenv import load_dotenv
from pathlib import Path
import os

TEST_SERVICE_TOKEN_CONTRACT_ID = "a48f097b"
TEST_WALLET_ADDRESS = "tlink12d9vmcgvgdc0c6wdc3ggdaz7q4n8zc0m6pxlza"
TEST_TOKEN_TYPE = "10000001"
TEST_TOKEN_INDEX = "00000001"
TEST_TX_HASH = "61AB8A054D47CA05E4ABE591B929282CBCD7DACD5A4C8259020C566F0EC186BE"
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
        response = api_client.service_token_detail(TEST_SERVICE_TOKEN_CONTRACT_ID)

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
        response = api_client.update_service_token_detail(TEST_SERVICE_TOKEN_CONTRACT_ID, request_body)
        # TODO fix this
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
        response = api_client.mint_service_token(TEST_SERVICE_TOKEN_CONTRACT_ID, request_body)
        # TODO fix this
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
        response = api_client.burn_service_token(TEST_SERVICE_TOKEN_CONTRACT_ID, request_body)
        # TODO fix this
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

        response = api_client.burn_from_service_token(TEST_SERVICE_TOKEN_CONTRACT_ID, request_body)
        # TODO fix this
        self.assertEqual(4041, response["statusCode"])

    def test_create_instance_and_service_token_holders(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_token_holders(TEST_SERVICE_TOKEN_CONTRACT_ID)

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

        response = api_client.update_fungible_token(TEST_SERVICE_TOKEN_CONTRACT_ID, TEST_TOKEN_INDEX, request_body)

        print("response: " + str(response))
        # TODO fix this
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

        response = api_client.update_non_fungible_token_type(TEST_SERVICE_TOKEN_CONTRACT_ID, TEST_TOKEN_TYPE, request_body)

        print("response: " + str(response))
        # TODO fix this
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

        response = api_client.update_non_fungible_token(TEST_SERVICE_TOKEN_CONTRACT_ID, TEST_TOKEN_TYPE, TEST_TOKEN_INDEX, request_body)

        print("response: " + str(response))
        # TODO fix this
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

        response = api_client.create_fungible_token(TEST_SERVICE_TOKEN_CONTRACT_ID, request_body)

        print("response: " + str(response))
        # TODO fix this
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

        response = api_client.mint_fungible_token(TEST_SERVICE_TOKEN_CONTRACT_ID, TEST_TOKEN_INDEX, request_body)

        print("response: " + str(response))
        # TODO fix this
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

        response = api_client.burn_fungible_token(TEST_SERVICE_TOKEN_CONTRACT_ID, TEST_TOKEN_INDEX, request_body)

        print("response: " + str(response))
        # TODO fix this
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

        response = api_client.create_non_fungible_token(TEST_SERVICE_TOKEN_CONTRACT_ID, request_body)

        print("response: " + str(response))
        # TODO fix this
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

        response = api_client.mint_non_fungible_token(TEST_SERVICE_TOKEN_CONTRACT_ID, TEST_TOKEN_TYPE, request_body)

        print("response: " + str(response))
        # TODO fix this
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
                    "tokenType": TEST_TOKEN_TYPE,
                    "name": "WGk",
                    "meta": "5y4bh"
                },
                {
                    "tokenType": TEST_TOKEN_TYPE,
                    "name": "aoU"
                }
            ]
        }

        response = api_client.multi_mint_non_fungible_token(TEST_SERVICE_TOKEN_CONTRACT_ID, request_body)

        print("response: " + str(response))
        # TODO fix this
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

        response = api_client.burn_non_fungible_token(TEST_SERVICE_TOKEN_CONTRACT_ID, TEST_TOKEN_TYPE, TEST_TOKEN_INDEX, request_body)

        print("response: " + str(response))
        # TODO fix this
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
        test_wallet_address = TEST_WALLET_ADDRESS
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
        test_wallet_address = TEST_WALLET_ADDRESS
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_transactions(wallet_address=test_wallet_address, limit=10, page=1, order_by="desc")

        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_service_wallet_base_coin(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_base_coin(test_wallet_address)

        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_service_wallet_service_tokens(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_service_tokens(wallet_address=test_wallet_address, limit=10, page=1, order_by="desc")
        print("response : " + str(response))
        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_service_wallet_service_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_service_token(test_wallet_address, TEST_SERVICE_TOKEN_CONTRACT_ID)
        print("response : " + str(response))
        self.assertEqual(1000, response["statusCode"])

    def test_create_instance_and_call_service_wallet_fungible_tokens(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_fungible_tokens(test_wallet_address, test_contract_id, limit=10, page=1, order_by="desc")
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4044, response["statusCode"])

    def test_create_instance_and_call_service_wallet_fungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_fungible_token(test_wallet_address, test_contract_id, TEST_TOKEN_TYPE)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4044, response["statusCode"])

    def test_create_instance_and_call_service_wallet_non_fungible_tokens(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_non_fungible_tokens(test_wallet_address, test_contract_id, limit=10, page=1, order_by="desc")
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4044, response["statusCode"])


    def test_create_instance_and_call_service_wallet_non_fungible_token_type(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_non_fungible_token_type(
            test_wallet_address,
            test_contract_id,
            TEST_TOKEN_TYPE,
            limit=10,
            page=1,
            order_by="desc"
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4044, response["statusCode"])

    def test_create_instance_and_call_service_wallet_non_fungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.service_wallet_non_fungible_token(
            test_wallet_address,
            test_contract_id,
            TEST_TOKEN_TYPE,
            TEST_TOKEN_INDEX
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4044, response["statusCode"])

    def test_create_instance_and_call_service_wallet_transfer_basecoin(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "walletSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "amount": "15"
        }

        response = api_client.service_wallet_transfer_basecoin(test_wallet_address,request_body)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4083, response["statusCode"])

    def test_create_instance_and_call_service_wallet_transfer_service_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "walletSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "amount": "15"
        }

        response = api_client.service_wallet_transfer_service_token(test_wallet_address, test_contract_id, request_body)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4083, response["statusCode"])

    def test_create_instance_and_call_service_wallet_transfer_fungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "walletSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "amount": "15"
        }

        response = api_client.service_wallet_transfer_fungible_token(
            test_wallet_address,
            test_contract_id,
            TEST_TOKEN_TYPE,
            request_body
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4083, response["statusCode"])

    def test_create_instance_and_call_service_wallet_transfer_nonfungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "walletSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "amount": "15"
        }

        response = api_client.service_wallet_transfer_nonfungible_token(
            test_wallet_address,
            test_contract_id,
            TEST_TOKEN_TYPE,
            TEST_TOKEN_INDEX,
            request_body
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4083, response["statusCode"])

    #
    def test_create_instance_and_call_service_wallet_batch_transfer_nonfungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_wallet_address = TEST_WALLET_ADDRESS
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "walletSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "transferList": [
                {
                    "tokenId": "1000000100000001"
                },
                {
                    "tokenId": "1000000100000002"
                }
            ]
        }

        response = api_client.service_wallet_batch_transfer_nonfungible_token(
            test_wallet_address,
            test_contract_id,
            request_body
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4083, response["statusCode"])

    def test_create_instance_and_call_user_detail(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_detail(test_user_id)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_transactions(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_transactions(user_id=test_user_id, limit=10, page=1, order_by="desc")
        print("response : " + str(response))

        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_base_coin(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        api_client = ApiClient(
          base_url=api_base_url,
          auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_base_coin(test_user_id)

        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_service_tokens(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        api_client = ApiClient(
          base_url=api_base_url,
          auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_service_tokens(user_id=test_user_id, limit=10, page=1, order_by="desc")
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_service_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_service_token(test_user_id, TEST_SERVICE_TOKEN_CONTRACT_ID)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_fungible_tokens(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
          base_url=api_base_url,
          auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_fungible_tokens(test_user_id, test_contract_id, limit=10, page=1, order_by="desc")
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_fungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
          base_url=api_base_url,
          auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_fungible_token(test_user_id, test_contract_id, TEST_TOKEN_TYPE)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_non_fungible_tokens(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID
        api_client = ApiClient(
          base_url=api_base_url,
          auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_non_fungible_tokens(test_user_id, test_contract_id, limit=10, page=1, order_by="desc")
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_non_fungible_token_type(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_non_fungible_token_type(
            test_user_id,
            test_contract_id,
            TEST_TOKEN_TYPE,
            limit=10,
            page=1,
            order_by="desc"
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_non_fungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_non_fungible_token(
            test_user_id,
            test_contract_id,
            TEST_TOKEN_TYPE,
            TEST_TOKEN_INDEX
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def test_create_instance_and_call_user_session_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_session_token = "test_user_session_token"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_session_token(test_user_session_token)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4048, response["statusCode"])

    def test_create_instance_and_call_user_service_token_proxy(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_contract_id = TEST_SERVICE_TOKEN_CONTRACT_ID # TODO fix this
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.user_service_token_proxy(test_user_id, test_contract_id)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4051, response["statusCode"])

    def call_issue_session_base_coin_tranfee(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_request_type = "aoa"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "amount": "15",
            "landingUri": "https://my.service.landing/home"
        }

        response = api_client.issue_session_base_coin_transfer(
            test_user_id,
            request_body,
            test_request_type
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4000, response["statusCode"])

    def call_issue_session_service_token_tranfer(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_request_type = "aoa"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "amount": "15",
            "landingUri": "https://my.service.landing/home"
        }

        response = api_client.issue_session_service_token_transfer(
            test_user_id,
            TEST_SERVICE_TOKEN_CONTRACT_ID,
            request_body,
            test_request_type
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4000, response["statusCode"])

    def call_request_service_token_proxy(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_request_type = "aoa"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "amount": "15",
            "landingUri": "https://my.service.landing/home"
        }

        response = api_client.request_service_token_proxy(
            test_user_id,
            TEST_SERVICE_TOKEN_CONTRACT_ID,
            request_body,
            test_request_type
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4000, response["statusCode"])

    def call_request_item_token_proxy(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"
        test_request_type = "aoa"
        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        request_body = {
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "amount": "15",
            "landingUri": "https://my.service.landing/home"
        }

        response = api_client.request_item_token_proxy(
            test_user_id,
            TEST_SERVICE_TOKEN_CONTRACT_ID,
            request_body,
            test_request_type
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4000, response["statusCode"])

    def call_commit_user_transaction(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")

        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.commit_user_transaction(
            "tes_request_session"
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4048, response["statusCode"])

    def call_transfer_user_service_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"

        request_body = {
            "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink15lz4v6kclnxmm04kdj639ewetykh4rpf9cqcz6",
            "amount": "999"
        }


        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.transfer_user_service_token(
            test_user_id,
            TEST_SERVICE_TOKEN_CONTRACT_ID,
            request_body
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4041, response["statusCode"])

    def call_transfer_user_fungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"

        request_body = {
            "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink15lz4v6kclnxmm04kdj639ewetykh4rpf9cqcz6",
            "amount": "999"
        }

        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.transfer_user_fungible_token(
            test_user_id,
            TEST_SERVICE_TOKEN_CONTRACT_ID,
            TEST_TOKEN_TYPE,
            request_body
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4041, response["statusCode"])

    def call_transfer_user_non_fungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"

        request_body = {
            "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink15lz4v6kclnxmm04kdj639ewetykh4rpf9cqcz6"
        }


        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.transfer_user_non_fungible_token(
            test_user_id,
            TEST_SERVICE_TOKEN_CONTRACT_ID,
            TEST_TOKEN_TYPE,
            TEST_TOKEN_INDEX,
            request_body
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4041, response["statusCode"])

    def call_batch_transfer_user_non_fungible_token(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"

        request_body = {
            "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "toAddress": "tlink1s658utvasn7f5q92034h6zgv0zh2uxy9tzmtqv",
            "transferList":     [
                {
                    "tokenId": "1000000100000001"
                },
                {
                    "tokenId": "1000000100000002"
                }
            ]
        }


        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.batch_transfer_user_non_fungible_token(
            test_user_id,
            TEST_SERVICE_TOKEN_CONTRACT_ID,
            request_body
        )
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4041, response["statusCode"])


    def call_transaction_result(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"

        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.transaction_result(TEST_TX_HASH)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4040, response["statusCode"])


    def call_post_memo(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"

        request_body = {
            "walletAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "walletSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
            "memo": "Show me the money"
        }

        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.post_memo(request_body)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4041, response["statusCode"])


    def call_get_memo(self):
        api_base_url = os.getenv("API_BASE_URL")
        service_api_key = os.getenv("SERVICE_API_KEY")
        service_api_secret = os.getenv("SERVICE_API_SECRET")
        test_user_id = "U556719f559479aab8b8f74c488bf6317"

        api_client = ApiClient(
            base_url=api_base_url,
            auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
        self.assertIsNotNone(api_client)

        response = api_client.get_memo(TEST_TX_HASH)
        print("response : " + str(response))
        # TODO fix this
        self.assertEqual(4040, response["statusCode"])
