import { BrowserRouter, Navigate, redirect, Route, Routes } from "react-router-dom"
import LoginPage from "./components/loginPage"
import '@/index.css'
import ReportsPage from "./components/reportsPage"
import BillsPage from "./components/billsPage"
import Header from "./components/header"
import RegistrationPage from "./components/registationPage"
import { useEffect } from "react"
import { useAuthStore } from "./util/authStore"
import Cookies from "js-cookie"
function App() {
  const [isAuth, setIsAuth] = useAuthStore((state) => [state.isAuth, state.setIsAuth])

  useEffect(() => {
    setIsAuth(!!Cookies.get('token'))
  }, [])
  

  return (
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/registration" element={<RegistrationPage />} />
          <Route path="/bills/:billId" element={<ReportsPage />} />
          <Route path="/bills" element={<BillsPage />} />
        </Routes>
      </BrowserRouter>
    
  )
}

export default App
