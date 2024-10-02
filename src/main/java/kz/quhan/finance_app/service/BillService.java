package kz.quhan.finance_app.service;

import jakarta.transaction.Transactional;
import kz.quhan.finance_app.dto.FullBillDTO;
import kz.quhan.finance_app.dto.JoinToken;
import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.Bill;
import kz.quhan.finance_app.exception.ApplicationAccessDeniedException;
import kz.quhan.finance_app.exception.BillException;
import kz.quhan.finance_app.mapper.BillMapper;
import kz.quhan.finance_app.repository.BillRepository;
import kz.quhan.finance_app.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BillService {
    private final BillRepository billRepository;
    private final AppUserService appUserService;
    private final BillMapper billMapper;
    private final JWTUtils jwtUtils;

    public Page<Bill> getBillsByUserId(String login, Pageable pageable) {
        return billRepository.getBillsByCreatorLogin(login, pageable);
    }

    @Transactional
    public Page<FullBillDTO> getBills(String userLogin, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Bill> billPage = getBillsByUserId(userLogin, pageable);

        billPage.forEach(bill -> {
            bill.getMembers().size();
        });

        List<FullBillDTO> fullBillDTOs = billMapper.toFullBillDTOs(billPage.getContent());

        return new PageImpl<>(fullBillDTOs, pageable, billPage.getTotalElements());
    }

    public void save(Bill bill) {
        billRepository.save(bill);
    }

    public Page<Bill> getList(Pageable pageable) {
        return billRepository.findAll(pageable);
    }

    public Bill getOne(Integer id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new BillException("bill with this id doesn't exist"));
    }

    public List<Bill> getMany(Collection<Integer> ids) {
        return billRepository.findAllById(ids);
    }

    public Bill create(Bill bill) {
        System.out.println(bill.getName());
        Optional<Bill> billByName = billRepository.getBillByName(bill.getName());

        if(billByName.isPresent()) {
            throw new BillException("bill with this name is already exist");
        }

        var context = SecurityContextHolder.getContext().getAuthentication();

        AppUser creator = appUserService.getAppUserByLogin(context.getName());

        List<AppUser> members = new ArrayList<>();
        members.add(creator);

        bill.setMembers(members);
        bill.setCreator(creator);

        return billRepository.save(bill);
    }

    public void delete(Integer id) {
        Optional<Bill> billoptional = billRepository.findById(id);

        if(billoptional.isEmpty()) {
            throw new BillException("bill with this id doesn't exist");
        }

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        Bill billFromDB = billoptional.get();

        Optional<AppUser> isMember = billFromDB.getMembers().stream()
                .filter(member -> member.getLogin().equals(userLogin))
                .findAny();

        if(isMember.isPresent()) {
            billRepository.deleteById(id);
        } else {
            throw new ApplicationAccessDeniedException("you can't delete this bill");
        }
    }

    public JoinToken generateJoinToken(Integer billId) {
        Optional<Bill> billOptional = billRepository.findById(billId);

        if(billOptional.isPresent()) {
            Bill bill = billOptional.get();

            String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

            Optional<AppUser> userInBillOptional = bill.getMembers()
                    .stream()
                    .filter(member -> member.getLogin().equals(userLogin))
                    .findAny();

            if(userInBillOptional.isPresent()) {
                String token = jwtUtils.generateJoinToken(billId);

                return new JoinToken(token);
            } else {
                throw new ApplicationAccessDeniedException("you don't have access to this bill");
            }
        } else {
            throw new BillException("bill with this id doesn't exist");
        }
    }

    @Transactional
    public void validateAndJoinToBill(String joinToken) {
        String id = jwtUtils.validateJoinTokenAndRetrieveBillId(joinToken);

        Optional<Bill> billOptional = billRepository.findById(Integer.parseInt(id));

        if(billOptional.isPresent()) {
            Bill bill = billOptional.get();
            String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

            AppUser appUser = appUserService.getAppUserByLogin(userLogin);

            Optional<AppUser> userInBill = bill.getMembers()
                    .stream()
                    .filter(user -> user.getLogin().equals(userLogin))
                    .findAny();

            if(userInBill.isEmpty()) {
                bill.getMembers().add(appUser);
            } else {
                throw new BillException("user in this bill is already stays");
            }
        } else {
            throw new BillException("bill with this id doesn't exist");
        }
    }

    public Bill getOneThatMine(Integer id) {
        Optional<Bill> billOptional = billRepository.findById(id);
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        if(billOptional.isPresent()) {
            Bill bill = billOptional.get();

            Optional<AppUser> isMember = bill.getMembers().stream().filter(member -> member.getLogin().equals(userLogin)).findAny();

            if(isMember.isPresent()) {
                return bill;
            } else {
                throw new ApplicationAccessDeniedException("you can't access to this bill");
            }
        } else {
            throw new BillException("bill with this id doesn't exist");
        }
    }
}
