import axios, { AxiosInstance, AxiosResponse, AxiosRequestConfig } from 'axios';
import cryptoRandomString from 'crypto-random-string';
import { GenericResponse } from './response';
import { SingtureGenerator } from './signature-generator';

declare module 'axios' {
  interface AxiosResponse<T = any> extends Promise<T> { }
}

const SERVICE_API_KEY_HEADER = "service-api-key"
const SIGNATURE = "Signature"
const TIMESTAMP = "Timestamp"
const NONCE = "Nonce"

export class HttpClient {
  protected readonly instance: AxiosInstance;
  private readonly serviceApiKey: string;
  private readonly serviceApiSecret: string;
  public constructor(
    baseURL: string,
    apiKey: string,
    apiSecret: string
  ) {
    this.instance = axios.create({
      baseURL,
    });
    this.serviceApiKey = apiKey
    this.serviceApiSecret = apiSecret

    this._initialzeResponseInterceptor();
  }

  private _initialzeResponseInterceptor = () => {
    this.instance.interceptors.response.use(
      this._handleResponse,
      this._handleError,
    );
    this.instance.interceptors.request.use(
      this._handleRequest,
      this._handleError,
    );
  };

  private _handleResponse = ({ data }: AxiosResponse) => data;
  protected _handleError = (error: any) => Promise.reject(error);

  private _handleRequest = (config: AxiosRequestConfig) => {
    this.addRequestHeaders(config)
    return config;
  }

  protected addRequestHeaders(config: AxiosRequestConfig) {
    const nonce = cryptoRandomString({ length: 8 });
    const timestamp = Date.now()
    const method = config.method.toUpperCase()
    config.headers[SERVICE_API_KEY_HEADER] = this.serviceApiKey;
    config.headers[NONCE] = nonce;
    config.headers[SIGNATURE] =
      SingtureGenerator.signature(this.serviceApiSecret, method, config.url, timestamp, nonce, config.params);
    config.headers[TIMESTAMP] = timestamp
  }

  public async time(): Promise<GenericResponse<void>> {
    return await this.instance.get("/v1/time")
  }
}
