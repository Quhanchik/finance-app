import { CalendarClockIcon, CheckCheckIcon, EditIcon, FilePenIcon, FileQuestionIcon, FilesIcon, MountainIcon, PlusIcon, SearchIcon, TagIcon, Trash2Icon, UserIcon } from "lucide-react"
import { DropdownMenu, DropdownMenuCheckboxItem, DropdownMenuContent, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from "./ui/dropdown-menu"
import { Button } from "./ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card"
import { Calendar } from "./ui/calendar"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "./ui/table"
import { Dialog, DialogContent, DialogFooter } from "./ui/dialog"
import { Label } from "./ui/label"
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover"
import { Select, SelectItem, SelectTrigger, SelectValue, SelectContent } from "./ui/select"
import { Input } from "./ui/input"
import { Textarea } from "./ui/textarea"
import { useEffect, useState } from "react"
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious } from "./ui/pagination";
import Bill from "@/entity/bill"
import { useNavigate, useParams, useSearchParams } from "react-router-dom"
import Cookies from "js-cookie"
import Page from "@/entity/page"
import FinanceUnit from "@/entity/financeUnit"
import Category from "@/entity/category"
import { Checkbox } from "./ui/checkbox"
import { format } from "date-fns"

export default function ReportsPage() {
  const [searchParams, setSerachParams] = useSearchParams();

    const [page, setPage] = useState(Number(searchParams.get('page') || 1));
    const [size, setSize] = useState(5);


    const [categories, setCategories] = useState<Category[]>();

    const [financeUnits, setFinanceUnits] = useState<Page<FinanceUnit>>();

    const [bill, setBill] = useState<Bill>();

    const [isModalOpen, setIsModalOpen] = useState(false);
    
    const [createdDate, setCreatedDate] = useState<Date | undefined>(new Date());
    const [createdDescription, setCreatedDescription] = useState<string>('');
    const [createdMoney, setCreatedMoney] = useState<string>('');
    const [createdCategory, setCreatedCategory] = useState<string>('');
    const [createdIsProfit, setCreatedIsProfit] = useState<boolean>(false);
    const [filtrationMember, setFiltrationMember] = useState<number | undefined>();
    const [filtrationCategory, setFiltrationCategory] = useState<number | undefined | null>(undefined);
    const [filtrationDate, setFiltrationDate] = useState<Object | undefined>(undefined);

    const [IsBillInfoModalOpen, setIsBillInfoModalOpen] = useState(false);

    const { billId } = useParams();

    const [joinToken, setJoinToken] = useState<string>('');

    const [deletingReportId, setDeletingReportId] = useState<number | undefined>();

    const navigate = useNavigate();

    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editingReportId, setEditingReportId] = useState<number | undefined>();

    const [editedDate, setEditedDate] = useState<Date | undefined>(new Date());
    const [editedDescription, setEditedDescription] = useState<string>('');
    const [editedMoney, setEditedMoney] = useState<string>('');
    const [editedCategory, setEditedCategory] = useState<string>('');
    const [editedIsProfit, setEditedIsProfit] = useState<boolean>(false);

    const [createAFinanceUnitLoading, setCreateAFinanceUnitLoading] = useState(false);
    const [deleteAFinanceUnitLoading, setDeleteAFinanceUnitLoading] = useState(false);
    const [editAFinanceUnitLoading, setEditAFinanceUnitLoading] = useState(false);

    const BACKEND_URL_BASE = import.meta.env.VITE_BACKEND_URL_BASE 

    const formatter = new Intl.NumberFormat('ru-KZ', {
      style: 'currency',
      currency: 'KZT'
    });

    useEffect(() => {
      searchParams.set('page', page.toString());
      setSerachParams(searchParams);

        fetch(BACKEND_URL_BASE + `/bills/join-token/${billId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + Cookies.get('token')
          }
        }).then(res => {
          if(res.status === 200) {
            res.json().then(data => {
              setJoinToken(data.joinToken);
            })
          }
        })
    }, [])

    useEffect(() => {
      if(!isEditModalOpen) {
        return
      }

      fetch(BACKEND_URL_BASE + `/finance-units/${editingReportId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + Cookies.get('token')
        }
      }).then(res => {
        if(res.status === 200) {
          res.json().then(data => {
            setEditedDescription(data.description);
            setEditedMoney(data.money);
            setEditedCategory(data.category.id);
            setEditedIsProfit(data.isProfit);
          })
        }
      })
    }, [isEditModalOpen])


    useEffect(() => {
        fetch(BACKEND_URL_BASE + `/bills/${billId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + Cookies.get('token')
          },
        }).then(res => {
          if(res.status === 200) {
            res.json().then(data => {
              setBill(data);
            })
          }
        })
      }, [])

      useEffect(() => {
        fetch(BACKEND_URL_BASE + '/categories', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + Cookies.get('token')
          }
        }).then(res => {
          if(res.status === 200) {
            res.json().then(data => {
              setCategories(data);
            })
          }
        })
      }, [])

      useEffect(() => {

        fetch(BACKEND_URL_BASE + `/finance-units/bill/${billId}?&` + 
          `&page=${page - 1}` +
          `&size=${size}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + Cookies.get('token')
          }
        }).then(res => {
          if(res.status === 200) {
            res.json().then(data => {
              setFinanceUnits(data);
              // navigate(0);
            })
          }
        })
      }, [page, size])

      useEffect(() => {
        // if(filtrationCategory === undefined && filtrationDate === undefined && filtrationMember === undefined) {
        //   return
        // }
        console.log(filtrationDate);
        

        // if(filtrationDate === undefined) {
        //   return
        // }

        // if((filtrationDate.from === undefined || filtrationDate.to === undefined)) {
        //   return
        // }
        

        fetch(BACKEND_URL_BASE + `/finance-units/bill/${billId}?&` + 
          `&page=${page - 1}` +
          `&size=${size}` +
          `${filtrationCategory ? `&category=${filtrationCategory}` : ''}` +
          `${filtrationDate && filtrationDate.from ? `&timestampStart=${filtrationDate.from.getTime()}` : ''}` + 
          `${filtrationDate && filtrationDate.to ? `&timestampEnd=${filtrationDate.to.getTime() + 82800000 + 3540000 + 59000 }` : ''}` + 
          `${filtrationMember ? `&creator=${filtrationMember}` : ''}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + Cookies.get('token')
          }
        }).then(res => {
          if(res.status === 200) {
            res.json().then(data => {
              setFinanceUnits(data);
              // navigate(0);
            })
          }
        })
      }, [filtrationCategory, filtrationDate, filtrationMember])

      

      const onCreateAFinanceUnit = (e: React.FormEvent<HTMLFormElement>) => {
        setCreateAFinanceUnitLoading(true);
        e.preventDefault();
        

        fetch(BACKEND_URL_BASE + `/finance-units`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + Cookies.get('token')
          },
          body: JSON.stringify({
            timestamp: createdDate,
            description: createdDescription,
            money: createdMoney,
            isProfit: createdIsProfit,
            category: {
              id: createdCategory
            },
            bill: {
              id: billId
            },
            creator: {
              id: JSON.parse(localStorage.getItem('user')!).id
            }
          })
        }).then(res => {
          if(res.status === 200) {
            navigate(0);
          } else {
            setCreateAFinanceUnitLoading(false);
            console.log('error');
          }
        })
      }

      const makeDate = (date: Date) => {
        const dateObj = new Date(date);
        return `${dateObj.getDate()}/${dateObj.getMonth() + 1}/${dateObj.getFullYear()} ● ${dateObj.getHours()}:${dateObj.getMinutes()}`
      }

      const makeDateShort = (date: Date) => {
        const dateObj = new Date(date);
        return `${dateObj.getDate()}/${dateObj.getMonth() + 1}/${dateObj.getFullYear()}`;
      }

      const makeMoneyStyled = (financeUnit: FinanceUnit) => {
       
        if(financeUnit.isProfit) {
          return <p className=" text-green-500">{`+₸${financeUnit.money}`}</p>;
        } else {
          return <p className="text-red-700">{`-₸${financeUnit.money}`}</p>
        }
      }

      const renderReports = () => {
        if(!financeUnits || financeUnits?.content.length === 0) {
          return <TableRow>
            <TableCell colSpan={5} className="text-center font-bold">No data</TableCell>
          </TableRow>
        }
        return financeUnits?.content.map((financeUnit, index) => {
          return (
            <TableRow key={index}>
              <TableCell>{makeDate(financeUnit.timestamp)}</TableCell>
              <TableCell>{financeUnit.category.name}</TableCell>
              <TableCell>{makeMoneyStyled(financeUnit)}</TableCell>
              <TableCell>{financeUnit.description}</TableCell>
              <TableCell>{financeUnit.creator.login}</TableCell>
              {
                financeUnit.creator.login === JSON.parse(localStorage.getItem('user')!).login ? (
                  <TableCell className="w-10"><Trash2Icon onClick={() => {setIsDeleteModalOpen(true); setDeletingReportId(financeUnit.id)}} className="w-6 h-6 cursor-pointer"/></TableCell>
                ) :
                <></>
              }
              {
                financeUnit.creator.login === JSON.parse(localStorage.getItem('user')!).login ? (
                  <TableCell className="w-10"><EditIcon onClick={() => {setIsEditModalOpen(true); setEditingReportId(financeUnit.id)}} className="w-6 h-6 cursor-pointer"/></TableCell>
                ) :
                <></>
              }
            </TableRow>
          )
        })
      }

      const renderSelectCategories = () => {
        if(!categories) {
          return <></>
        }
        const elems = categories.map(category => {
          return (
            <SelectItem key={category.id} value={category.id.toString()}>{category.name}</SelectItem>
          )
        }) 

        return (
          <SelectContent>
            {elems}
          </SelectContent>
        )
      }

    const renderDropdownCategories = () => {
        if(!categories) {
          return <></>
        }

        let elems = [
          <DropdownMenuItem key={0} onClick={() => setFiltrationCategory(undefined)}>All</DropdownMenuItem>
        ]

        categories.map(category => {
          elems.push(
            <DropdownMenuItem key={category.id} onClick={() => setFiltrationCategory(category.id)}>{category.name}</DropdownMenuItem>
          )
        })

        return (
          <DropdownMenuContent>
            {elems}
          </DropdownMenuContent>
        )
    }

    const renderDropdownMembers = () => {
      if(!categories) {
        return <></>
      }

      let elems = [
        <DropdownMenuItem key={0} onClick={() => setFiltrationMember(undefined)}>All</DropdownMenuItem>
      ]

      bill?.members.map(member => {
        elems.push (
          <DropdownMenuItem key={member.id} onClick={() => setFiltrationMember(member.id)}>{member.login}</DropdownMenuItem>
        )
      })

      return (
        <DropdownMenuContent>
          {elems}
        </DropdownMenuContent>
      )
    }

    const renderMembers = () => {
      if(!bill?.members) {
        return <></>
      }

      const elems = bill?.members.map(member => {
        return (
          <p>{member.login}</p>
        )
      })  

      return (
        <div>
          {elems}
        </div>
      )
    }

    const deleteReport = () => {
      setDeleteAFinanceUnitLoading(true);
      fetch(BACKEND_URL_BASE + `/finance-units/${deletingReportId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + Cookies.get('token')
        }
      }).then(res => {
        if(res.status === 200) {
          navigate(0);
        } else {
          setDeleteAFinanceUnitLoading(false);
        }
      })
    }

    const onEditAFinanceUnit = (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      setEditAFinanceUnitLoading(true);
      fetch(BACKEND_URL_BASE + `/finance-units/${editingReportId}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + Cookies.get('token')
        },
        body: JSON.stringify({
          description: editedDescription,
          money: editedMoney,
          category: {
            id: editedCategory
          },
          bill: {
            id: billId
          },
          timestamp: editedDate,
          isProfit: editedIsProfit,
          creator: {
            id: JSON.parse(localStorage.getItem('user')!).id
          }
        })
        }
      ).then(res => {
        if(res.status === 200) {
          navigate(0);
        } else {
          setEditAFinanceUnitLoading(false);
        }
      })
    }

    return (
        <div className="flex flex-col h-full p-4 w-full">
          <main className="flex-1 bg-muted/40 pt-4 px-6 w-full">
            <div className="grid gap-6 w-full">
              <div className="gap-6 grid md:grid-cols-3 sm:grid-cols-1 ">
                <Card>
                  <CardHeader>
                    <CardDescription>Total Income</CardDescription>
                    <CardTitle>{formatter.format(bill?.totalIncome)}</CardTitle>
                  </CardHeader>
                </Card>
                <Card>
                  <CardHeader>
                    <CardDescription>Total Expenses</CardDescription>
                    <CardTitle>{formatter.format(bill?.totalExpenses)}</CardTitle>
                  </CardHeader>
                </Card>
                <Card>
                  <CardHeader>
                    <CardDescription>Net Profit</CardDescription>
                    <CardTitle className={bill?.totalIncome - bill?.totalExpenses < 0 ? "text-red-700" : "text-green-500"}>{formatter.format(bill?.totalMoney)}</CardTitle>
                  </CardHeader>
                </Card>
              </div>
              <div className="flex items-center justify-between">
                <div>
                  <h2 className="text-lg sm:hidden font-semibold">Finance Reports</h2>
                </div>
                <div className="grid gap-2 grid-flow-col">
                  <Button onClick={() => setIsBillInfoModalOpen(true)}>
                    <FileQuestionIcon className="h-4 w-4 mr-2" />
                    Bill Info
                  </Button>
                  <Button onClick={() => setIsModalOpen(true)}>
                    <PlusIcon className="h-4 w-4 mr-2" />
                    Add Report
                  </Button>
                </div>
              </div>
              <div className="grid gap-4">
                <div className="w-fit grid justify-items-start justify-start gap-4 sm:grid-cols-1 md:grid-cols-3">
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="outline" className="w-[200px] justify-start">
                        <CalendarClockIcon className="h-4 w-4 mr-2" />
                        <span>{filtrationDate == undefined ? "from - to" : (makeDateShort(filtrationDate?.from) + " - " + (filtrationDate?.to == null ? "to" : makeDateShort(filtrationDate?.to)))}</span>
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="start" className="w-[300px]">
                      <Calendar mode={"range"} numberOfMonths={2} selected={filtrationDate} onSelect={setFiltrationDate} disabled={(date) => date > new Date() || date < new Date("1900-01-01")}/>
                    </DropdownMenuContent>
                  </DropdownMenu>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="outline" className="w-[200px] justify-start">
                        <TagIcon className="h-4 w-4 mr-2" />
                        <span>{filtrationCategory == undefined ? "Category" : categories?.find(category => category.id == filtrationCategory)?.name}</span>
                      </Button>
                    </DropdownMenuTrigger>
                    {renderDropdownCategories()}
                  </DropdownMenu>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="outline" className="w-[200px] justify-start">
                        <UserIcon className="h-4 w-4 mr-2" />
                        <span>{filtrationMember == undefined ? "Member" : bill?.members?.find(member => member.id == filtrationMember)?.login}</span>
                      </Button>
                    </DropdownMenuTrigger>
                    {renderDropdownMembers()}
                  </DropdownMenu>
                </div>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Date</TableHead>
                      <TableHead>Category</TableHead>
                      <TableHead>Amount</TableHead>
                      <TableHead>Description</TableHead>
                      <TableHead>Creator</TableHead>
                      <TableHead />
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {renderReports()}
                  </TableBody>
                </Table>
              </div>
            </div>
          </main>
          <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
            <DialogContent className="sm:max-w-[500px]">
              <form onSubmit={onCreateAFinanceUnit}>
                <div className="flex flex-col gap-4">
                  <div className="space-y-1">
                    <Label htmlFor="date">Date</Label>
                    <Popover>
                      <PopoverTrigger asChild>
                        <Button variant="outline" className="w-full justify-start font-normal">
                          <CalendarClockIcon className="h-4 w-4 mr-2" />
                          <span>{createdDate ? createdDate.toLocaleDateString() : 'Select date'}</span>
                        </Button>
                      </PopoverTrigger>
                      <PopoverContent className="w-auto p-0" align="start">
                        <Calendar mode="single" selected={createdDate} onSelect={setCreatedDate} disabled={(date) => date > new Date() || date < new Date("1900-01-01")}/>
                      </PopoverContent>
                    </Popover>
                  </div>
                  <div className="space-y-1">
                    <Label htmlFor="category">Category</Label>
                    <Select onValueChange={(value) => setCreatedCategory(value)} value={createdCategory}>
                      <SelectTrigger id="category">
                        <SelectValue placeholder="Select category" />
                      </SelectTrigger>
                      {renderSelectCategories()}
                    </Select>
                  </div>
                  <div className="space-y-1">
                    <Label htmlFor="amount">Amount</Label>
                    <Input id="amount" type="number" placeholder="0.00" value={createdMoney} onChange={(e) => setCreatedMoney(e.target.value)} />
                  </div>
                  <div className="space-y-1">
                    <Label htmlFor="description">Description</Label>
                    <Textarea id="description" placeholder="Provide details..." value={createdDescription} onChange={(e) => setCreatedDescription(e.target.value)} />
                  </div>
                  <div className="flex align-middle">
                    <Label className="mr-2" htmlFor="isProfit">Is profit</Label>
                    <Checkbox className=" z-50" checked={createdIsProfit} onCheckedChange={() => setCreatedIsProfit(!createdIsProfit)}/>
                  </div>
                </div>
                  <div className="mt-5 flex justify-end gap-2">
                    <Button variant="outline" onClick={() => setIsModalOpen(false)}>
                      Cancel
                    </Button>
                    <Button type="submit" disabled={createAFinanceUnitLoading}>Save Report</Button>
                  </div>
              </form>
            </DialogContent>
          </Dialog>
          <Dialog open={IsBillInfoModalOpen} onOpenChange={setIsBillInfoModalOpen}>
            <DialogContent>
              <div>
                <p className="text-xl">join token</p>
                <div className="w-96 break-words">{joinToken}</div> 
              </div>
              <div>
                <p className="text-xl">members:</p>
                {renderMembers()}
              </div>
            </DialogContent>
          </Dialog>
          <Dialog open={isDeleteModalOpen} onOpenChange={setIsDeleteModalOpen}>
            <DialogContent>
              <div>Are you sure that you want to delete this report?</div>
              <div className="mt-5 flex justify-end gap-2">
                <Button variant="outline" onClick={() => setIsDeleteModalOpen(false)}>Close</Button>
                <Button className="bg-red-500" onClick={deleteReport}>delete</Button>
              </div>
            </DialogContent>
          </Dialog>
          <Dialog open={isEditModalOpen} onOpenChange={setIsEditModalOpen}>
            <DialogContent>
              <form onSubmit={onEditAFinanceUnit}>
                <div className="flex flex-col gap-4">
                  <div className="space-y-1">
                    <Label htmlFor="date">Date</Label>
                    <Popover>
                      <PopoverTrigger asChild>
                        <Button variant="outline" className="w-full justify-start font-normal">
                          <CalendarClockIcon className="h-4 w-4 mr-2" />
                          <span>{editedDate ? editedDate.toLocaleDateString() : 'Select date'}</span>
                        </Button>
                      </PopoverTrigger>
                      <PopoverContent className="w-auto p-0" align="start">
                        <Calendar mode="single" selected={editedDate} onSelect={setEditedDate} disabled={(date) => date > new Date() || date < new Date("1900-01-01")}/>
                      </PopoverContent>
                    </Popover>
                  </div>
                  <div className="space-y-1">
                    <Label htmlFor="category">Category</Label>
                    <Select onValueChange={(value) => setEditedCategory(value)} value={editedCategory}>
                      <SelectTrigger id="category">
                        <SelectValue placeholder="Select category"/>
                      </SelectTrigger>
                      {renderSelectCategories()}
                    </Select>
                  </div>
                  <div className="space-y-1">
                    <Label htmlFor="amount">Amount</Label>
                    <Input id="amount" type="number" placeholder="0.00" value={editedMoney} onChange={(e) => setEditedMoney(e.target.value)} />
                  </div>
                  <div className="space-y-1">
                    <Label htmlFor="description">Description</Label>
                    <Textarea id="description" placeholder="Provide details..." value={editedDescription} onChange={(e) => setEditedDescription(e.target.value)} />
                  </div>
                  <div className="flex align-middle">
                    <Label className="mr-2" htmlFor="isProfit">Is profit</Label>
                    <Checkbox className=" z-50" checked={editedIsProfit} onCheckedChange={() => setEditedIsProfit(!editedIsProfit)}/>
                  </div>
                </div>
                  <div className="mt-5 flex justify-end gap-2">
                    <Button variant="outline" onClick={() => setIsEditModalOpen(false)}>
                      Cancel
                    </Button>
                    <Button type="submit">Save Report</Button>
                  </div>
              </form>
            </DialogContent>
          </Dialog>
          <Pagination>
              <PaginationContent>
                  <PaginationItem>
                  <PaginationPrevious className={page <= 1 ? 'pointer-events-none text-gray-400' : ''} href={`/bills/${billId}?page=${page - 1}`} />
                  </PaginationItem>
                  <PaginationItem>
                  <PaginationLink>{`${page} - ${financeUnits?.totalPages}`}</PaginationLink>
                  </PaginationItem>
                  <PaginationItem>
                      {financeUnits ? 
                      <PaginationNext className={page >= financeUnits.totalPages ? 'pointer-events-none text-gray-400' : ''} href={`/bills/${billId}?page=${page + 1}`} />
                      :
                      <PaginationNext className="pointer-events-none text-gray-400" />
                      }
                  </PaginationItem>
              </PaginationContent>
          </Pagination>
        </div>
      )
}