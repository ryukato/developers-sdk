# LINE Blockchain Developers SDK for JavaScript
This is a subproject of LINE Blockchain Developers SDK. 
See [README](../README.md) for overview.

This is written by Typescript, so it supports both Typescript and JavaScript.
## Build
To build this library, run following commands.

```
yarn
yarn run build
```

## Run all tests
Run the following command to test the library.

```
yarn run test
```

## Key objects and usage
### `HttpClient`
This class represents a HTTP client to connect and interact LINE Blockchain Developers API. It provides functions to call the endpoints of the API with mandatory and optional parameters.

It's an entry point for this library, every dApp for LINE Blockchain Developers should have an instance of `HttpClient`.

Create an instance with your connection and authentication information as follows:

```javascript
import { HttpClient } from '../lib/http-client-base';
const httpClient = new HttpClient(baseUrl, apiKey, apiSecret);
```

- `baseUrl` is the address of API server. Find one for the chain your service runs on in [API guide](https://docs-blockchain.line.biz/api-guide/).
- `apiKey` is your service's API key.
- `apiSecret` is your serivce's API secret. **Never** use the secret hardcoded in the source code.

Now, you can call any endpoints via the functions of the instance. A simple example is to get the server time:

```javascript
(aync() => {
  const reponse = await httpclient.time();
  console.log(response['statusCode']);
})();
```

Remember that you must handle it in an asynchronous way.

### Request and response
When requesting, you can use predefined request data classes in `lib/request.ts`. Try to send a memo save request as follows:

```javascript
import { MemoRequest } from './lib/request';

(aync() => {
  const request = new MemoRequest('my first memo', walletAddress, walletSecret);
  const response = await httpClient.createMemo(request);
})();
```

When you need to parse a JSON-formatted `responseData` in your response, find and use the proper response data class in `lib/response.ts`. To get the txhash or the above request for example:

```javascript
import { GenericResponse, TxResultResponse } from './lib/response';

(aync() => {
  const request = new MemoRequest('my first memo', walletAddress, walletSecret);
  let response: GenericResponse<TxResultResponse> = await httpclient.createMemo(servcieId);
  console.log(response.responseData.txhash);
})();
```

### `SingtureGenerator`
This class provides a functionality to generate signatures for a request. 
All API requests, except for the endpoint to retrieve the server time, must pass authentication information and be signed. 

Never mind, fortunately, `HttpClient` itself will import this and generate signatures before sending a request. If you do want to study how LINE Blockchain signature created, it's okay to dive into the source code.
