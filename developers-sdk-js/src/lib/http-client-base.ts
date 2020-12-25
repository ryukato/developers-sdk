import { LoggerFactory } from "./logger-factory";
import axios, { AxiosInstance, AxiosResponse, AxiosRequestConfig } from 'axios';
import cryptoRandomString from 'crypto-random-string';
import {
  GenericResponse,
  ServiceDetail,
  ServiceToken,
  TxResultResponse,
  ServiceTokenHolder,
  ItemToken,
  FungibleToken,
  FungibleTokenHolder,
  ItemTokenType,
  NonFungibleTokenType,
  NonFungibleId,
  NonFungibleTokenTypeHolder,
  NonFungibleTokenHolder
} from './response';
import {
  AbstractTransactionRequest,
  AbstractItemTokenBurnTransactionRequest,
  UpdateServiceTokenRequest,
  MintServiceTokenRequest,
  BurnServiceTokenRequest,
  PageRequest,
  FungibleTokenCreateUpdateRequest,
  FungibleTokenMintRequest,
  FungibleTokenBurnRequest,
  NonFungibleTokenCreateUpdateRequest,
  NonFungibleTokenMintRequest,
  NonFungibleTokenMultiMintRequest,
  NonFungibleTokenBurnRequest,
  NonFungibleTokenAttachRequest
} from './request';
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
    this.instance.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8"
    this.instance.defaults.headers.put["Content-Type"] = "application/json;charset=UTF-8"

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

  private _handleResponse = ({ data }: AxiosResponse) => {
    this.logger.debug(`response: ${JSON.stringify(data)}`);
    return data
  };
  protected _handleError = (error: any) => Promise.reject(error);

  private _handleRequest = (config: AxiosRequestConfig) => {
    this.addRequestHeaders(config)
    this.logger.debug(`headers: ${JSON.stringify(config.headers)}`)
    if (config.data) {
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
      SingtureGenerator.signature(this.serviceApiSecret, method, config.url, timestamp, nonce, config.params, config.data);
    config.headers[Constant.TIMESTAMP_HEADER] = timestamp
  }

  public async time(): Promise<GenericResponse<void>> {
    const response = await this.instance.get("/v1/time");
    return response;
  }

  public async serviceDetail(serviceId: string): Promise<GenericResponse<ServiceDetail>> {
    const path = `/v1/services/${serviceId}`;
    const response = await this.instance.get(path);
    return response;
  }

  public async serviceTokens(): Promise<GenericResponse<Array<ServiceToken>>> {
    const response = await this.instance.get(`/v1/service-tokens`);
    return response;
  }

  public async serviceTokenDetail(contractId: string): Promise<GenericResponse<ServiceToken>> {
    const path = `/v1/service-tokens/${contractId}`;
    const response = await this.instance.get(path);
    return response;
  }

  public async updateServiceToken(
    contractId: string,
    request: UpdateServiceTokenRequest): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/service-tokens/${contractId}`;
    const response = await this.instance.put(path, request);
    return response;
  }

  public async mintServiceToken(
    contractId: string,
    request: MintServiceTokenRequest): Promise<GenericResponse<TxResultResponse>> {
    this.assertTransactionRequest(request)
    const path = `/v1/service-tokens/${contractId}/mint`;
    const response = await this.instance.post(path, request);
    return response;
  }

  public async burnServiceToken(
    contractId: string,
    request: BurnServiceTokenRequest): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/service-tokens/${contractId}/burn`;
    const response = await this.instance.post(path, request);
    return response;
  }

  public async serviceTokenHolders(
    contractId: string,
    pageRequest: PageRequest
  ): Promise<GenericResponse<Array<ServiceTokenHolder>>> {
    const path = `/v1/service-tokens/${contractId}/holders`;
    const requestConfig = this.pageRequestConfig(pageRequest);
    const response = await this.instance.get(path, requestConfig);
    return response;
  }

  public async itemToken(contractId: string): Promise<GenericResponse<ItemToken>> {
    const path = `/v1/item-tokens/${contractId}`;
    const response = await this.instance.get(path);
    return response;
  }

  public async fungibleTokens(
    contractId: string,
    pageRequest: PageRequest
  ): Promise<GenericResponse<Array<FungibleToken>>> {
    const path = `/v1/item-tokens/${contractId}/fungibles`;
    const requestConfig = this.pageRequestConfig(pageRequest);
    const response = await this.instance.get(path, requestConfig);
    return response;
  }

  public async createFungibleToken(
    contractId: string,
    request: FungibleTokenCreateUpdateRequest): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/fungibles`;
    const response = await this.instance.post(path, request);
    return response;
  }

  public async fungibleToken(
    contractId: string,
    tokenType: string
  ): Promise<GenericResponse<FungibleToken>> {
    const path = `/v1/item-tokens/${contractId}/fungibles/${tokenType}`;
    return await this.instance.get(path);
  }

  public async updateFungibleToken(
    contractId: string,
    tokenType: string,
    request: FungibleTokenCreateUpdateRequest): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/fungibles/${tokenType}`;
    const response = await this.instance.put(path, request);
    return response;
  }

  public async mintFungibleToken(
    contractId: string,
    tokenType: string,
    request: FungibleTokenMintRequest): Promise<GenericResponse<TxResultResponse>> {
    this.assertTransactionRequest(request)
    const path = `/v1/item-tokens/${contractId}/fungibles/${tokenType}/mint`;
    const response = await this.instance.post(path, request);
    return response;
  }

  public async burnFungibleToken(
    contractId: string,
    tokenType: string,
    request: FungibleTokenBurnRequest): Promise<GenericResponse<TxResultResponse>> {
    this.assertItemTokenBurnTransactionRequest(request)
    const path = `/v1/item-tokens/${contractId}/fungibles/${tokenType}/burn`;
    const response = await this.instance.post(path, request);
    return response;
  }

  public async fungibleTokenHolders(
    contractId: string,
    tokenType: string,
    pageRequest: PageRequest
  ): Promise<GenericResponse<Array<FungibleTokenHolder>>> {
    const path = `/v1/item-tokens/${contractId}/fungibles/${tokenType}/holders`;
    const requestConfig = this.pageRequestConfig(pageRequest);
    const response = await this.instance.get(path, requestConfig);
    return response;
  }

  public async nonFungibleTokens(
    contractId: string,
    pageRequest: PageRequest): Promise<GenericResponse<Array<ItemTokenType>>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles`;
    const requestConfig = this.pageRequestConfig(pageRequest);
    const response = await this.instance.get(path,requestConfig);
    return response;
  }

  public async createNonFungibleToken(
    contractId: string,
    request: NonFungibleTokenCreateUpdateRequest
  ): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles`;
    const response = await this.instance.post(path, request);
    return response;
  }

  public async nonFungibleTokenType(
    contractId: string,
    tokenType: string,
    pageRequest: PageRequest
  ): Promise<GenericResponse<NonFungibleTokenType>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}`;
    const requestConfig = this.pageRequestConfig(pageRequest);
    const response = await this.instance.get(path, requestConfig);
    return response;
  }

  public async updateNonFungibleTokenType(
    contractId: string,
    tokenType: string,
    request: NonFungibleTokenCreateUpdateRequest
  ): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}`;
    const response = await this.instance.put(path, request);
    return response;
  }

  public async nonFungibleToken(
    contractId: string,
    tokenType: string,
    tokenIndex: string
  ): Promise<GenericResponse<NonFungibleId>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}`;
    return await this.instance.get(path);
  }

  public async updateNonFungibleToken(
    contractId: string,
    tokenType: string,
    tokenIndex: string,
    request: NonFungibleTokenCreateUpdateRequest
  ): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}`;
    const response = await this.instance.put(path, request);
    return response;
  }

  public async mintNonFungibleToken(
    contractId: string,
    tokenType: string,
    tokenIndex: string,
    request: NonFungibleTokenMintRequest
  ): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}/mint`
    const response = await this.instance.post(path, request);
    return response;
  }

  public async nunFungibleTokenTypeHolders(
    contractId: string,
    tokenType: string,
    pageRequest: PageRequest
  ): Promise<GenericResponse<NonFungibleTokenTypeHolder>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/holders`;
    const requestConfig = this.pageRequestConfig(pageRequest);
    return await this.instance.get(path, requestConfig);
  }

  // NFT has to belong to only one holder
  public async nunFungibleTokenHolder(
    contractId: string,
    tokenType: string,
    tokenIndex: string
  ): Promise<GenericResponse<NonFungibleTokenHolder>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}/holder`
    return await this.instance.get(path);
  }

  public async multiMintnonFungibleToken(
    contractId: string,
    request: NonFungibleTokenMultiMintRequest
  ): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/multi-mint`
    const response = await this.instance.post(path, request);
    return response;
  }

  public async burnNonFungibleToken(
    contractId: string,
    tokenType: string,
    tokenIndex: string,
    request: NonFungibleTokenBurnRequest
  ): Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}/burn`
    const response = await this.instance.post(path, request);
    return response;
  }

  public async childrenOfNonFungibleToken(
    contractId: string,
    tokenType: string,
    tokenIndex: string,
    pageRequest: PageRequest
  ): Promise<GenericResponse<Array<NonFungibleId>>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}/children`
    const requestConfig = this.pageRequestConfig(pageRequest);
    return await this.instance.get(path, requestConfig);
  }

  public async parentOfNonFungibleToken(
    contractId: string,
    tokenType: string,
    tokenIndex: string
  ) : Promise<GenericResponse<Array<NonFungibleId>>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}/parent`
    return await this.instance.get(path);
  }

  public async attachNonFungibleToken(
    contractId: string,
    tokenType: string,
    tokenIndex: string,
    request: NonFungibleTokenAttachRequest
  ) : Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}/parent`
    const response = await this.instance.post(path, request);
    return response;
  }

  public async detachNonFungibleToken(
    contractId: string,
    tokenType: string,
    tokenIndex: string
  ) : Promise<GenericResponse<TxResultResponse>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}/parent`
    const response = await this.instance.delete(path);
    return response;
  }

  public async rootOfNonFungibleToken(
    contractId: string,
    tokenType: string,
    tokenIndex: string
  ) : Promise<GenericResponse<Array<NonFungibleId>>> {
    const path = `/v1/item-tokens/${contractId}/non-fungibles/${tokenType}/${tokenIndex}/root`
    return await this.instance.get(path);
  }

  private pageRequestConfig(pageRequest: PageRequest): AxiosRequestConfig {
    return {
      "params": {
        "limit": pageRequest.limit,
        "page": pageRequest.page,
        "orderBy": pageRequest.orderBy
      }
    };
  }

  private assertTransactionRequest(request: AbstractTransactionRequest) {
    if (!request.toUserId && !request.toAddress) {
      this.logger.error("toAddress or toUserId, one of them is required");
      throw new Error("toAddress or toUserId, one of them is required")
    }
  }

  private assertItemTokenBurnTransactionRequest(request: AbstractItemTokenBurnTransactionRequest) {
    if (!request.fromUserId && !request.fromAddress) {
      this.logger.error("fromAddress or fromUserId, one of them is required");
      throw new Error("fromAddress or fromUserId, one of them is required")
    }
  }
}
