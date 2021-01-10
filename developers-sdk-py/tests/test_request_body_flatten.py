import unittest
from sdk.request_signature import RequestBodyFlattener
from sdk.api_client import ApiClient


class TestFlattenBody(unittest.TestCase):
    def setUp(self):
        pass
    def tearDown(self):
        pass

    def test_with_empty_body(self):
        assert RequestBodyFlattener().flatten({}) == ""

    def test_with_multi_mint_body(self):
        req_params = {
            'ownerAddress': 'tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq',
            'ownerSecret': 'uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=',
            'toAddress': 'tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp',
            'mintList': [
                {
                    'tokenType': '10000001',
                    'name': 'NewNFT'
                },
                {
                    'tokenType': '10000003',
                    'name': 'NewNFT2',
                    'meta': 'New nft 2 meta information'
                }
            ]
        }

        expected = "mintList.meta=,New nft 2 meta information&mintList.name=NewNFT,NewNFT2&mintList.tokenType=10000001,10000003&ownerAddress=tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq&ownerSecret=uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=&toAddress=tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp"
        self.assertEqual(expected,  RequestBodyFlattener().flatten(req_params))
