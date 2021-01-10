"""
This module implements APIs for link-developers.

Author: Yoonyoul Yoo
Date: 2021/01/09
"""

from uplink import Consumer, get, json, decorators
# from uplink import params
from uplink.clients.io import RequestTemplate, transitions
from sdk.signature_generator import SignatureGenerator
import logging
import sys
import random
import string
import time

logging.basicConfig(stream=sys.stdout)
simple_formatter = logging.Formatter("[%(name)s] %(message)s")

__all__ = ["append_header"]


class CustomRequestTemplate(RequestTemplate):
    """A Python client for link-developers API."""

    __logger = logging.getLogger('CustomRequestTemplate')
    __logger.setLevel(logging.INFO)

    def before_request(self, request):
        """Handle request beore it is being sent."""
        self.__logger.info("request" + str(request))
        return transitions.send(request)


class append_header(decorators.MethodAnnotation):
    """A decorator that adds retry support to a consumer method or to an entire consumer."""

    __logger = logging.getLogger('CustomRequestTemplate')
    __logger.setLevel(logging.INFO)

    __signature_generator = SignatureGenerator()

    def __log_request(self, request_builder):
        self.__logger.info("request method: " + str(request_builder.method))
        self.__logger.info("request path: " + str(request_builder.relative_url))
        self.__logger.info("request headers: " + str(request_builder.info['headers']))
        self.__logger.info("request params: " + str(request_builder.info['params']))
        self.__logger.info("request body: " + str(request_builder.info["data"]))

    def __nonce(self):
        return ''.join(random.choice(string.ascii_uppercase + string.ascii_lowercase + string.digits) for _ in range(8))

    def __timestamp(self):
        return str(int(round(time.time() * 1000)))

    def modify_request(self, request_builder):
        """Modify request before sending it."""
        method = request_builder.method
        path = request_builder.relative_url
        timestamp = self.__timestamp()
        nonce = self.__nonce()
        params = request_builder.info['params']
        body = request_builder.info["data"]
        # TODO: load secret and api-key from configuration
        signature = self.__signature_generator.generate("test", method, path, timestamp, nonce, params, body)
        headers = {"nonce": "test", "service-api-key": "test", "Signature": signature}
        request_builder.info['headers'].update(headers)
        request_builder.add_request_template(
            self._create_template(request_builder)
        )

    def _create_template(self, request_builder):
        return CustomRequestTemplate()


class ApiClient(Consumer):
    """A Python client for link-developers API."""

    @append_header()
    @get("/v1/time")
    def time(self):
        """Retreives the time value from server."""
