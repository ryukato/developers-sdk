import { LoggerFactory } from "./logger-factory";
import axios, { AxiosInstance, AxiosResponse, AxiosRequestConfig } from 'axios';
import cryptoRandomString from 'crypto-random-string';
import { GenericResponse, ServiceDetail, ServiceToken, TxResultResponse, ServiceTokenHolder } from './response';
import { UpdateServiceTokenRequest, MintServiceTokenRequest, BurnServiceTokenRequest, OrderBy } from './request';
import { SingtureGenerator } from './signature-generator';
import { Constant } from './constants';

declare module 'axios' {
  interface AxiosResponse<T = any> extends Promise<T> { }
}

export class HttpClient {
  private logger = LoggerFactory.logger("HttpClient");

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

  // for test
  public getAxiosInstance(): AxiosInstance {
    return this.instance;
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
    this.logger.debug(`headers: ${JSON.stringify(config.headers)}`)
    if(config.data) {
        this.logger.debug(`body: ${JSON.stringify(config.data)}`)
    }
    if (config.params) {
      this.logger.debug(`query-params: ${JSON.stringify(config.params)}`)
    }
    return config;
  }

  protected addRequestHeaders(config: AxiosRequestConfig) {
    const nonce = cryptoRandomString({ length: 8 });
    const timestamp = Date.now()
    const method = config.method.toUpperCase()
    config.headers[Constant.SERVICE_API_KEY_HEADER] = this.serviceApiKey;
    config.headers[Constant.NONCE_HEADER] = nonce;
    config.headers[Constant.SIGNATURE_HEADER] =
      SingtureGenerator.signature(this.serviceApiSecret, method, config.url, timestamp, nonce, config.params);
    config.headers[Constant.TIMESTAMP_HEADER] = timestamp
  }

  public async time(): Promise<GenericResponse<void>> {
    const response = await this.instance.get("/v1/time");
    this.logger.debug(`response: ${JSON.stringify(response)}`);
    return response;
  }

  public async serviceDetail(serviceId: string): Promise<GenericResponse<ServiceDetail>> {
    const response = await this.instance.get(`/v1/services/${serviceId}`);
    this.logger.debug(`response: ${JSON.stringify(response)}`);
    return response;
  }

  public async serviceTokens(): Promise<GenericResponse<Array<ServiceToken>>> {
    const response = await this.instance.get(`/v1/service-tokens`);
    this.logger.debug(`response: ${JSON.stringify(response)}`);
    return response;
  }

  public async serviceTokenDetail(contractId: string): Promise<GenericResponse<ServiceToken>> {
    const response = await this.instance.get(`/v1/service-tokens/${contractId}`);
    this.logger.debug(`response: ${JSON.stringify(response)}`);
    return response;
  }

  public async updateServiceToken(
    contractId: string,
    updateServiceTokenRequest: UpdateServiceTokenRequest): Promise<GenericResponse<TxResultResponse>> {
    const response =
      await this.instance.put(`/v1/service-tokens/${contractId}`, updateServiceTokenRequest);
    this.logger.debug(`response: ${JSON.stringify(response)}`);
    return response;
  }

  public async mintServiceToken(
    contractId: string,
    mintServiceTokenRequest: MintServiceTokenRequest): Promise<GenericResponse<TxResultResponse>> {
    const response =
      await this.instance.post(`/v1/service-tokens/${contractId}/mint`, mintServiceTokenRequest);
    this.logger.debug(`response: ${JSON.stringify(response)}`);
    return response;
  }

  public async burnServiceToken(
    contractId: string,
    burnServiceTokenRequest: BurnServiceTokenRequest): Promise<GenericResponse<TxResultResponse>> {
    const response =
      await this.instance.post(`/v1/service-tokens/${contractId}/burn`, burnServiceTokenRequest);
    this.logger.debug(`response: ${JSON.stringify(response)}`);
    return response;
  }

  public async serviceTokenHolders(
    contractId: string,
    limit: number = 10,
    page: number = 0,
    orderBy: OrderBy = OrderBy.ASC
  ): Promise<GenericResponse<Array<ServiceTokenHolder>>> {
    const response = await this.instance.get(`/v1/service-tokens/${contractId}/holders`, {
      "params": {
        "limit": limit,
        "page": page,
        "orderBy": orderBy
      }
    });
    this.logger.debug(`response: ${JSON.stringify(response)}`);
    return response;
  }
}
