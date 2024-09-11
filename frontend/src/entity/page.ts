export default interface Page<T> {
    content: T[],
    totalElements: number,
    totalPages: number,
    sort: {
        empty: boolean,
        sorted: boolean,
        unsorted: boolean
    }
    last: boolean
    first: boolean
}