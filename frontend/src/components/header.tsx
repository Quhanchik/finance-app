
import { Cookie, MountainIcon } from "lucide-react";
import { Button } from "./ui/button";
import { useAuthStore } from "@/util/authStore";
import { Link, useNavigate } from "react-router-dom";
import { Dropdown } from "react-day-picker";
import { DropdownMenu, DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem, DropdownMenuTrigger } from "./ui/dropdown-menu";
import Cookies from "js-cookie";

export default function Header() {
  const [isAuth, setIsAuth] = useAuthStore((state) => [state.isAuth, state.setIsAuth]);
  // const isAuth = true
  const navigate = useNavigate()

  const logoutHandler = () => {
    Cookies.remove('token')
    setIsAuth(false)
    navigate('/login')
  }

    return (
        <header className="bg-primary text-primary-foreground py-4 px-6 flex items-center justify-between">
            <div className="flex gap-2">
              <MountainIcon className="h-6 w-6" />
              <span className="text-lg font-semibold">Finance Manager</span>
            </div>
              <div>
                  {
                    isAuth ?
                    <DropdownMenu>
                      <DropdownMenuTrigger>
                        {JSON.parse(window.localStorage.getItem('user')).login}
                      </DropdownMenuTrigger>
                      <DropdownMenuContent>
                        <DropdownMenuGroup>
                          <DropdownMenuItem onClick={logoutHandler}>logout</DropdownMenuItem>
                        </DropdownMenuGroup>
                      </DropdownMenuContent>
                    </DropdownMenu> :
                    <div>
                        <Button><Link to="/login">login</Link></Button>
                        <Button><Link to="/register">register</Link></Button>
                    </div>
                  }
              </div>
          </header>
    )
}