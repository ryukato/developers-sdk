"""
This module implements APIs for link-developers.

Author: Yoonyoul Yoo
Date: 2021/01/09
"""

from uplink import Consumer, put, get, post, returns, Path, Query, json, Body
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
        """
        Request to update the information of the service token with the given contract ID.

        Body
        Parameter 	  Type 	     Description 	                                                                                                                     Required
        name 	      String 	 New name of the service token. At least 3 characters and up to 20 characters. Lowercase and uppercase alphabets and numbers only. 	 Optional
        meta 	      String 	 New metadata string. No fixed format up to 1,000 characters. 	                                                                     Optional
        ownerAddress  String 	 Address of the service wallet which is the contract owner. 	                                                                     Required
        ownerSecret   String 	 Secret of the service wallet which is the contract owner. 	                                                                         Required
        """
        pass

    @json
    @returns.json
    @post("/v1/service-tokens/{contract_id}/mint")
    def mint_service_token(self, contract_id: Path("contract_id"), service_token_mint_request: Body):
        """
        Request to mint the given service token and transfer it to the given wallet.

        Body
        Parameter 	     Type 	    Description 	                                                             Required
        toUserId 	     String 	User ID of the user to receive the minted service token. 	                 Optional
        toAddress 	     String 	Address of the wallet to receive the minted service token. 	                 Optional
        amount 	         String 	Amount to mint and transfer, larger than or equal to 1 and less than 2^255.  Required
        ownerAddress 	 String 	Address of the service wallet of the contract owner 	                     Required
        ownerSecret 	 String 	Secrete key of the service wallet of the contract owner 	                 Required
        """
        pass

    @json
    @returns.json
    @post("/v1/service-tokens/{contract_id}/burn")
    def burn_service_token(self, contract_id: Path("contract_id"), service_token_burn_request: Body):
        """
        Request to burn the service token in the owner wallet.

        This endpoint will NOT be available from April 1, 2021. Use Burn a service token in user wallet instead, which can burn a service token in the owner wallet.

        Body
        Parameter 	     Type 	    Description 	                                                             Required
        amount 	         String 	Amount to be burnt, larger than or equal to 1 and less than 2^255. 	         Required
        ownerAddress 	 String 	Address of the service wallet of the contract owner 	                     Required
        ownerSecret 	 String 	Secrete key of the service wallet of the contract owner 	                 Required
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

        Body
        Parameter 	     Type 	    Description 	                                                             Required
        fromUserId 	     String 	User ID corresponding to the wallet that holds the token to be burnt         Optional
        fromAddress	     String 	Address of the wallet with the token to be burnt	                         Optional
        amount 	         String 	Amount to be burnt, larger than or equal to 1 and less than 2^255. 	         Required
        ownerAddress 	 String 	Address of the service wallet of the contract owner 	                     Required
        ownerSecret 	 String 	Secrete key of the service wallet of the contract owner 	                 Required
        """
        pass

    @returns.json
    @get("/v1/service-tokens/{contract_id}/holders")
    def service_token_holders(self, contract_id: Path("contract_id"), limit: Query = 10, page: Query = 1, orderBy: Query = "desc"):
        """List all holders of the service token with the given contract ID <https://docs-blockchain.line.biz/glossary/?id=contract-id>."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}")
    def item_token(self, contract_id: Path("contract_id")):
        """Retrieve the contract information of the given item token."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/fungibles")
    def fungible_tokens(self, contract_id: Path("contract_id")):
        """List all fungible item tokens created with the given contract and retrieve the corresponding information."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/fungibles/{token_type}")
    def fungible_token(self, contract_id: Path("contract_id"), token_type: Path("token_type")):
        """Retrieve the information of the fungible item token with the given Contract ID and Token Type."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/fungibles/{token_type}/holders")
    def fungible_token_holders(self, contract_id: Path("contract_id"), token_type: Path("token_type")):
        """List up all users who hold the fungible item token with the given Contract ID and Token Type."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/non-fungibles")
    def non_fungible_tokens(self, contract_id: Path("contract_id")):
        """List all non-fungible item tokens created with the given contract and retrieve the corresponding information."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}")
    def non_fungible_token_type(self, contract_id: Path("contract_id"), token_type: Path("token_type")):
        """Retrieve the information of the non-fungible item token created with the given contract ID and Token Type."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}")
    def non_fungible_token(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index")
    ):
        """Retrieve the information of the non-fungible item token with the given Contract ID, Token Type and Token Index."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}")
    def non_fungible_token_type_holders(self, contract_id: Path("contract_id"), token_type: Path("token_type")):
        """Retrieve the holders of the non-fungible item token with the given Contract ID and Token Type."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}/holders")
    def non_fungible_token_holders(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index")
    ):
        """Retrieve the holder of the non-fungible item token with the given Contract ID, Token Type and Token Index."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}/children")
    def non_fungible_token_children(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index")
    ):
        """List up the children of the non-fungible item token with the given Contract ID, Token Type and Token Index."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}/parent")
    def non_fungible_token_parent(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index")
    ):
        """Retrieve the parent of the item token with the given Contract ID, Token Type and Token Index."""
        pass

    @returns.json
    @get("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}/root")
    def non_fungible_token_root(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index")
    ):
        """Retrieve the root of the given non-fungible item token."""
        pass

    @json
    @returns.json
    @put("/v1/service-tokens/{contract_id}")
    def update_fungible_token(self, contract_id: Path("contract_id"), update_request: Body):
        """
        Request to update the information of the service token with the given contract ID.

        Body
        Parameter 	     Type 	  Description 	                                                                Required
        name 	         String   Name of the given item token. At least 3 characters and up to 20 characters. 	Optional
        meta 	         String   Metadata string of the given item token 	                                    Optional
        ownerAddress     String   Address of the contract owner service wallet 	                                Required
        ownerSecret 	 String   Secret key of the contract owner service wallet 	                            Required
        """
        pass
