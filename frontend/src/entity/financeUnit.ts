import User from "./user"
import Category from "./category"

export default interface FinanceUnit {
    id: number
    name: string
    description: string
    category: Category
    creator: User
    money: number
    timestamp: Date
}