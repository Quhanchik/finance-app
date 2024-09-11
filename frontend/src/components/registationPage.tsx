
import { Link, useNavigate } from 'react-router-dom'
import { Card, CardContent, CardFooter } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import Cookies from 'js-cookie'
import { FormEvent, useContext, useState } from 'react'

export default function RegistrationPage() {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(false);
    const BACKEND_URL_BASE = import.meta.env.VITE_BACKEND_URL_BASE
    const navigate = useNavigate();

    const onSubmit = (e: FormEvent) => {
        e.preventDefault();
        
        fetch(BACKEND_URL_BASE + '/auth/registration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                login: login,
                password: password
            })
        }).then((response) => {
            if(response.status === 200) {
                response.json().then(data => {
                    localStorage.setItem('user', JSON.stringify(data.user));
                    Cookies.set('token', data.accessToken);

                    navigate('/bills');
                })
            } else {
                setError(true);
            }
        })
    }

    return (
        <div className="flex items-center justify-center bg-background px-4 py-12 sm:px-6 lg:px-8">
      <div className="mx-auto w-full max-w-md space-y-6">
        <div className="text-center">
          <h2 className="text-3xl font-bold tracking-tight text-foreground">Sign in to your account</h2>
          <p className="mt-2 text-muted-foreground">
            Already have an account?{" "}
            <Link to='/login' className="font-medium text-primary hover:underline">
              Login
            </Link>
          </p>
        </div>
        <form onSubmit={(e) => onSubmit(e)}>
            <Card>
            <CardContent className="space-y-4">
                <div className="space-y-2">
                <Label>login</Label>
                <Input id="login" onChange={(e) => setLogin(e.target.value)} />
                </div>
                <div className="space-y-2">
                <div className="flex items-center justify-between">
                    <Label htmlFor="password">Password</Label>
                </div>
                <Input id="password" type="password" onChange={(e) => setPassword(e.target.value)}/>
                </div>
                {error && <p className="text-red-500">Wrong login or password</p>}
            </CardContent>
            <CardFooter>
                <Button className="w-full">Sign up</Button>
            </CardFooter>
            </Card>
        </form>
      </div>
    </div>
    )
}