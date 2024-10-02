package kz.quhan.finance_app.restController;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.quhan.finance_app.dto.FullFinanceUnitDTO;
import kz.quhan.finance_app.entity.FinanceUnit;
import kz.quhan.finance_app.repository.FinanceUnitRepository;
import kz.quhan.finance_app.service.FinanceUnitService;
import kz.quhan.finance_app.specification.FinanceUnitSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance-units")
@RequiredArgsConstructor
public class FinanceUnitRestController {

    private final FinanceUnitService financeUnitService;

    @GetMapping("/bill/{billId}")
    public Page<FullFinanceUnitDTO> getList(
            @PathVariable("billId") Integer billId,
            @RequestParam(value = "page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "timestampStart", required = false)String timestampStart,
            @RequestParam(value = "timestampEnd", required = false)String timestampEnd,
            @RequestParam(value = "creator", required = false)Integer creatorId,
            @RequestParam(value = "category", required = false) Integer categoryId) {

        Specification<FinanceUnit> spec = Specification.where(FinanceUnitSpecification.hasCategory(categoryId))
                .and(FinanceUnitSpecification.hasCreator(creatorId))
                .and(FinanceUnitSpecification.moreThanTimestamp(timestampStart))
                .and(FinanceUnitSpecification.lessThanTimestamp(timestampEnd))
                .and(FinanceUnitSpecification.hasBill(billId))
                .and(FinanceUnitSpecification.hasCategory(categoryId));

        return financeUnitService.getList(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp")), spec);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        FullFinanceUnitDTO financeUnitDTO = financeUnitService.getOne(id);

        return ResponseEntity.ok(financeUnitDTO);
    }

    @PostMapping
    public ResponseEntity<FinanceUnit> create(@RequestBody FullFinanceUnitDTO dto) {
        return ResponseEntity.ok(financeUnitService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> edit(@PathVariable Integer id, @RequestBody FullFinanceUnitDTO dto) {
        financeUnitService.edit(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        financeUnitService.delete(id);

        return ResponseEntity.ok().build();
    }
}
