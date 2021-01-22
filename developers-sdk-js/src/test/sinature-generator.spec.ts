import { expect } from 'chai';
import { describe, it } from 'mocha';
import { SingtureGenerator } from '../lib/signature-generator';

describe('signature-generator test', () => {
  it('signature without parameters test', () => {
    let method = "GET"
    let path = "/v1/wallets"
    let timestamp = 1581850266351
    let secret = "9256bf8a-2b86-42fe-b3e0-d3079d0141fe"
    let nonce = "Bp0IqgXE"
    let signature = SingtureGenerator.signature(secret, method, path, timestamp, nonce)
    expect(signature).to.equal("2LtyRNI16y/5/RdoTB65sfLkO0OSJ4pCuz2+ar0npkRbk1/dqq1fbt1FZo7fueQl1umKWWlBGu/53KD2cptcCA==")
  });

  it('signature with parameters test', () => {
    let parameters = {
      "page": 2,
      "msgType": "coin/MsgSend"
    };

    let method = "GET"
    let path = "/v1/wallets/tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq/transactions"
    let timestamp = 1581850266351
    let secret = "9256bf8a-2b86-42fe-b3e0-d3079d0141fe"
    let nonce = "Bp0IqgXE"
    let signature = SingtureGenerator.signature(secret, method, path, timestamp, nonce, parameters)
    expect(signature).to.equal("5x6bEV1mHkpJpEJMnMsCUH7jV5GzKzA038UwcqpYIAx7Zn1SvA9qhdf+aitu+3juXzXB+qSxM4zRon6/aNVMFg==")
  });

  it('signature with paing parameters test', () => {
    // paging parameters sorted by its key when generating signature
    let parameters = {
      "limit": 10,
      "page": 1,
      "orderBy": "desc"
    };

    let method = "GET"
    let path = "/v1/service-tokens/a48f097b/holders"
    let timestamp = 1611243023551
    let secret = "098d8862-477d-49f2-928f-7655489be2d3"
    let nonce = "KScYbbH0"
    // sign-target will be "KScYbbH01611243023551GET/v1/service-tokens/a48f097b/holders?limit=10&orderBy=desc&page=1"
    let signature = SingtureGenerator.signature(secret, method, path, timestamp, nonce, parameters)
    expect(signature).to.equal("8vcqBHXiwGaP5+78ZvuidcoZ/UiKnR1IrgXKzUaRf+HqetD5eHMaeTEW3OvHoKn7Z512WVNuKmRQDW88DvJ1aA==")
  });

});
