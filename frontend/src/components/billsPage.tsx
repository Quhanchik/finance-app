import Bill from "@/entity/bill";
import Page from '@/entity/page';
import countSpell from "@/util/countSpell";
import { DialogTrigger } from "@radix-ui/react-dialog";
import Cookies from "js-cookie";
import { Eye } from "lucide-react";
import { useEffect, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { Button } from "./ui/button";
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "./ui/card";
import { Dialog, DialogContent } from "./ui/dialog";
import { Input } from "./ui/input";
import { Pagination, PaginationContent, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious } from "./ui/pagination";

export default function BillsPage() {
    const [searchParams, setSerachParams] = useSearchParams();
    const [paginatedBills, setPaginatedBills] = useState<Page<Bill>>();
    const [page, setPage] = useState<number>(Number(searchParams.get('page') || 1));
    const [size, setSize] = useState<number>(10);
    const [createName, setCreateName] = useState<string>('');
    const [createDescription, setCreateDescription] = useState<string>('');
    const [createTotalMoney, setCreateTotalMoney] = useState<number>(0);

    const [joinToken, setJoinToken] = useState<string>('');

    const navigate = useNavigate();

    const BACKEND_URL_BASE = import.meta.env.VITE_BACKEND_URL_BASE

    useEffect(() => {
        searchParams.set('page', page.toString());
        setSerachParams(searchParams);

        fetch(BACKEND_URL_BASE + '/bill?page=' + (page - 1) + '&size=' + size, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + Cookies.get('token')
            }
        }).then(res => {
            if(res.status === 200) {
                res.json().then(data => {
                    setPaginatedBills(data);
                })
            } else {
                console.log('error');
            }
        })
    }, [])

    const renderListOfBills = () => {
        if(paginatedBills) { 
            const renderedBills = paginatedBills.content.map(bill => {
                return (
                    <Link to={`/bills/${bill.id}`} key={bill.id}>
                        <Card className="max-w-200 hover:cursor-pointer hover:scale-105 duration-150">
                            <CardHeader>
                                <CardTitle>{bill.name}</CardTitle>
                            </CardHeader>
                            <CardContent>
                                <div className={`text-3xl ${Number(bill?.totalMoney) < 0 ? 'text-red-500' : 'text-green-500'}`}>{`$${bill.totalMoney}`}</div>
                            </CardContent>
                            <CardFooter  className="flex items-center justify-between">
                                <div className="flex">
                                    <div className="mr-2">
                                        {countSpell(bill.members.length)}
                                    </div> 
                                    <Eye className="h-5 w-5 mt-1"/>
                                </div>
                                <Button className="ml-4">edit bill</Button>
                            </CardFooter>
                        </Card>
                    </Link>
                )
            })

            return <div className="grid grid-cols-5 gap-4">
                {renderedBills}
            </div>
        }
    }

    const onCreateSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        fetch(BACKEND_URL_BASE + '/bill', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + Cookies.get('token')
            },
            body: JSON.stringify({
                name: createName,
                description: createDescription,
                totalMoney: createTotalMoney
            })
        }).then(res => {
            if(res.status === 200) {
                navigate(0);
            } else {
                console.log('error');
            }
        })
    }

    const onJoinSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        fetch(BACKEND_URL_BASE + `/bill/join-token`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + Cookies.get('token')
            },
            body: JSON.stringify({
                joinToken
            })
        }).then(res => {
            if(res.status === 201) {
                navigate(0);
            } else {
                console.log('error');
            }
        })
    }

    return (
        <div className="h-full w-full p-20 flex flex-col justify-between">
            <div>
                <div className="flex justify-between">
                    <div className="text-3xl mb-4">Your bills</div>
                    <div className="flex gap-4">
                        <Dialog>
                            <DialogTrigger>
                                <Button>join</Button>
                            </DialogTrigger>
                            <DialogContent>
                                <form onSubmit={onJoinSubmit} className="grid gap-4 p-4 pb-0">
                                    <Input onChange={(e) => setJoinToken(e.target.value)} value={joinToken} type="text" placeholder="link" />
                                    <Button className="mt-4">join</Button>
                                </form>
                            </DialogContent>
                        </Dialog>
                        <Dialog>
                            <DialogTrigger>
                                <Button>create</Button>
                            </DialogTrigger>
                            <DialogContent>
                                <form onSubmit={onCreateSubmit} className="grid gap-4 p-4 pb-0">
                                    <Input onChange={(e) => setCreateName(e.target.value)} type="text" placeholder="name" />
                                    <Input onChange={(e) => setCreateDescription(e.target.value)} type="text" placeholder="description"  />
                                    <Input onChange={(e) => setCreateTotalMoney(Number(e.target.value))} type="number" placeholder="total money" />
                                    <Button className="mt-4">create</Button>
                                </form>
                            </DialogContent>
                        </Dialog>
                    </div>
                </div>
                <div className="grid justify-start gap-6 grid-flow-col">
                    {renderListOfBills()}
                </div>
            </div>
            <div className="flex flex-col justify-between">
                <div></div>
                <Pagination>
                    <PaginationContent>
                        <PaginationItem>
                        <PaginationPrevious className={page <= 1 ? 'pointer-events-none text-gray-400' : ''} href={`/bills?page=${page - 1}`} />
                        </PaginationItem>
                        <PaginationItem>
                        <PaginationLink>{`${page} - ${paginatedBills?.totalPages}`}</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                           {paginatedBills ? 
                            <PaginationNext className={page >= paginatedBills.totalPages ? 'pointer-events-none text-gray-400' : ''} href={`/bills?page=${page + 1}`} />
                            :
                            <PaginationNext className="pointer-events-none text-gray-400" />
                            }
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>
                <div></div>
            </div>
        </div>
    )
}