package kz.quhan.finance_app.restController;

import kz.quhan.finance_app.dto.FullBillDTO;
import kz.quhan.finance_app.dto.JoinToken;
import kz.quhan.finance_app.entity.Bill;
import kz.quhan.finance_app.mapper.BillMapper;
import kz.quhan.finance_app.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillRestController {

    private final BillService billService;

    private final BillMapper billMapper;

    @GetMapping
    public ResponseEntity<Page<FullBillDTO>> getAllMyBills(@RequestParam("page") Integer page, @RequestParam("size") Integer size, Principal principal) {
        Page<FullBillDTO> fullBillDTOs = billService.getBills(principal.getName(), page, size);

        return ResponseEntity.ok(fullBillDTOs);
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody Bill billReq) {
        Bill createdBill = billService.create(billReq);

        return ResponseEntity.ok(createdBill.getId());
    }

    @GetMapping("/join-token/{billId}")
    public ResponseEntity<?> generateJoinToken(@PathVariable("billId") Integer billId) {
        JoinToken joinToken = billService.generateJoinToken(billId);

        return ResponseEntity.ok(joinToken);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        Bill bill = billService.getOneThatMine(id);

        FullBillDTO fullBillDTO = billMapper.toFullBillDTO(bill);

        return ResponseEntity.ok(fullBillDTO);
    }

    @PostMapping("/join-token")
    public ResponseEntity<?> validateAndJoinToBill(@RequestBody JoinToken joinToken) {
        billService.validateAndJoinToBill(joinToken.getJoinToken());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        billService.delete(id);

        return ResponseEntity.ok().build();
    }
}
