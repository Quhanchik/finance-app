package kz.quhan.finance_app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kz.quhan.finance_app.dto.FinanceUnitWithCategoryAndCreatorAndBillDTO;
import kz.quhan.finance_app.entity.Bill;
import kz.quhan.finance_app.entity.FinanceUnit;
import kz.quhan.finance_app.repository.FinanceUnitRepository;
import kz.quhan.finance_app.utils.UserIdDoesntMatchException;
import kz.quhan.finance_app.utils.UserInBillDoesntExistException;
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
    private final ObjectMapper objectMapper;
    private final BillService billService;
    public Page<FinanceUnitWithCategoryAndCreatorAndBillDTO> getList(Pageable pageable, Specification<FinanceUnit> spec) {
        Integer currentUserId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        System.out.println("log");

        return financeUnitRepository
                .findAll(spec, pageable)
                .map(financeUnit -> {
                    return objectMapper.convertValue(financeUnit,
                            FinanceUnitWithCategoryAndCreatorAndBillDTO.class);
                });
    }

    public Optional<FinanceUnit> getOne(Integer id) {
        return financeUnitRepository.findById(id);
    }

    @Transactional
    public Optional<FinanceUnitWithCategoryAndCreatorAndBillDTO> getOneWithCategory(Integer id) {
        Optional<FinanceUnit> financeUnitOptional = getOne(id);

        if(financeUnitOptional.isPresent()) {
            FinanceUnit financeUnit = financeUnitOptional.get();

            Hibernate.initialize(financeUnit.getCategory());
            Hibernate.initialize(financeUnit.getCreator());

            var dto = objectMapper.convertValue(financeUnit, FinanceUnitWithCategoryAndCreatorAndBillDTO.class);

            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    public List<FinanceUnit> getMany(Collection<Integer> ids) {
        return financeUnitRepository.findAllById(ids);
    }

    public FinanceUnit create(FinanceUnitWithCategoryAndCreatorAndBillDTO financeUnitWithCategoryAndCreatorAndBillDTO) {
        Integer currentUserId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        if(financeUnitWithCategoryAndCreatorAndBillDTO.getCreator().getId().equals(currentUserId)) {
            FinanceUnit financeUnit = objectMapper.convertValue(financeUnitWithCategoryAndCreatorAndBillDTO, FinanceUnit.class);

            Optional<Bill> billOptional = billService.getOne(financeUnit.getBill().getId());

            if(billOptional.isPresent()) {

                Bill bill = billOptional.get();


                if(financeUnit.getIsProfit()) {
                    bill.setTotalMoney(bill.getTotalMoney() + financeUnit.getMoney());
                    bill.setTotalIncome(bill.getTotalIncome() + financeUnit.getMoney());
                } else {
                    bill.setTotalMoney(bill.getTotalMoney() - financeUnit.getMoney());
                    bill.setTotalExpenses(bill.getTotalExpenses() + financeUnit.getMoney());
                }

                return financeUnitRepository.save(financeUnit);
            } else {
                throw new UserInBillDoesntExistException();
            }

        } else {
            throw new UserIdDoesntMatchException();
        }
    }

    public FinanceUnit patch(Integer id, FinanceUnit financeUnit) {
        financeUnit.setId(id);
        return financeUnitRepository.save(financeUnit);
    }

    public List<FinanceUnit> patchMany(Collection<FinanceUnit> ids, JsonNode patchNode) {
        return financeUnitRepository.saveAll(ids);
    }

    public void delete(FinanceUnit financeUnit) {
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

    public void deleteMany(Collection<Integer> ids) {
        financeUnitRepository.deleteAllById(ids);
    }
}
