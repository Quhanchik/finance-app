import Auth from "@/entity/auth";
import {create} from "zustand"

export const useAuthStore = create<Auth>((set) =>( {
    isAuth: false,
    setIsAuth: (auth: boolean) => set(() => ({isAuth: auth})),
}));