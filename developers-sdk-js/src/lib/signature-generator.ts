import CryptoJS from 'crypto-js';
import _ from "lodash";
import { RequestBodyFlattener } from "./request-body-flattener";
/**
reference site: https://jokecamp.wordpress.com/2012/10/21/examples-of-creating-base64-hashes-using-hmac-sha256-in-different-languages/#js
*/
export class SingtureGenerator {
  static signature(
    apiSecret: string,
    method: string,
    path: string,
    timestamp: number,
    nonce: string,
    parameters: object = {} // query string or request body
  ): string {
    let signTarget = SingtureGenerator.createSignTarget(method, path, timestamp, nonce, parameters);
    if (parameters && _.size(parameters) > 0) {
      signTarget += RequestBodyFlattener.flatten(parameters);
    }
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
