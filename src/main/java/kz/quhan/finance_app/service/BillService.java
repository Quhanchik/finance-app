package kz.quhan.finance_app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kz.quhan.finance_app.dto.BillWithCreatorAndMembersDTO;
import kz.quhan.finance_app.dto.JoinToken;
import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.Bill;
import kz.quhan.finance_app.repository.AppUserRepository;
import kz.quhan.finance_app.repository.BillRepository;
import kz.quhan.finance_app.utils.JWTUtils;
import kz.quhan.finance_app.utils.NoSuchBillException;
import kz.quhan.finance_app.utils.UserInBillDoesntExistException;
import kz.quhan.finance_app.utils.UserInBillExistException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
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
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final ObjectMapper mapper;
    private final JWTUtils jwtUtils;

    public Page<Bill> getBillsByUserId(Integer id, Pageable pageable) {
        return billRepository.gettingBillsByUserId(id, pageable);
    }

    @Transactional
    public Page<BillWithCreatorAndMembersDTO> getBillsWithCreatorAndMembersByUserId(Integer id, Pageable pageable) {
        Page<Bill> billPage = getBillsByUserId(id, pageable);

        billPage.forEach(bill -> {
            bill.getMembers().size();
        });

        return billPage.map(this::billToBillWithCreatorAndMembersDTO);
    }

    public void save(Bill bill) {
        billRepository.save(bill);
    }

    public Page<Bill> getList(Pageable pageable) {
        return billRepository.findAll(pageable);
    }

    public Optional<Bill> getOne(Integer id) {
        return billRepository.findById(id);
    }

    public List<Bill> getMany(Collection<Integer> ids) {
        return billRepository.findAllById(ids);
    }

    public Bill create(Bill bill) {
        var context = SecurityContextHolder.getContext().getAuthentication();

        AppUser creator = appUserService.getOne(Integer.parseInt(context.getName())).get();

        List<AppUser> members = new ArrayList<>();
        members.add(creator);

        bill.setMembers(members);
        bill.setCreator(creator);

        return billRepository.save(bill);
    }

    public Bill patch(Bill id, JsonNode patchNode) {
        return billRepository.save(id);
    }

    public List<Bill> patchMany(Collection<Bill> ids, JsonNode patchNode) {
        return billRepository.saveAll(ids);
    }

    public void delete(Bill id) {
        billRepository.delete(id);
    }

    public void deleteMany(Collection<Integer> ids) {
        billRepository.deleteAllById(ids);
    }

    public BillWithCreatorAndMembersDTO billToBillWithCreatorAndMembersDTO(Bill bill) {
        return mapper.convertValue(bill, BillWithCreatorAndMembersDTO.class);
    }

    public Optional<JoinToken> generateJoinToken(Integer billId) {
        Optional<Bill> billOptional = billRepository.findById(billId);

        if(billOptional.isPresent()) {
            Bill bill = billOptional.get();
            Integer currentUserId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
//            AppUser currentUser = appUserService.getOne(currentUserId).get();

            Optional<AppUser> userInBillOptional = bill.getMembers()
                    .stream()
                    .filter(member -> member.getId().equals(currentUserId))
                    .findAny();

            if(userInBillOptional.isPresent()) {
                String token = jwtUtils.generateJoinToken(billId);

                return Optional.of(new JoinToken(token));
            } else {
                throw new UserInBillDoesntExistException();
            }
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public void validateAndJoinToBill(String joinToken) throws UserInBillExistException, NoSuchBillException {
        String id = jwtUtils.validateJoinTokenAndRetrieveBillId(joinToken);

        Optional<Bill> billOptional = billRepository.findById(Integer.parseInt(id));

        if(billOptional.isPresent()) {
            Bill bill = billOptional.get();
            AppUser appUser = appUserService.getOne(Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName())).get();

            Optional<AppUser> userInBill = bill.getMembers()
                    .stream()
                    .filter(user -> user.getId().equals(appUser.getId()))
                    .findAny();

            if(userInBill.isEmpty()) {
                bill.getMembers().add(appUser);
            } else {
                throw new UserInBillExistException();
            }
        } else {
            throw new NoSuchBillException();
        }
    }
}
