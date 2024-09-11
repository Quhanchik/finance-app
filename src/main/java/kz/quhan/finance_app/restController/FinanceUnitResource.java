package kz.quhan.finance_app.restController;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import kz.quhan.finance_app.dto.FinanceUnitWithCategoryAndCreatorAndBillDTO;
import kz.quhan.finance_app.entity.AppUser;
import kz.quhan.finance_app.entity.FinanceUnit;
import kz.quhan.finance_app.repository.FinanceUnitRepository;
import kz.quhan.finance_app.service.FinanceUnitService;
import kz.quhan.finance_app.specification.FinanceUnitSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/finance-unit")
@RequiredArgsConstructor
public class FinanceUnitResource {

    private final FinanceUnitService financeUnitService;

    private final FinanceUnitRepository financeUnitRepository;

    private final ObjectMapper objectMapper;

    @GetMapping("/bill/{billId}")
    public Page<FinanceUnitWithCategoryAndCreatorAndBillDTO> getList(
            @PathVariable("billId") Integer billId,
            @RequestParam(value = "page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "timestampStart", required = false)String timestampStart,
            @RequestParam(value = "timestampEnd", required = false)String timestampEnd,
            @RequestParam(value = "creator", required = false)Integer creatorId,
            @RequestParam(value = "category", required = false) Integer categoryId) {
        System.out.println(creatorId);


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
        var financialUnitOptional = financeUnitService.getOneWithCategory(id);

        if(financialUnitOptional.isPresent()) {
            return ResponseEntity.ok(financialUnitOptional.get());
        } else {
            var problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.BAD_REQUEST, "financial unit with this id doesn't found");

            return ResponseEntity.badRequest().body(problemDetail);
        }
    }

    @GetMapping("/by-ids")
    public List<FinanceUnit> getMany(@RequestParam List<Integer> ids) {
        return financeUnitService.getMany(ids);
    }

    @PostMapping
    public FinanceUnit create(@RequestBody FinanceUnitWithCategoryAndCreatorAndBillDTO dto) {
        return financeUnitService.create(dto);
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<?> patch(@PathVariable Integer id) throws IOException {
//
//    }

//    @PatchMapping
//    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody List<FinanceUnit> financeUnitsPayload) throws IOException {
//
//        List<FinanceUnit> resultFinanceUnits = financeUnitService.patchMany(financeUnits);
//        List<Integer> ids1 = resultFinanceUnits.stream()
//                .map(FinanceUnit::getId)
//                .toList();
//        return ids1;
//    }

    @DeleteMapping("/{id}")
    public FinanceUnit delete(@PathVariable Integer id) {
        FinanceUnit financeUnit = financeUnitRepository.findById(id).orElse(null);
        if (financeUnit != null) {
            financeUnitService.delete(financeUnit);
        }
        return financeUnit;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        financeUnitService.deleteMany(ids);
    }
}
