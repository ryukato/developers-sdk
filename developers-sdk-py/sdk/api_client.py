"""
This module implements APIs for link-developers.

Author: Yoonyoul Yoo
Date: 2021/01/09
"""

from uplink import Consumer, get, returns
from uplink.auth import ApiTokenHeader
import logging
import sys
import random
import string
import time

# Fixme: config a sort of global loggerFactory
logging.basicConfig(stream=sys.stdout)
simple_formatter = logging.Formatter("[%(name)s] %(message)s")

SERVICE_API_KEY_HEADER = "service-api-key"
SIGNATURE_HEADER = "Signature"
TIMESTAMP_HEADER = "Timestamp"
NONCE_HEADER = "Nonce"


class ApiSignatureAuth(ApiTokenHeader):
    """Developers api authentication Handler."""

    __logger = logging.getLogger('ApiSignatureAuth')
    __logger.setLevel(logging.DEBUG)

    def __init__(self, api_key, api_secret, signature_generator):
        """Initialize with api_key, secret and signature_generator."""
        self.api_key = api_key
        self.api_secret = api_secret
        self.signature_generator = signature_generator

    def __log_request(self, request_builder):
        self.__logger.debug("request headers: " + str(request_builder.info['headers']))
        self.__logger.debug("request params: " + str(request_builder.info['params']))
        self.__logger.debug("request body: " + str(request_builder.info['data']))

    def __nonce(self):
        return ''.join(random.choice(string.ascii_uppercase + string.ascii_lowercase + string.digits) for _ in range(8))

    def __timestamp(self):
        return str(int(round(time.time() * 1000)))

    def __build_headers(self, nonce, timestamp, api_key, signature):
        return {NONCE_HEADER: nonce, TIMESTAMP_HEADER: timestamp, SERVICE_API_KEY_HEADER: api_key, SIGNATURE_HEADER: signature}

    def __call__(self, request_builder):
        """Append headers for authentication of developers api."""
        method = request_builder.method
        path = request_builder.relative_url
        timestamp = self.__timestamp()
        nonce = self.__nonce()
        params = request_builder.info['params']
        body = request_builder.info["data"]
        signature = self.signature_generator.generate(self.api_secret, method, path, timestamp, nonce, params, body)
        headers = self.__build_headers(nonce, timestamp, self.api_key, signature)
        request_builder.info['headers'].update(headers)
        self.__log_request(request_builder)


class ApiClient(Consumer):
    """A Python client for link-developers API."""

    @returns.json
    @get("/v1/time")
    def time(self):
        """Retreives the time value from server."""
