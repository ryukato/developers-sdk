"""
This module implements APIs for line-blockchain-developers.

Author: Yoonyoul Yoo
Date: 2021/01/09
"""

from uplink import Consumer, put, get, post, returns, Path, Query, json, Body
from uplink.auth import ApiTokenHeader
import logging
import os
import sys
import random
import string
import time


SERVICE_API_KEY_HEADER = "service-api-key"
SIGNATURE_HEADER = "Signature"
TIMESTAMP_HEADER = "Timestamp"
NONCE_HEADER = "Nonce"


class ApiSignatureAuth(ApiTokenHeader):
    """Developers api authentication Handler."""

    __logger = logging.getLogger(__name__)

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

    def __user_agent(self, headers ={}):
        headers['user-agent'] = "developers-sdk-py"

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
        self.__user_agent(headers)
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
    def service_token_holders(self, contract_id: Path("contract_id"), limit: Query = 10, page: Query = 1, order_by: Query = "desc"):
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
    @put("/v1/item-tokens/{contract_id}/fungibles/{token_type}")
    def update_fungible_token(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        update_request: Body
    ):
        """
        Request to update the information of the fungible item token with the given Contract ID and Token Type.

        Body
        Parameter 	     Type 	  Description 	                                                                Required
        name 	         String   Name of the given item token. At least 3 characters and up to 20 characters. 	Optional
        meta 	         String   Metadata string of the given item token 	                                    Optional
        ownerAddress     String   Address of the contract owner service wallet 	                                Required
        ownerSecret 	 String   Secret key of the contract owner service wallet 	                            Required
        """
        pass

    @json
    @returns.json
    @put("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}")
    def update_non_fungible_token_type(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        update_request: Body
    ):
        """
        Request to update the Token Type of the non-fungible item token with the given Contract ID and Token Type.

        Body
        Parameter 	      Type 	     Description 	                                                                Required
        name 	          String 	 Name of the given token type. At least 3 characters and up to 20 characters. 	Optional
        meta 	          String 	 Metadata string of the given token type 	                                    Optional
        ownerAddress 	  String 	 Address of the contract owner service wallet 	                                Required
        ownerSecret 	  String 	 Secret key of the contract owner service wallet 	                            Required
        """
        pass

    @json
    @returns.json
    @put("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}")
    def update_non_fungible_token(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index"),
        update_request: Body
    ):
        """
        Request to update the information of the fungible item token with the given Contract ID, Token Type and Token Index.

        Body
        Parameter 	      Type 	  Description 	                                                                      Required
        name 	          String  Name of the given item token type. At least 3 characters and up to 20 characters.   Optional
        meta 	          String  Metadata string of the given item token. No fixed format up to 1,000 characters. 	  Optional
        ownerAddress 	  String  Address of the contract owner service wallet 	                                      Required
        ownerSecret 	  String  Secret key of the contract owner service wallet                                     Required
        """
        pass

    @json
    @returns.json
    @put("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}/parent")
    def attach_non_fungible_token(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index"),
        attach_request: Body
    ):
        """
        Attach the given item token to another item token. Target item token becomes a parent to the given item token.

        Body
        Parameter 	            Type 	 Description 	                                                    Required
        parentTokenId 	        String   Token ID of the item token becoming a parent 	                    Required
        serviceWalletAddress 	String   Address of the relevant service wallet.                            Required
        serviceWalletSecret 	String 	 Secret of serviceWalletAddress 	                                Required
        tokenHolderAddress      String 	 Address of the holder of selected item tokens (parent and child).  Optional
        tokenHolderUserId       String 	 User ID of the owner of selected item tokens (parent and child) 	Optional
        """
        pass

    @json
    @returns.json
    @put("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}/parent")
    def detach_non_fungible_token(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index"),
        detach_request: Body
    ):
        """
        Detach the given item token from its parent.

        Body
        Parameter               Type 	Description                                                           Required
        serviceWalletAddress 	String 	Address of the relevant service wallet.                               Required
        serviceWalletSecret 	String 	Secret of serviceWalletAddress                                        Required
        tokenHolderAddress      String 	Address of the holder of selected item tokens (parent and child)      Optional
        tokenHolderUserId       String 	User ID of the owner of selected item tokens (parent and child)       Optional
        """
        pass

    @json
    @returns.json
    @post("/v1/item-tokens/{contract_id}/fungibles")
    def create_fungible_token(
        self,
        contract_id: Path("contract_id"),
        create_request: Body
    ):
        """
        Request to create a new Token Type of the fungible item token with the given contract.

        Note

        This endpoint creates a new Token Type but DOES NOT mint any tokens. To issue/mint fungibles, call the Mint a fungible endpoint.

        Body
        Parameter 	     Type 	  Description 	                                                                        Required
        ownerAddress 	 String   Address of the item token contract owner                                              Required
        ownerSecret 	 String   Secret of the item token contract owner                                               Required
        name 	         String   Name of the item token type. At least 3 characters and up to 20 characters.
                                   Lowercase and uppercase alphabets and numbers only. 	                                Required
        meta 	         String   Metadata string of the given item token type. No fixed format up to 1,000 characters. Optional
        """
        pass


    @json
    @returns.json
    @post("/v1/item-tokens/{contract_id}/fungibles/{token_type}/mint")
    def mint_fungible_token(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        mint_request: Body
    ):
        """
        Request to mint a new fungible item token with the given Contract ID and Token Type and transfer it to the given wallet.

        Note

        To mint a fungible item token, it should have been created in advance. Refer to Create a fungible.

        Body
        Parameter 	   Type 	Description 	                                        Required
        ownerAddress   String 	Address of the given contract owner 	                Required
        ownerSecret    String 	Secret of the given contract owner 	                    Required
        toUserId       String 	User ID of the user to receive the minted item token. 	Optional
        toAddress      String 	Wallet address to receive the minted item token.        Optional
        amount         String 	Amount to be minted, larger than 1 but less than 2^255. Required

        Must specify either toUserId or toAddress. You will get an error when both parameters or nothing are specified
        """
        pass

    @json
    @returns.json
    @post("/v1/item-tokens/{contract_id}/fungibles/{token_type}/burn")
    def burn_fungible_token(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        burn_request: Body
    ):
        """
        Request to burn a specific fungible item token.

        Note

        When you’re burning an item token from the user wallet, you should use the address and secret of the contract owner wallet for the given item token, instead of the user wallet.
        To this end, users need to set up a proxy in the user wallet in advance. Refer to Issue a session token for item token proxy setting.

        Body
        Parameter 	   Type 	Description 	                                                    Required
        ownerAddress   String 	Address of the given contract owner 	                            Required
        ownerSecret    String 	Secret of the given contract owner 	                                Required
        fromUserId     String 	User ID of the user who holds the given item token to be burnt. 	Optional
        fromAddress    String 	Address of the wallet that holds the given item token.              Optional
        amount         String 	Amount to be burnt, larger than 1 but less than 2^255.              Required

        Must specify either fromUserId or fromAddress. You will get an error when both parameters or no parameter is specified.
        """
        pass

    @json
    @returns.json
    @post("/v1/item-tokens/{contract_id}/non-fungibles")
    def create_non_fungible_token(
        self,
        contract_id: Path("contract_id"),
        create_request: Body
    ):
        """
        Request to create a new Token Type of the non-fungible item token with the given contract.

        Note

        You need to create a non-fungible item token first in the console to get the contract ID of the item token.

        Body
        Parameter 	   Type 	Description 	                                                    Required
        ownerAddress   String 	Address of the given contract owner 	                            Required
        ownerSecret    String 	Secret of the given contract owner 	                                Required
        fromUserId     String 	User ID of the user who holds the given item token to be burnt. 	Optional
        fromAddress    String 	Address of the wallet that holds the given item token.              Optional
        amount         String 	Amount to be burnt, larger than 1 but less than 2^255.              Required

        Must specify either fromUserId or fromAddress. You will get an error when both parameters or no parameter is specified.
        """
        pass

    @json
    @returns.json
    @post("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/mint")
    def mint_non_fungible_token(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        mint_request: Body
    ):
        """
        Request to mint a non-fungible item token with the given Contract ID, Token Type and Token Index, and issue it to the given wallet.

        Note

        To mint a non-fungible item token, it should have been created in advance. Refer to Create a non-fungible.

        Body
        Parameter 	  Type 	    Description 	                                                                    Required
        toUserId 	  String 	User ID of the user to receive the minted item token. 	                            Optional†
        toAddress 	  String 	Address of the wallet to receive the minted item token. 	                        Optional†
        name 	      String 	Name of the given item token type. At least 3 characters and up to 20 characters. 	Required
        meta 	      String 	Metadata string of the given item token. No fixed format. 	                        Optional
        ownerAddress  String 	Address of the contract owner service wallet 	                                    Required
        ownerSecret   String 	Secret key of the contract owner service wallet 	                                Required

        Must specify either toUserId or toAddress. You will get an error when both parameters or nothing are specified
        """
        pass

    @json
    @returns.json
    @post("/v1/item-tokens/{contract_id}/non-fungibles/multi-mint")
    def multi_mint_non_fungible_token(
        self,
        contract_id: Path("contract_id"),
        multi_mint_request: Body
    ):
        """
        Request to mint multiple non-fungible item tokens belonging in the given contract in batch.

        Note

        Selected item tokens must belong to the same contract, and tokens can be sent to one wallet each time.

        Body
        Parameter 	   Type 	            Description 	                                                            Required
        mintList 	   Array of MintList 	Array composed of the list of selected item tokens (tokenType, name, meta) 	Required
        toUserId 	   String 	            User ID of the user to receive minted item tokens. 	                        Optional
        toAddress 	   String 	            Address of the wallet to receive minted item tokens. 	                    Optional
        ownerAddress   String 	            Address of the contract owner service wallet 	                            Required
        ownerSecret    String 	            Secret key of the contract owner service wallet 	                        Required

        Must specify either toUserId or toAddress. You will get an error when both parameters or nothing are specified.

        Composition of MintList object

        Parameter 	Type 	Description 	                                                                              Required
        tokenType 	String 	Token Type of the item token to be minted. 	                                                  Required
        name 	    String 	Name of the given item token. At least 3 characters and up to 20 characters. 	              Required
        meta 	    String 	Additional metadata string of the given item token. No fixed format up to 1,000 characters.   Optional
        """
        pass

    @json
    @returns.json
    @post("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}/burn")
    def burn_non_fungible_token(
        self,
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index"),
        burn_request: Body
    ):
        """
        Request to burn a specific non-fungible item token.

        Note

        Unlike for service tokens, when you’re burning an item token, you should use the secret of the contract owner wallet for the given item token, instead of the user wallet.
        To this end, users need to set up a proxy to delegate permissions to the contract owner wallet.

        Body
        Parameter 	    Type 	Description 	                                                        Required
        fromUserId 	    String 	User ID of the user linked to the wallet with the given item token 	    Optional
        fromAddress 	String 	Address of the wallet with the given item token 	                    Optional
        ownerAddress 	String 	Address of the contract owner service wallet 	                        Required
        ownerSecret 	String 	Secret of the contract owner service wallet 	                        Required

        Must enter either fromUserId or fromAddress. You will get an error when both parameters are entered.
        """
        pass


    @returns.json
    @get("/v1/wallets")
    def service_wallets(self):
        """Retrieve the list of service wallets issued at the console and their metadata."""
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}")
    def service_wallet_detail(self, wallet_address: Path("wallet_address")):
        """Retrieve the metadata of the given service wallet."""
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}/transactions")
    def service_wallet_transactions(
        self,
        wallet_address: Path("wallet_address"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc",
        before: Query = None,
        after: Query = None
    ):
        """
        Retrieve the transaction details related to the given service wallet.
        Service wallet related transactions’ should meet one of the following conditions.

        Transactions signed by the given service wallet
        Transactions generated by other wallets, containing the address of the given service wallet
        """
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}/base-coin")
    def service_wallet_base_coin(
        self,
        wallet_address: Path("wallet_address")
    ):
        """Retrieve the balance of base coins in the given service wallet."""
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}/service-tokens")
    def service_wallet_service_tokens(
        self,
        wallet_address: Path("wallet_address"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc"
    ):
        """Retrieve the balance of all service tokens in the given service wallet."""
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}/service-tokens/{contract_id}")
    def service_wallet_service_token(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id")
    ):
        """Retrieve the balance of all service tokens in the given service wallet."""
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}/item-tokens/{contract_id}/fungibles")
    def service_wallet_fungible_tokens(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc"
    ):
        """Retrieve the balance of all fungible item tokens in the given service wallet."""
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}/item-tokens/{contract_id}/fungibles/{token_type}")
    def service_wallet_fungible_token(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type")
    ):
        """Retrieve the balance of a specific fungible item token in the given service wallet."""
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}/item-tokens/{contract_id}/non-fungibles")
    def service_wallet_non_fungible_tokens(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc"
    ):
        """Retrieve the balance of all non-fungible item tokens in the given service wallet."""
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}/item-tokens/{contract_id}/non-fungibles/{token_type}")
    def service_wallet_non_fungible_token_type(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc"
    ):
        """Retrieve the balance of a specific non-fungible item token with the given Token Type in the given service wallet."""
        pass

    @returns.json
    @get("/v1/wallets/{wallet_address}/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}")
    def service_wallet_non_fungible_token(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index")
    ):
        """Retrieve the balance of a specific non-fungible item token in the given service wallet."""
        pass

    @json
    @returns.json
    @post("/v1/wallets/{wallet_address}/base-coin/transfer")
    def service_wallet_transfer_basecoin(
        self,
        wallet_address: Path("wallet_address"),
        transfer_request: Body
    ):
        """
        Request to transfer base coins in the given service wallet to another wallet.

        Body
        Parameter 	   Type 	Description 	                                       Required
        walletSecret   String 	Secret of the service wallet that holds base coins.    Required
        toAddress      String 	Address of the wallet to receive base coins. 	       Optional
        toUserId       String 	User ID of the recipient 	                           Optional
        amount         String 	Amount to transfer, larger than or equal to 1 and less than 2^255, as a string in decimal numbers. 	Required
        """
        pass

    @json
    @returns.json
    @post("/v1/wallets/{wallet_address}/service-tokens/{contract_id}/transfer")
    def service_wallet_transfer_service_token(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id"),
        transfer_request: Body
    ):
        """
        Request to transfer base coins in the given service wallet to another wallet.

        Body
        Parameter 	   Type 	Description 	                                                    Required
        walletSecret   String 	Secret of the service wallet that holds the given service token.    Required
        toAddress      String 	Address of the wallet to receive base coins. 	                    Optional
        toUserId       String 	User ID of the recipient 	                                        Optional
        amount         String 	Amount to transfer, larger than or equal to 1 and less than 2^255, as a string in decimal numbers. 	Required
        """
        pass


    @json
    @returns.json
    @post("/v1/wallets/{wallet_address}/item-tokens/{contract_id}/fungibles/{token_type}/transfer")
    def service_wallet_transfer_fungible_token(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        transfer_request: Body
    ):
        """
        Request to transfer base coins in the given service wallet to another wallet.

        Body
        Parameter 	   Type 	Description 	                                                  Required
        walletSecret   String 	Secret of the service wallet that holds  the given item token.    Required
        toAddress      String 	Address of the wallet to receive base coins. 	                  Optional
        toUserId       String 	User ID of the recipient 	                                      Optional
        amount         String 	Amount to transfer, larger than or equal to 1 and less than 2^255, as a string in decimal numbers. 	Required
        """
        pass

    @json
    @returns.json
    @post("/v1/wallets/{wallet_address}/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}/transfer")
    def service_wallet_transfer_nonfungible_token(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index"),
        transfer_request: Body
    ):
        """
        Request to transfer base coins in the given service wallet to another wallet.

        Body
        Parameter 	   Type 	Description 	                                                  Required
        walletSecret   String 	Secret of the service wallet that holds  the given item token.    Required
        toAddress      String 	Address of the wallet to receive base coins. 	                  Optional
        toUserId       String 	User ID of the recipient 	                                      Optional
        """
        pass

    @json
    @returns.json
    @post("/v1/wallets/{wallet_address}/item-tokens/{contract_id}/non-fungibles/batch-transfer")
    def service_wallet_batch_transfer_nonfungible_token(
        self,
        wallet_address: Path("wallet_address"),
        contract_id: Path("contract_id"),
        transfer_request: Body
    ):
        """
        Request to batch transfer multiple non-fungible item tokens in the given service wallet to another wallet.

        Tip
        Item tokens that are batch transferred must belong to the same contract, and they should be sent to the single wallet each time.

        Body
        Parameter 	     Type                 Description 	                                                  Required
        walletSecret 	 String               Secret of the service wallet that holds given item tokens.      Required
        toAddress 	     String               Address of the wallet to receive non-fungible item tokens.      Optional
        toUserid 	     String               User ID of the recipient 	                                      Optional
        transferList 	 Array of Token IDs   List of item tokens to be sent in batch.                        Required
                                              Array of an object including tokenId with at least one input.
                                                * tokenId: Token ID <https://docs-blockchain.line.biz/glossary/?id=token-id> of given item tokens
        """
        pass

    @returns.json
    @get("/v1/users/{user_id}")
    def user_detail(self, user_id: Path("user_id")):
        """Retrieve the information of the given user."""
        pass

    @returns.json
    @get("/v1/users/{user_id}/transactions")
    def user_transactions(
        self,
        user_id: Path("user_id"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc",
        before: Query = None,
        after: Query = None
    ):
        """
        Retrieve the transaction details related to the given user wallet. ‘User wallet related transactions’ should meet one of the following conditions.

        1. Transactions signed and committed by the given user wallet
        2. Transactions generated by other wallets, containing the address of the given user wallet


        """
        pass

    @returns.json
    @get("/v1/users/{user_id}/base-coin")
    def user_base_coin(
        self,
        user_id: Path("user_id")
    ):
        """Retrieve the balance of base coins in the given user wallet."""
        pass

    @returns.json
    @get("/v1/users/{user_id}/service-tokens")
    def user_service_tokens(
        self,
        user_id: Path("user_id"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc"
    ):
        """Retrieve the balance of all service tokens held by the given user."""
        pass

    @returns.json
    @get("/v1/users/{user_id}/service-tokens/{contract_id}")
    def user_service_token(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id")
    ):
        """Retrieve the balance of a specific service token held by the given user."""
        pass

    @returns.json
    @get("/v1/users/{user_id}/item-tokens/{contract_id}/fungibles")
    def user_fungible_tokens(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc"
    ):
        """Retrieve the balance of all fungible item tokens held by the given user."""
        pass

    @returns.json
    @get("/v1/users/{user_id}/item-tokens/{contract_id}/fungibles/{token_type}")
    def user_fungible_token(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type")
    ):
        """Retrieve the balance of a specific fungible item token held by the given user."""
        pass

    @returns.json
    @get("/v1/users/{user_id}/item-tokens/{contract_id}/non-fungibles")
    def user_non_fungible_tokens(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc"
    ):
        """Retrieve the balance of all non-fungible item tokens held by the given user."""
        pass

    @returns.json
    @get("/v1/users/{user_id}/item-tokens/{contract_id}/non-fungibles/{token_type}")
    def user_non_fungible_token_type(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        limit: Query = 10,
        page: Query = 1,
        order_by: Query = "desc"
    ):
        """Retrieve the balance of a specific non-fungible item token with the given Token Type held by the user."""
        pass

    @returns.json
    @get("/v1/users/{user_id}/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}")
    def user_non_fungible_token(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index")
    ):
        """Retrieve the balance of a specific non-fungible item token with the given Token Index held by the given user."""
        pass

    @returns.json
    @get("/v1/user-requests/{request_session_token}")
    def user_session_token(
        self,
        request_session_token: Path("request_session_token")
    ):
        """
        Retrieve the status of the session token issued by the given user wallet from BITMAX Wallet.

        This endpoint can be used under the following situations.

        * Transferring base coin or service token from the user wallet.
        * Signing a transaction with the user wallet after obtaining service token and item token management permissions.
        """
        pass

    @returns.json
    @get("/v1/users/{user_id}/service-tokens/{contract_id}/proxy")
    def user_service_token_proxy(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id")
    ):
        """Check if the proxy <https://docs-blockchain.line.biz/glossary/?id=proxy> has been set for the service token in the given user wallet."""
        pass

    @returns.json
    @get("/v1/users/{user_id}/item-tokens/{contract_id}/proxy")
    def user_item_token_proxy(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id")
    ):
        """Check if the proxy is set on a specific item token in the given user wallet."""
        pass


    @returns.json
    @json
    @post("/v1/users/{user_id}/base-coin/request-transfer")
    def issue_session_base_coin_transfer(
        self,
        user_id: Path("user_id"),
        issue_session_request: Body,
        request_type: Query = "aoa"
    ):
        """
        Issue a session token for BITMAX Wallet passcode authentication to transfer base coins in the given user wallet to another wallet.
        A new session token should be issued for each transfer, and the same token can’t be reused.

        Body
        Parameter 	Type 	Description                            Required
        toAddress 	String 	Wallet address of the recipient        Optional
        toUserId 	String 	User ID of the recipient 	           Optional
        amount      String 	Amount to transfer 	                   Required
        landingUri 	String 	                                       Optional

        * landingUri: URL of the landing page for the user returning to the service after completing BITMAX Wallet authentication.

        When this parameter is entered, BITMAX Wallet provides a button to redirect with this URL.
        If not, BITMAX Wallet provides an instruction for the user to return to the service.
        """
        pass

    @returns.json
    @json
    @post("/v1/users/{user_id}/service-tokens/{contract_id}/request-transfer")
    def issue_session_service_token_transfer(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        issue_session_request: Body,
        request_type: Query = "aoa"
    ):
        """
        Issue a session token for BITMAX Wallet passcode authentication to transfer service tokens in the given user wallet to another wallet.

        Body
        Parameter 	Type 	Description                            Required
        toAddress 	String 	Wallet address of the recipient        Optional
        toUserId 	String 	User ID of the recipient 	           Optional
        amount      String 	Amount to transfer 	                   Required
        landingUri 	String 	                                       Optional

        * landingUri: URL of the landing page for the user returning to the service after completing BITMAX Wallet authentication.

        When this parameter is entered, BITMAX Wallet provides a button to redirect with this URL.
        If not, BITMAX Wallet provides an instruction for the user to return to the service.
        """
        pass

    @returns.json
    @json
    @post("/v1/users/{user_id}/service-tokens/{contract_id}/request-proxy")
    def request_service_token_proxy(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        proxy_request: Body,
        request_type: Query = "aoa"
    ):
        """
        Issue a session token for BITMAX Wallet passcode authentication to set proxy with a specific service token in the given user wallet.

        Proxy is a service wallet that has permissions to transfer, and burn a service token in the user wallet through user authentication.
        When the proxy is set, the relevant permissions are delegated to the contract owner wallet that the given service token belongs to and this contract owner wallet can generate a transaction on behalf of the user wallet from then on.
        Other service wallets, except for the owner wallet, don’t have permissions.

        Body
        Parameter 	 Type 	  Description                                     Required
        ownerAddress String   Address of the contract owner service wallet    Optional
        landingUri 	 String 	                                              Optional

        * landingUri: URL of the landing page for the user returning to the service after completing BITMAX Wallet authentication.

        When this parameter is entered, BITMAX Wallet provides a button to redirect with this URL.
        If not, BITMAX Wallet provides an instruction for the user to return to the service.
        """
        pass

    @returns.json
    @json
    @post("/v1/users/{user_id}/item-tokens/{contract_id}/request-proxy")
    def request_item_token_proxy(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        proxy_request: Body,
        request_type: Query = "aoa"
    ):
        """
        Issue a session token for BITMAX Wallet passcode authentication to set proxy with a specific service token in the given user wallet.

        Proxy is a service wallet that has permissions to transfer, and burn a service token in the user wallet through user authentication.
        When the proxy is set, the relevant permissions are delegated to the contract owner wallet that the given service token belongs to and this contract owner wallet can generate a transaction on behalf of the user wallet from then on.
        Other service wallets, except for the owner wallet, don’t have permissions.

        Body
        Parameter 	 Type 	  Description                                     Required
        ownerAddress String   Address of the contract owner service wallet    Optional
        landingUri 	 String 	                                              Optional

        * landingUri: URL of the landing page for the user returning to the service after completing BITMAX Wallet authentication.

        When this parameter is entered, BITMAX Wallet provides a button to redirect with this URL.
        If not, BITMAX Wallet provides an instruction for the user to return to the service.
        """
        pass


    @returns.json
    @json
    @post("/v1/user-requests/{request_session_token}/commit")
    def commit_user_transaction(
        self,
        request_session_token: Path("request_session_token")
    ):
        """Commit a transaction signed by the given user wallet."""
        pass


    @returns.json
    @json
    @post("/v1/users/{user_id}/service-tokens/{contract_id}/transfer")
    def transfer_user_service_token(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        transfer_request: Body
    ):
        """
        Request to transfer the delegated service token in the given user wallet to another wallet.

        Body
        Parameter 	     Type 	    Description 	                                                                                   Required
        ownerAddress     String 	Address of the service wallet that owns the contract of the service token. 	                       Required
        ownerSecret      String 	Secret key of the contract owner service wallet 	                                               Required
        toAddress        String 	Wallet address of the recipient 	                                                               Optional
        toUserId         String 	User ID of the recipient 	                                                                       Optional
        amount           String 	Amount to transfer, larger than or equal to 1 and less than 2^255, as a string in decimal numbers. Required
        """
        pass

    @returns.json
    @json
    @post("/v1/users/{user_id}/item-tokens/{contract_id}/fungibles/{token_type}/transfer")
    def transfer_user_fungible_token(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        transfer_request: Body
    ):
        """
        Request to transfer the delegated fungible item token in the given user wallet to another wallet.

        Body
        Parameter 	     Type 	    Description 	                                                                                   Required
        ownerAddress     String 	Address of the service wallet that owns the contract of the given item token.                      Required
        ownerSecret      String 	Secret key of the contract owner service wallet 	                                               Required
        toAddress        String 	Wallet address of the recipient 	                                                               Optional
        toUserId         String 	User ID of the recipient 	                                                                       Optional
        amount           String 	Amount to transfer, larger than or equal to 1 and less than 2^255, as a string in decimal numbers. Required
        """
        pass

    @returns.json
    @json
    @post("/v1/users/{user_id}/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}/transfer")
    def transfer_user_non_fungible_token(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        token_type: Path("token_type"),
        token_index: Path("token_index"),
        transfer_request: Body
    ):
        """
        Request to transfer the delegated non-fungible item token in the given user wallet to another wallet.

        Body
        Parameter 	     Type 	    Description 	                                                                                   Required
        ownerAddress     String 	Address of the service wallet that owns the contract of the given item token.                      Required
        ownerSecret      String 	Secret key of the contract owner service wallet 	                                               Required
        toAddress        String 	Wallet address of the recipient 	                                                               Optional
        toUserId         String 	User ID of the recipient 	                                                                       Optional
        """
        pass

    @returns.json
    @json
    @post("/v1/users/{user_id}/item-tokens/{contract_id}/non-fungibles/batch-transfer")
    def batch_transfer_user_non_fungible_token(
        self,
        user_id: Path("user_id"),
        contract_id: Path("contract_id"),
        transfer_request: Body
    ):
        """
        Request to batch transfer multiple delegated non-fungible item tokens in the given user wallet to another wallet.

        Body
        Parameter 	     Type 	    Description 	                                                                        Required
        ownerAddress     String 	Address of the service wallet that owns the contract of the given item token.           Required
        ownerSecret      String 	Secret key of the contract owner service wallet 	                                    Required
        toAddress        String 	Wallet address of the recipient 	                                                    Optional
        toUserId         String 	User ID of the recipient 	                                                            Optional
        transferList     Array      List of item tokens to be sent in batch.                                                Required
                                    Array of an object including tokenId with at least one input.
                                    tokenId: Token ID <https://docs-blockchain.line.biz/glossary/?id=token-id> of given item tokens
        """
        pass

    @returns.json
    @get("/v1/transactions/{tx_hash}")
    def transaction_result(self, tx_hash: Path("tx_hash")):
        """Retrieve the status and information of the given transaction."""
        pass

    @returns.json
    @get("/v1/memos/{tx_hash}")
    def get_memo(self, tx_hash: Path("tx_hash")):
        """Retrieve the text saved in the given transaction."""
        pass

    @json
    @returns.json
    @post("/v1/memos")
    def post_memo(self, memo_request: Body):
        """
        Save the text in the blockchain. This task is irrelevant to tokens.

        Body
        Parameter       Type 	Description                                               Required
        walletAddress 	String 	Address of the service wallet                             Required
        walletSecret 	String 	Wallet secret of the service wallet                       Required
        memo            String 	Text to be saved. Free format up to 1,000 characters.     Required
        """
        pass
