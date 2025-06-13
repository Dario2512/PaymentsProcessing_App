export interface TransactionModel {
    id: number,
    amount: number,
    cardNumber: string,
    returnCode: number,
    timestamp: string
}
export interface TransactionAddModel {
    cardNumber: string,
    amount: number,
    timestamp: string
}
