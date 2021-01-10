import unittest
from sdk.signature_generator import SignatureGenerator


class TestSignatureGenerator(unittest.TestCase):
    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_with_empty_body(self):
        method = "GET"
        path = "/v1/wallets"
        timestamp = 1581850266351
        secret = "9256bf8a-2b86-42fe-b3e0-d3079d0141fe"
        nonce = "Bp0IqgXE"
        expected_sigature = "2LtyRNI16y/5/RdoTB65sfLkO0OSJ4pCuz2+ar0npkRbk1/dqq1fbt1FZo7fueQl1umKWWlBGu/53KD2cptcCA=="
        actual_signature = SignatureGenerator().generate(secret, method, path, timestamp, nonce)
        self.assertEqual(expected_sigature, actual_signature)
