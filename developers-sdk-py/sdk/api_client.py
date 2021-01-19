"""
This module implements APIs for link-developers.

Author: Yoonyoul Yoo
Date: 2021/01/09
"""

from uplink import Consumer, put, get, post, returns, Path, json, Body
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

    __logger = logging.getLogger("ApiSignatureAuth")
    __logger.setLevel(logging.DEBUG)

    def __init__(self, api_key, api_secret, signature_generator):
        """Initialize with api_key, secret and signature_generator."""
        self.api_key = api_key
        self.api_secret = api_secret
        self.signature_generator = signature_generator

    def __log_request(self, request_builder):
        self.__logger.debug("request headers: " + str(request_builder.info["headers"]))
        self.__logger.debug("request params: " + str(request_builder.info["params"]))
        self.__logger.debug("request body: " + str(request_builder.info["data"]))

    def __nonce(self):
        return "".join(random.choice(string.ascii_uppercase + string.ascii_lowercase + string.digits) for _ in range(8))

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
        params = request_builder.info["params"]
        body = request_builder.info["data"]
        signature = self.signature_generator.generate(self.api_secret, method, path, timestamp, nonce, params, body)
        headers = self.__build_headers(nonce, timestamp, self.api_key, signature)
        request_builder.info["headers"].update(headers)
        self.__log_request(request_builder)


class ApiClient(Consumer):
    """A Python client for link-developers API."""

    @returns.json
    @get("/v1/time")
    def time(self):
        """Retreives the time value from server."""
        pass

    @returns.json
    @get("/v1/services/{service_id}")
    def service_detail(self, service_id: Path("service_id")):
        """Retreives detail information of a service."""
        pass

    @returns.json
    @get("/v1/service-tokens")
    def service_tokens(self):
        """List up all service tokens issued by the service with the corresponding information."""
        pass

    @returns.json
    @get("/v1/service-tokens/{contract_id}")
    def service_token_detail(self, contract_id: Path("contract_id")):
        """Retrieve the information of service tokens included in the given contract."""
        pass

    @json
    @returns.json
    @put("/v1/service-tokens/{contract_id}")
    def update_service_token_detail(self, contract_id: Path("contract_id"), update_request: Body):
        """Request to update the information of the service token with the given contract ID."""
        pass

    @json
    @returns.json
    @post("/v1/service-tokens/{contract_id}/mint")
    def mint_service_token(self, contract_id: Path("contract_id"), service_token_mint_request: Body):
        """Request to mint the given service token and transfer it to the given wallet."""
        pass

    @json
    @returns.json
    @post("/v1/service-tokens/{contract_id}/burn")
    def burn_service_token(self, contract_id: Path("contract_id"), service_token_burn_request: Body):
        """
        Request to burn the service token in the owner wallet.

        This endpoint will NOT be available from April 1, 2021. Use Burn a service token in user wallet instead, which can burn a service token in the owner wallet.
        """
        pass

    # /v1/service-tokens/{contractId}/burn-from
    @json
    @returns.json
    @post("/v1/service-tokens/{contract_id}/burn")
    def burn_from_service_token(self, contract_id: Path("contract_id"), service_token_burn_from_request: Body):
        """
        Request to burn the service token in the user wallet.

        When you’re burning a service token from the user wallet, \
        you should use the address and secret of the contract owner wallet for the given service token, instead of the user wallet.
        Wallet owner must approve setting the proxy <https://docs-blockchain.line.biz/glossary/?id=proxy> and complete authentication in advance.\

        Refer to Issue a session token for service token proxy setting <https://docs-blockchain.line.biz/api-guide/category-users?id=v1-users-userid-service-tokens-contractid-request-proxy-post> endpoint for setting the proxy for the service token.
        """
        pass

    @returns.json
    @get("/v1/service-tokens/{contract_id}/holders")
    def service_token_holders(self, contract_id: Path("contract_id")):
        """List all holders of the service token with the given contract ID <https://docs-blockchain.line.biz/glossary/?id=contract-id>."""
        pass
