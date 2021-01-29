import { LoggerFactory } from "./logger-factory";
import CryptoJS from 'crypto-js';
import _ from "lodash";
import { RequestBodyFlattener } from "./request-body-flattener";
/**
reference site: https://jokecamp.wordpress.com/2012/10/21/examples-of-creating-base64-hashes-using-hmac-sha256-in-different-languages/#js
*/
const logger = LoggerFactory.logger("SignatureGenerator");
export class SignatureGenerator {
  static signature(
    apiSecret: string,
    method: string,
    path: string,
    timestamp: number,
    nonce: string,
    parameters: object = {}, // query string
    body: object = {}
  ): string {
    let obj = _.assignIn(parameters, body);
    let signTarget = SignatureGenerator.createSignTarget(method, path, timestamp, nonce, obj);
    if (obj && _.size(obj) > 0) {
      signTarget += RequestBodyFlattener.flatten(obj);
    }
    logger.debug(`signature-target: ${signTarget}`)
    let hash = CryptoJS.HmacSHA512(signTarget, apiSecret);
    return CryptoJS.enc.Base64.stringify(hash);
  }

  private static createSignTarget(
    method: string,
    path: string,
    timestamp: number,
    nonce: string,
    parameters: object = {}
  ) {
    let signTarget = `${nonce}${timestamp}${method}${path}`;
    if (parameters && _.size(parameters) > 0) {
      if (signTarget.indexOf('?') < 0) {
        signTarget += '?'
      } else {
        signTarget += '&'
      }
    }
    return signTarget;
  }
}
