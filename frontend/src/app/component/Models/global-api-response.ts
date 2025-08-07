export interface GlobalAPIResponse<T> {
  message: string;
  success: boolean;
  data: T;
}
