import MockAdapter from 'axios-mock-adapter';

import * as chai from 'chai'
import chaiAsPromised from 'chai-as-promised'
chai.use(chaiAsPromised)

const expect = chai.expect;
import { describe, it } from "mocha";

import _ from "lodash";
import { HttpClient } from '../lib/http-client-base';
import { Constant } from '../lib/constants';
import { PageRequest, OrderBy } from '../lib/request';

describe('http-client-base test', () => {
  let stub: MockAdapter;

  after(() => {
    stub.restore();
  });

  const baseUrl = "http://localhost";
  const testApiKey = "test-api-key";
  const testSecret = "test-api-secret";
  const httpClient = new HttpClient(baseUrl, testApiKey, testSecret);

  it('time api test', async () => {

    const receivedData = {
      "responseTime": 1581850266351,
      "statusCode": 1000,
      "statusMessage": "success"
    };

    let httpClient = new HttpClient(baseUrl, testApiKey, testSecret);
    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet("/v1/time").reply(config => {
      assertHeaders(config.headers);
      return [200, receivedData];
    });

    const response = await httpClient.time();
    expect(response["statusCode"]).to.equal(1000);
  })

  it('service-detail api test', async () => {
    const testServiceId = "cad3f2d5-fb4d-4ab9-9355-56e862f92ff6";
    const receivedData = {
      "responseTime": 1585467717505,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": {
        "serviceId": testServiceId,
        "name": "TESTDAPP",
        "description": "TESTDAPP description",
        "category": "SNS"
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/services/${testServiceId}`).reply(config => {
      assertHeaders(config.headers);
      return [200, receivedData];
    });

    const response = await httpClient.serviceDetail(testServiceId);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"]["serviceId"]).to.equal(testServiceId);
  })

  it('service-tokens api test', async () => {
    const receivedData = {
      "responseTime": 1585467715136,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": [
        {
          "contractId": "9636a07e",
          "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
          "name": "skt1",
          "symbol": "SYNPH",
          "imgUri": "https://sample.image",
          "meta": "kjpxcnzuec5l1x8r5ngxl1ghl4tzvawv9bryobjvzc1o4uywnzeydcv4jl8f5mzw1w9e4897op6rsy43exbyojrk5e81jj9jvqd9yye6mdnffhbzptqyc8693ss4by0cjvle1jgtb8ofgr1tkve8nuyn3z9qm14wrtmdzsysvo2n33qwmc6gj2ugdsi9c4m8wa3alf5cdp1dkzs8vj715ifme6v0h4yvk7ranmby0hu0rewu7iv4ex79e8vyvqoodck1b3ry3az5xhfmlwbe1bmku908q3e0wy26rg6gcirgdbkhtryt1f1djpjo2zkkml94h8unwupoll",
          "decimals": 6,
          "createdAt": 1584070098000,
          "totalSupply": "2185",
          "totalMint": "2205",
          "totalBurn": "20",
          "serviceId": "cad3f2d5-fb4d-4ab9-9355-56e862f92ff6"
        }
      ]
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/service-tokens`).reply(config => {
      assertHeaders(config.headers);
      return [200, receivedData];
    });

    const response = await httpClient.serviceTokens();
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"][0]["contractId"]).to.equal("9636a07e");
  })

  it('service-token detail api test', async () => {
    const testContractId = "9636a07e";
    const receivedData = {
      "responseTime": 1585467715136,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": {
        "contractId": testContractId,
        "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
        "name": "skt1",
        "symbol": "SYNPH",
        "imgUri": "https://sample.image",
        "meta": "kjpxcnzuec5l1x8r5ngxl1ghl4tzvawv9bryobjvzc1o4uywnzeydcv4jl8f5mzw1w9e4897op6rsy43exbyojrk5e81jj9jvqd9yye6mdnffhbzptqyc8693ss4by0cjvle1jgtb8ofgr1tkve8nuyn3z9qm14wrtmdzsysvo2n33qwmc6gj2ugdsi9c4m8wa3alf5cdp1dkzs8vj715ifme6v0h4yvk7ranmby0hu0rewu7iv4ex79e8vyvqoodck1b3ry3az5xhfmlwbe1bmku908q3e0wy26rg6gcirgdbkhtryt1f1djpjo2zkkml94h8unwupoll",
        "decimals": 6,
        "createdAt": 1584070098000,
        "totalSupply": "2185",
        "totalMint": "2205",
        "totalBurn": "20",
        "serviceId": "cad3f2d5-fb4d-4ab9-9355-56e862f92ff6"
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/service-tokens/${testContractId}`).reply(config => {
      assertHeaders(config.headers);
      return [200, receivedData];
    });

    const response = await httpClient.serviceTokenDetail(testContractId);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"]["contractId"]).to.equal("9636a07e");
  })

  it('update service-token api test', async () => {
    const testContractId = "9636a07e";
    const request = {
      "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
      "ownerSecret": 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      "name": "dTudb9Hq5i2ieHyJFo6o",
      "meta": "bdfssdfasd"
    };

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPut(`/v1/service-tokens/${testContractId}`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response = await httpClient.updateServiceToken(testContractId, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('mint service-token api test', async () => {
    const testContractId = "9636a07e"
    const request = {
      "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
      "ownerSecret": 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      "toAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
      "amount": "1249051"
    };

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPost(`/v1/service-tokens/${testContractId}/mint`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response = await httpClient.mintServiceToken(testContractId, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('burn service-token api test', async () => {
    const testContractId = "9636a07e";
    const request = {
      "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
      "ownerSecret": 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      "amount": "31"
    };

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPost(`/v1/service-tokens/${testContractId}/burn`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response = await httpClient.burnServiceToken(testContractId, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('service-token-holders api test', async () => {
    const testContractId = "9636a07e"
    const pageRequest = new PageRequest(0, 10, OrderBy.DESC);
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const receivedData = {
      "responseTime": 1585467715916,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": [
        {
          "address": testAddress,
          "userId": null,
          "amount": "1066"
        }
      ]
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/service-tokens/${testContractId}/holders`).reply(config => {
      assertHeaders(config.headers);
      assertPageParameters(config.params, pageRequest);
      return [200, receivedData];
    });

    const response = await httpClient.serviceTokenHolders(testContractId, pageRequest);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"][0]["address"]).to.equal(testAddress);
  })

  it('item-token api test', async () => {
    const testContractId = "61e14383";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const receivedData = {
      "responseTime": 1585467704763,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": {
        "contractId": testContractId,
        "baseImgUri": "https://image-base-uri/",
        "ownerAddress": testAddress,
        "createdAt": 1584070104000,
        "serviceId": "cad3f2d5-fb4d-4ab9-9355-56e862f92ff6"
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/item-tokens/${testContractId}`).reply(config => {
      assertHeaders(config.headers);
      return [200, receivedData];
    });

    const response = await httpClient.itemToken(testContractId);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"]["ownerAddress"]).to.equal(testAddress);
  })

  it('all fungible-tokens api test', async () => {
    const testContractId = "61e14383";
    const testTokenType = "0000004a";
    const pageRequest = new PageRequest(0, 10, OrderBy.DESC);
    const receivedData = {
      "responseTime": 1585467697421,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": [
        {
          "tokenType": testTokenType,
          "name": "TOKEN0313",
          "meta": "",
          "createdAt": 1585378323000,
          "totalSupply": "0",
          "totalMint": "0",
          "totalBurn": "0"
        }
      ]
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/item-tokens/${testContractId}/fungibles`).reply(config => {
      assertHeaders(config.headers);
      assertPageParameters(config.params, pageRequest);
      return [200, receivedData];
    });

    const response = await httpClient.fungibleTokens(testContractId, pageRequest);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"][0]["tokenType"]).to.equal(testTokenType);
  })

  it('create fungible-token api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const request = {
      "ownerAddress": testAddress,
      "ownerSecret": 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      "name": "4W1Vj9U8tYf"
    }

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPost(`/v1/item-tokens/${testContractId}/fungibles`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response = await httpClient.createFungibleToken(testContractId, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('fungible-token api test', async () => {
    const testContractId = "61e14383";
    const testTokenType = "0000004a";

    const receivedData = {
      "responseTime": 1585467697421,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": {
        "tokenType": testTokenType,
        "name": "TOKEN0313",
        "meta": "",
        "createdAt": 1585378323000,
        "totalSupply": "0",
        "totalMint": "0",
        "totalBurn": "0"
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/item-tokens/${testContractId}/fungibles/${testTokenType}`).reply(config => {
      assertHeaders(config.headers);
      return [200, receivedData];
    });

    const response = await httpClient.fungibleToken(testContractId, testTokenType);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"]["tokenType"]).to.equal(testTokenType);
  })

  it('update fungible-token api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testTokenType = "0000004a";
    const request = {
      "ownerAddress": testAddress,
      "ownerSecret": 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      "name": "4W1Vj9U8tYf"
    }

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPut(`/v1/item-tokens/${testContractId}/fungibles/${testTokenType}`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response =
      await httpClient.updateFungibleToken(testContractId, testTokenType, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('mint fungible-token api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testTokenType = "0000004a";
    const request = {
      'ownerAddress': testAddress,
      'ownerSecret': 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      'toAddress': testAddress,
      'amount': '5113980'
    }

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPost(`/v1/item-tokens/${testContractId}/fungibles/${testTokenType}/mint`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response =
      await httpClient.mintFungibleToken(testContractId, testTokenType, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('burn fungible-token api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testTokenType = "0000004a";
    const request = {
      'ownerAddress': testAddress,
      'ownerSecret': 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      'fromAddress': testAddress,
      'amount': '5113980'
    }

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPost(`/v1/item-tokens/${testContractId}/fungibles/${testTokenType}/burn`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response =
      await httpClient.burnFungibleToken(testContractId, testTokenType, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('burn fungible-token without fromAddress and fromUserId api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testTokenType = "0000004a";
    const request = {
      'ownerAddress': testAddress,
      'ownerSecret': 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      'amount': '5113980'
    }

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPost(`/v1/item-tokens/${testContractId}/fungibles/${testTokenType}/burn`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const promise = httpClient.burnFungibleToken(testContractId, testTokenType, request)
    await expect(promise).to.eventually.be.rejectedWith(Error)
  })

  it('fungible-token-holders api test', async () => {
    const testContractId = "9636a07e"
    const pageRequest = new PageRequest(0, 10, OrderBy.DESC);
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testTokenType = "0000004a";
    const receivedData = {
      "responseTime": 1585467715916,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": [
        {
          "walletAddress": testAddress,
          "userId": null,
          "amount": "1066"
        }
      ]
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/item-tokens/${testContractId}/fungibles/${testTokenType}/holders`).reply(config => {
      assertHeaders(config.headers);
      assertPageParameters(config.params, pageRequest);
      return [200, receivedData];
    });

    const response = await httpClient.fungibleTokenHolders(testContractId, testTokenType, pageRequest);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"][0]["walletAddress"]).to.equal(testAddress);
  })

  it('non-fungible-tokens api test', async () => {
    const testContractId = "9636a07e"
    const pageRequest = new PageRequest(0, 10, OrderBy.DESC);
    const testTokenType = "0000004a";
    const receivedData = {
      "responseTime": 1585467713288,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": [
        {
          "tokenType": testTokenType,
          "name": "y1gcofvx0y86",
          "meta": "",
          "createdAt": 1585353869000,
          "totalSupply": "0",
          "totalMint": "0",
          "totalBurn": "0"
        }
      ]
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/item-tokens/${testContractId}/non-fungibles`).reply(config => {
      assertHeaders(config.headers);
      assertPageParameters(config.params, pageRequest);
      return [200, receivedData];
    });

    const response = await httpClient.nonFungibleTokens(testContractId, pageRequest);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"][0]["tokenType"]).to.equal(testTokenType);
  })

  it('create non-fungible-token api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const request = {
      'ownerAddress': testAddress,
      'ownerSecret': 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      'name': 'yVvznw2RICXtz11Lw',
      'meta': '235v234r01234'
    };

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPost(`/v1/item-tokens/${testContractId}/non-fungibles`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response = await httpClient.createNonFungibleToken(testContractId, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('non-fungible-token-type api test', async () => {
    const testContractId = "9636a07e"
    const pageRequest = new PageRequest(0, 10, OrderBy.DESC);
    const testTokenType = "0000004a";
    const testTokenIndex = "00000001";
    const receivedData = {
      "responseTime": 1585467712122,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": {
        "tokenType": testTokenType,
        "name": "NFT Name",
        "meta": "NFT meta",
        "createdAt": 1584075623000,
        "totalSupply": 13,
        "totalMint": 15,
        "totalBurn": 2,
        "token": [
          {
            "tokenIndex": testTokenIndex,
            "name": "NFT index name",
            "meta": "NFT index meta",
            "createdAt": 1584075664000,
            "burnedAt": null
          }
        ]
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/item-tokens/${testContractId}/non-fungibles/${testTokenType}`).reply(config => {
      assertHeaders(config.headers);
      assertPageParameters(config.params, pageRequest);
      return [200, receivedData];
    });

    const response = await httpClient.nonFungibleTokenType(testContractId, testTokenType, pageRequest);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"]["tokenType"]).to.equal(testTokenType);
    expect(response["responseData"]["token"][0]["tokenIndex"]).to.equal(testTokenIndex);
  })

  it('update non-fungible-token-type api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testTokenType = "0000004a";
    const request = {
      'ownerAddress': testAddress,
      'ownerSecret': 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      'name': 'yVvznw2RICXtz11Lw',
      'meta': '235v234r01234'
    };

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onPut(`/v1/item-tokens/${testContractId}/non-fungibles/${testTokenType}`).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response = await httpClient.updateNonFungibleTokenType(testContractId, testTokenType, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('non-fungible-token api test', async () => {
    const testContractId = "9636a07e"
    const testTokenType = "0000004a";
    const testTokenIndex = "00000001";
    const testTokenId = testTokenType + testTokenIndex
    const receivedData = {
      "responseTime": 1585467695350,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": {
        "name": "NFT index name",
        "tokenId": testTokenId,
        "meta": "NFT index meta",
        "createdAt": 1584075664000,
        "burnedAt": null
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/item-tokens/${testContractId}/non-fungibles/${testTokenType}/${testTokenIndex}`).reply(config => {
      assertHeaders(config.headers);
      return [200, receivedData];
    });

    const response = await httpClient.nonFungibleToken(testContractId, testTokenType, testTokenIndex);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"]["tokenId"]).to.equal(testTokenId);
  })

  it('update non-fungible-token api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testTokenType = "0000004a";
    const testTokenIndex = "00000001";
    const request = {
      'ownerAddress': testAddress,
      'ownerSecret': 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      'name': 'yVvznw2RICXtz11Lw',
      'meta': '235v234r01234'
    };

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    const path = `/v1/item-tokens/${testContractId}/non-fungibles/${testTokenType}/${testTokenIndex}`
    stub.onPut(path).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response = await httpClient.updateNonFungibleToken(testContractId, testTokenType, testTokenIndex, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('mint non-fungible-token api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testTokenType = "0000004a";
    const testTokenIndex = "00000001";
    const request = {
      'ownerAddress': testAddress,
      'ownerSecret': 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      'toAddress': 'tlink1wxxfe3etmaxv8hvrdxfwveewrcynynhlnm0jkn',
      'name': 'Nnq8Eda',
      'meta': '5y4bh'
    }

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    const path = `/v1/item-tokens/${testContractId}/non-fungibles/${testTokenType}/${testTokenIndex}/mint`
    stub.onPost(path).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response = await httpClient.mintNonFungibleToken(testContractId, testTokenType, testTokenIndex, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })

  it('non-fungible-token-type holders api test', async () => {
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testContractId = "9636a07e"
    const testTokenType = "0000004a";
    const pageRequest = new PageRequest(0, 10, OrderBy.DESC);
    const receivedData = {
      "responseTime": 1585467711436,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": [
        {
          "walletAddress": testAddress,
          "userId": null,
          "numberOfIndex": "5"
        }
      ]
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/item-tokens/${testContractId}/non-fungibles/${testTokenType}/holders`).reply(config => {
      assertHeaders(config.headers);
      return [200, receivedData];
    });

    const response = await httpClient.nunFungibleTokenTypeHolders(testContractId, testTokenType, pageRequest);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"][0]["walletAddress"]).to.equal(testAddress);
  })

  it('non-fungible-token holder api test', async () => {
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const testContractId = "9636a07e"
    const testTokenType = "0000004a";
    const testTokenIndex = "00000001";
    const receivedData = {
      "responseTime": 1585467711436,
      "statusCode": 1000,
      "statusMessage": "Success",
      "responseData": {
        "walletAddress": testAddress,
        "userId": null,
        "numberOfIndex": "5"
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    stub.onGet(`/v1/item-tokens/${testContractId}/non-fungibles/${testTokenType}/${testTokenIndex}/holder`).reply(config => {
      assertHeaders(config.headers);
      return [200, receivedData];
    });

    const response = await httpClient.nunFungibleTokenHolder(testContractId, testTokenType, testTokenIndex);
    expect(response["statusCode"]).to.equal(1000);
    expect(response["responseData"]["walletAddress"]).to.equal(testAddress);
  })

  it('multi-mint non-fungible-token api test', async () => {
    const testContractId = "9636a07e";
    const testAddress = "tlink1nf5uhdmtsshmkqvlmq45kn4q9atnkx4l3u4rww";
    const request = {
      'ownerAddress': testAddress,
      'ownerSecret': 'PCSO7JBIH1gWPNNR5vT58Hr2SycFSUb9nzpNapNjJFU=',
      'toAddress': testAddress,
      'mintList': [
        {
          'tokenType': '10000001',
          'name': 'WGk',
          'meta': '5y4bh'
        },
        {
          'tokenType': '10000001',
          'name': 'aoU'
        }
      ]
    }

    const testTxHash = "22DF78611396824D293AF7ABA04A2A646B1E3055A19B32E731D8E03BAE743661";
    const receivedData = {
      "responseTime": 1585467711877,
      "statusCode": 1002,
      "statusMessage": "Accepted",
      "responseData": {
        "txHash": testTxHash
      }
    };

    stub = new MockAdapter(httpClient.getAxiosInstance());

    const path = `/v1/item-tokens/${testContractId}/non-fungibles/multi-mint`
    stub.onPost(path).reply(config => {
      assertHeaders(config.headers);
      expect(config.data).to.equal(JSON.stringify(request));
      return [200, receivedData];
    });

    const response = await httpClient.multiMintnonFungibleToken(testContractId, request);
    expect(response["statusCode"]).to.equal(1002);
    expect(response["responseData"]["txHash"]).to.equal(testTxHash);
  })
})


function assertHeaders(headers: any) {
  expect(headers).to.have.any.keys(Constant.SERVICE_API_KEY_HEADER);
  expect(headers).to.have.any.keys(Constant.NONCE_HEADER);
  expect(headers).to.have.any.keys(Constant.SIGNATURE_HEADER);
  expect(headers).to.have.any.keys(Constant.TIMESTAMP_HEADER);
}

function assertPageParameters(pageParameters: any, pageRequest: PageRequest) {
  expect(pageParameters["page"]).to.equal(pageRequest.page);
  expect(pageParameters["limit"]).to.equal(pageRequest.limit);
  expect(pageParameters["orderBy"]).to.equal(pageRequest.orderBy);
}
