package kz.quhan.finance_app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kz.quhan.finance_app.dto.FullFinanceUnitDTO;
import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.Bill;
import kz.quhan.finance_app.entity.FinanceUnit;
import kz.quhan.finance_app.exception.ApplicationAccessDeniedException;
import kz.quhan.finance_app.exception.FinanceUnitException;
import kz.quhan.finance_app.mapper.BillMapper;
import kz.quhan.finance_app.mapper.FinanceUnitMapper;
import kz.quhan.finance_app.repository.FinanceUnitRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FinanceUnitService {
    private final FinanceUnitRepository financeUnitRepository;
    private final BillService billService;
    private final FinanceUnitMapper financeUnitMapper;
    private final AppUserService appUserService;

    private final BillMapper billMapper;

    public Page<FullFinanceUnitDTO> getList(Pageable pageable, Specification<FinanceUnit> spec) {
        return financeUnitRepository
                .findAll(spec, pageable)
                .map(financeUnitMapper::toFullFinanceUnitDTO);
    }

    @Transactional
    public FullFinanceUnitDTO getOne(Integer id) {
        Optional<FinanceUnit> financeUnitOptional = financeUnitRepository.findById(id);

        if(financeUnitOptional.isPresent()) {
            FinanceUnit financeUnit = financeUnitOptional.get();

            Hibernate.initialize(financeUnit.getCategory());
            Hibernate.initialize(financeUnit.getCreator());

           return financeUnitMapper.toFullFinanceUnitDTO(financeUnit);
        } else {
            throw new FinanceUnitException("finance unit with this id doesn't exist");
        }
    }

    public FinanceUnit create(FullFinanceUnitDTO fullFinanceUnitDTO) {
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser appUser = appUserService.getAppUserByLogin(userLogin);

        if(fullFinanceUnitDTO.getCreator().getId().equals(appUser.getId())) {
            FinanceUnit financeUnit = financeUnitMapper.toEntity(fullFinanceUnitDTO);

            Bill bill = billService.getOne(financeUnit.getBill().getId());

                if(financeUnit.getIsProfit()) {
                    bill.setTotalMoney(bill.getTotalMoney() + financeUnit.getMoney());
                    bill.setTotalIncome(bill.getTotalIncome() + financeUnit.getMoney());
                } else {
                    bill.setTotalMoney(bill.getTotalMoney() - financeUnit.getMoney());
                    bill.setTotalExpenses(bill.getTotalExpenses() + financeUnit.getMoney());
                }

                return financeUnitRepository.save(financeUnit);
        } else {
            throw new ApplicationAccessDeniedException("id of creator doesn't match to yours");
        }
    }

    public void delete(Integer id) {
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<FinanceUnit> financeUnitOptional = financeUnitRepository.findById(id);

        if(financeUnitOptional.isEmpty()) {
            throw new FinanceUnitException("financial unit with this id doesn't exist");
        }

        FinanceUnit financeUnit = financeUnitOptional.get();

        if(!financeUnit.getCreator().getLogin().equals(userLogin)) {
            throw new ApplicationAccessDeniedException("you don't have access to this finance unit");
        }

        Bill bill = financeUnit.getBill();

        if(financeUnit.getIsProfit()) {
            bill.setTotalMoney(bill.getTotalMoney() - financeUnit.getMoney());
            bill.setTotalIncome(bill.getTotalIncome() - financeUnit.getMoney());
        } else {
            bill.setTotalMoney(bill.getTotalMoney() + financeUnit.getMoney());
            bill.setTotalExpenses(bill.getTotalExpenses() - financeUnit.getMoney());
        }

        billService.save(bill);

        financeUnitRepository.delete(financeUnit);
    }

    public void edit(Integer id, FullFinanceUnitDTO dto) {
        dto.setId(id);
        Optional<FinanceUnit> financeUnitOptional = financeUnitRepository.findById(dto.getId());

        if(financeUnitOptional.isPresent()) {
            FinanceUnit financeUnit = financeUnitOptional.get();

            String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

            if(financeUnit.getCreator().getLogin().equals(userLogin)) {
                Bill bill = billService.getOne(dto.getBill().getId());

                if(!financeUnit.getIsProfit().equals(dto.getIsProfit())) {
                    if(dto.getIsProfit()) {
                        bill.setTotalMoney(bill.getTotalMoney() + financeUnit.getMoney() * 2);
                        bill.setTotalIncome(bill.getTotalIncome() + financeUnit.getMoney());
                        bill.setTotalExpenses(bill.getTotalExpenses() - financeUnit.getMoney());
                    } else {
                        bill.setTotalMoney(bill.getTotalMoney() - financeUnit.getMoney() * 2);
                        bill.setTotalExpenses(bill.getTotalExpenses() + financeUnit.getMoney());
                        bill.setTotalIncome(bill.getTotalIncome() - financeUnit.getMoney());
                    }
                }
                dto.setBill(billMapper.toBillDTO(bill));
                FinanceUnit entity = financeUnitMapper.toEntity(dto);
                financeUnitRepository.save(entity);
            } else {
                throw new ApplicationAccessDeniedException("you can't access to this finance unit");
            }
        } else {
            throw new FinanceUnitException("finance unit with this id doesn't exist");
        }
    }
}
