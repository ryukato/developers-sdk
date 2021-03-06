import _ from "lodash";
export class AbstractItemTokenBurnTransactionRequest {

  constructor(
    readonly fromUserId?: string,
    readonly fromAddress?: string
  ) {
    if (!fromUserId && !fromAddress) {
      throw new Error("fromAddress or fromUserId, one of them is required")
    }
  }
}

export class AbstractTransactionRequest {
  constructor(
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    if (!toUserId && !toAddress) {
      throw new Error("toAddress or toUserId, one of them is required")
    }
  }
}


export class UpdateServiceTokenRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly name: string,
    readonly meta?: string
  ) { }
}

export class BurnServiceTokenRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly amount: string
  ) { }
}

export class MintServiceTokenRequest extends AbstractTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly amount: string,
    toAddress?: string,
    toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class MemoRequest {
  constructor(
    readonly memo: string,
    readonly walletAddress: string,
    readonly walletSecret: string
  ) { }
}

export class TransferBaseCoinRequest extends AbstractTransactionRequest {
  constructor(
    readonly walletSecret: string,
    readonly amount: string,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class TransferServiceTokenRequest extends AbstractTransactionRequest {
  constructor(
    readonly walletSecret: string,
    readonly amount: string,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class TransferServiceTokenProxyRequest extends AbstractTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly amount: string,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class TransferFungibleTokenRequest extends AbstractTransactionRequest {
  constructor(
    readonly walletSecret: string,
    readonly amount: string,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class TransferFungibleTokenProxyRequest extends AbstractTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly amount: string,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class TransferNonFungibleTokenRequest extends AbstractTransactionRequest {
  constructor(
    readonly walletSecret: string,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class TransferNonFungibleTokenProxyRequest extends AbstractTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class BatchTransferNonFungibleTokenRequest extends AbstractTransactionRequest {
  constructor(
    readonly walletSecret: string,
    readonly transferList: Array<TokenId>,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class BatchTransferNonFungibleTokenProxyRequest extends AbstractTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly transferList: Array<TokenId>,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}


export class TokenId {
  private constructor(readonly tokenId: string) { }
  private static tokenIdFormat = new RegExp("\\d{8}\\d{8}")
  static from(value: string): TokenId {
    if (value.length != 16) {
      throw new Error("Invalid tokenId: length of token-id has to be 16")
    }
    if (!TokenId.tokenIdFormat.test(value)) {
      throw new Error(`Invalid tokenId: invalid format of tokenId, valid format is ${this.tokenIdFormat}`)
    }
    return new TokenId(value);
  }

  static fromMulti(values: Array<string>): Array<TokenId> {
      return _.map(values, (value => TokenId.from(value)))
  }
}

export class FungibleTokenCreateUpdateRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly name: string,
    readonly meta?: string
  ) { }
}

export class FungibleTokenMintRequest extends AbstractTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly amount: string,
    readonly toAddress?: string,
    readonly toUserId?: string,
  ) {
    super(toAddress, toUserId);
  }
}

export class FungibleTokenBurnRequest extends AbstractItemTokenBurnTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly amount: string,
    readonly fromUserId?: string,
    readonly fromAddress?: string,
  ) {
    super(fromUserId, fromAddress);
  }
}

export class NonFungibleTokenCreateUpdateRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly name: string,
    readonly meta?: string
  ) { }
}

export class NonFungibleTokenMintRequest extends AbstractTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly name: string,
    readonly meta: string,
    readonly toAddress?: string,
    readonly toUserId?: string,
  ) {
    super(toAddress, toUserId);
  }
}

export class NonFungibleTokenMultiMintRequest extends AbstractTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly mintList: Array<MultiMintNonFungible>,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
  }
}

export class MultiMintNonFungible {
  constructor(
    readonly tokenType: string,
    readonly name: string,
    readonly meta?: string
  ) { }
}

export class NonFungibleTokenBurnRequest extends AbstractItemTokenBurnTransactionRequest {
  constructor(
    readonly ownerAddress: string,
    readonly ownerSecret: string,
    readonly fromUserId?: string,
    readonly fromAddress?: string
  ) {
    super(fromUserId, fromAddress);
  }
}

export class NonFungibleTokenAttachRequest {
  constructor(
    readonly parentTokenId: string,
    readonly serviceWalletAddress: string,
    readonly serviceWalletSecret: string,
    readonly tokenHolderAddress?: string,
    readonly tokenHolderUserId?: string
  ) {
    if (tokenHolderAddress != null || tokenHolderUserId != null) {
      throw new Error("tokenHolderAddress or tokenHolderUserId, one of them is required")
    }
  }
}

export class NonFungibleTokenDetachRequest {
  constructor(
    readonly serviceWalletAddress: string,
    readonly serviceWalletSecret: string,
    readonly tokenHolderAddress: string = null,
    readonly tokenHolderUserId: string = null
  ) {
    if (tokenHolderAddress != null || tokenHolderUserId != null) {
      throw new Error("tokenHolderAddress or tokenHolderUserId, one of them is required")
    }
  }
}

export class UserServiceTokenTransferRequest extends AbstractTransactionRequest {
  constructor(
    readonly amount: string,
    readonly toAddress: string = null,
    readonly toUserId: string = null,
    readonly landingUri: string
  ) {
    super(toAddress, toUserId);
    if (Number(amount) <= 0) {
      throw new Error("Invalid amount - $amount is less than zero ")
    }
  }
}

export class IssueTransferSessionTokenRequest extends AbstractTransactionRequest {
  constructor(
    readonly amount: string,
    readonly landingUri: string,
    readonly toAddress?: string,
    readonly toUserId?: string
  ) {
    super(toAddress, toUserId);
    if (Number(amount) <= 0) {
      throw new Error("Invalid amount - $amount is less than zero ")
    }
  }
}

export class UserProxyRequest {
  constructor(
    readonly ownerAddress: string,
    readonly landingUri: string
  ) { }
}

export enum OrderBy {
  ASC = "asc", DESC = "desc"
}

export enum RequestType {
  REDIRECT_URI = "redirect_uri", AOA = "aoa"
}

export class PageRequest {
  constructor(
    readonly page: number = 0,
    readonly limit: number = 10,
    readonly orderBy: OrderBy = OrderBy.ASC) { }
}
