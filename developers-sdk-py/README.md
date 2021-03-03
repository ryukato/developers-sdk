# Line developers-sdk-py
This is written by Python to support development with Python.

## Start development
### Copy repository
1. clone this repository
  - `git clone https://github.com/ryukato/developers-sdk.git`
2. Change directory to `developers_sdk_py`
  - `cd developers_sdk_py`

### Setup environment

#### environment variables
To run the tests, you need to create `.env` file whose api_key, secret and base_url for developers-api.

* API_BASE_URL="[base-url]"
* SERVICE_API_KEY="[your api-key]"
* SERVICE_API_SECRET="[your api-secret]"

##### Sample
API_BASE_URL="test-api.blockchain.line.me"
SERVICE_API_KEY="test-api-key"
SERVICE_API_SECRET="test-api-secret"

#### Using `venv`
1. Create virtual environment
  - `python3 -m venv venv`
2. Activate the virtual environment
  - `source venv/bin/activate`
3. Run tests to see everything working fine.
  - `python setup.py test`

> Note
>
> To run single test, then please refer to following commend.
`python setup.py test -s tests.test_api_client.TestApiClient.test_create_instance_and_call_service_wallets`

#### Using pipenv
1. if you don't have `pipenv`, then please install it by run comment.
  - `pip install pipenv`
2. Activate virtual environment.
  - `pipenv shell`
2. Run below command to setup virtual environment and install requirements.
  - `pipenv install`
3. Run tests to see everything working fine.
  - `python -m unittest tests/*.py`

> Note
>
> This project support `venv` and `pipenv`, then we have to update both `setup.py` and `Pipfile` whenever install a new requirement.

## Examples using sdk
### Create ApiClient

```
api_base_url = os.getenv("API_BASE_URL")
service_api_key = os.getenv("SERVICE_API_KEY")
service_api_secret = os.getenv("SERVICE_API_SECRET")
api_client = ApiClient(
    base_url=api_base_url,
    auth=ApiSignatureAuth(service_api_key, service_api_secret, SignatureGenerator()))
```

### Get time

```
response = api_client.time()
response_time = response["responseTime"]
```

### Mint Service-token

```
request_body = {
    "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
    "ownerSecret": "PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=",
    "toAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
    "amount": "1249051"
}
response = api_client.mint_service_token("service-token-contract-id", request_body)
statusCode = response["statusCode"]
if (statusCode != 1002) {
  # handling failed transaction
} else {
  txHash = response["responseData"]["txHash"]
  # handling accepted transaction  
}

```

### Query Service-token holders

```
response = api_client.service_token_holders("service-token-contract-id")
holders = response["responseData"]
```

## Additional resources
This section has some resources for Python beginner like me. :)
### Learning Python
* This is youtube channel for Python beginner: https://bit.ly/3q14Ddq
### Python3 Official document
* Nothing better than official document: https://docs.python.org/3
### Uplink
* Http Client for python, which is used in this sdk.
* document: https://uplink.readthedocs.io/en/stable/index.html
