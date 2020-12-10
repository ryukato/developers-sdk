export class GenericResponse<T> {
  constructor(readonly responseTime: number, readonly statusCode: number, readonly statusMessage: string, readonly responseData: T){}
}
