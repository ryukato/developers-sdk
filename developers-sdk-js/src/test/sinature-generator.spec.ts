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
});
