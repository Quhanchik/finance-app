import User from "./user"

export default interface Bill {
    id: number
    name: string
    totalMoney: number
    description: string
    members: User[]
    creator: User
    totalExpenses: number
    totalIncome: number
}