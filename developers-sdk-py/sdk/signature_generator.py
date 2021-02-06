"""
This module implements generating request signature.

* API Key: 136db0ad-0fe1-456f-96a4-329be3f93036
* API Secret: 9256bf8a-2b86-42fe-b3e0-d3079d0141fe
* Timestamp: 1581850266351
* Nonce: Bp0IqgXE
* HTTP method: GET
* Request path: /v1/wallets/tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq/transactions
* Query string: page=2&msgType=coin/MsgSend

Then flatten body will be
"Bp0IqgXE1581850266351GET/v1/wallets/tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq/transactions?page=2&msgType=coin/MsgSend"

And signature is going to be
"fasfnqKVVClFam+Dov+YN+rUfOo/PMZfgKx8E36YBtPh7gB2C+YJv4Hxl0Ey3g8lGD0ErEGnD0gqAt85iEhklQ=="

Author: Yoonyoul Yoo
Date: 2021/01/09
"""
import hmac
import hashlib
import base64
import logging
import sys
from sdk.request_flattener import RequestBodyFlattener

logging.basicConfig(stream=sys.stdout)
simple_formatter = logging.Formatter("[%(name)s] %(message)s")


class SignatureGenerator:
    """This is to generate signature with flatten request."""

    __logger = logging.getLogger('ApiSignatureAuth')
    __logger.setLevel(logging.DEBUG)

    def __createSignTarget(self, method, path, timestamp, nonce, parameters: dict = {}):
        signTarget = f'{nonce}{str(timestamp)}{method}{path}'
        if(len(parameters) > 0):
            signTarget = signTarget + "?"

        return signTarget

    def generate(self, secret: str, method: str, path: str, timestamp: int, nonce: str, query_params: dict = {}, body: dict = {}):
        """
        Generate signature with given arguments.

        Args:
            -secret- api-secret
            -method- http method
            -path- api path
            -timestamp- Unix timestamp value
            -nonce- random stirng with 8 length
            -query_params- query paraemeters
            -body- request body

        Returns:
            -signauture- generated signature
        """
        body_flattener = RequestBodyFlattener()
        all_parameters = {}
        all_parameters.update(query_params)
        all_parameters.update(body)

        self.__logger.debug("query_params: " + str(query_params))

        signTarget = self.__createSignTarget(method.upper(), path, timestamp, nonce, all_parameters)

        if (len(query_params) > 0):
            signTarget += '&'.join('%s=%s' % (key, value) for (key, value) in query_params.items())

        if (len(body) > 0):
            if (len(query_params) > 0):
                signTarget += "&" + body_flattener.flatten(body)
            else:
                signTarget += body_flattener.flatten(body)

        self.__logger.debug("signTarget: " + str(signTarget))
        raw_hmac = hmac.new(bytes(secret, 'utf-8'), bytes(signTarget, 'utf-8'), hashlib.sha512)
        result = base64.b64encode(raw_hmac.digest()).decode('utf-8')

        return result
