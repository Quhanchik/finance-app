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
import { set } from "date-fns";

export default function BillsPage() {
    const [searchParams, setSerachParams] = useSearchParams();
    const [paginatedBills, setPaginatedBills] = useState<Page<Bill>>();
    const [page, setPage] = useState<number>(Number(searchParams.get('page') || 1));
    const [size, setSize] = useState<number>(10);
    const [createName, setCreateName] = useState<string>('');
    const [createDescription, setCreateDescription] = useState<string>('');
    const [createTotalMoney, setCreateTotalMoney] = useState<number>(0);

    const [isDeleteBillModalOpen, setIsDeleteBillModalOpen] = useState(false);

    const [joinToken, setJoinToken] = useState<string>('');

    const navigate = useNavigate();

    const BACKEND_URL_BASE = import.meta.env.VITE_BACKEND_URL_BASE

    const [currentBillId, setCurrentBillId] = useState<number>();

    useEffect(() => {
        searchParams.set('page', page.toString());
        setSerachParams(searchParams);

        fetch(BACKEND_URL_BASE + '/bills?page=' + (page - 1) + '&size=' + size, {
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
                        <Card id={bill.id.toString()} className="hover:cursor-pointer hover:scale-105 duration-150">
                            <CardHeader onClick={() => navigate(`/bills/${bill.id}`)} >
                                <CardTitle>{bill.name}</CardTitle>
                            </CardHeader>
                            <CardContent   onClick={() => navigate(`/bills/${bill.id}`)} >
                                <div className={`text-3xl ${Number(bill?.totalMoney) < 0 ? 'text-red-500' : 'text-green-500'}`}>{`$${bill.totalMoney}`}</div>
                            </CardContent>
                            <CardFooter  className="flex items-center justify-between">
                                <div className="flex">
                                    <div className="mr-2">
                                        {countSpell(bill.members.length)}
                                    </div> 
                                    <Eye className="h-5 w-5 mt-1"/>
                                </div>
                                <Button id={bill.id.toString()} variant="destructive" onClick={() => {setIsDeleteBillModalOpen(true); setCurrentBillId(bill.id)}} className="ml-4 z-99">delete bill</Button>
                            </CardFooter>
                        </Card>
                )
            })

            return <div className="grid gap-4 xl:grid-cols-4 lg:grid-cols-3 md:grid-cols-2">
                {renderedBills}
            </div>
        }
    }

    const onCreateSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        fetch(BACKEND_URL_BASE + '/bills', {
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

        fetch(BACKEND_URL_BASE + `/bills/join-token`, {
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

    const deleteBill = (e) => {
        e.preventDefault();

        fetch(BACKEND_URL_BASE + `/bills/${currentBillId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + Cookies.get('token')
            }
        }).then(res => {
            if(res.status === 200) {
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
                        <Dialog open={isDeleteBillModalOpen} onOpenChange={setIsDeleteBillModalOpen}>
                            <DialogContent>
                                <div>Are you sure that you want to delete this bill?</div>
                                <div className="mt-5 flex justify-end gap-2">
                                    <Button variant="outline" onClick={() => setIsDeleteBillModalOpen(false)}>Close</Button>
                                    <Button className="bg-red-500" onClick={deleteBill}>delete</Button>
                                </div>
                            </DialogContent>
                        </Dialog>
                    </div>
                </div>
                <div >
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