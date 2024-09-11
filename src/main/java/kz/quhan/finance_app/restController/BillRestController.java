package kz.quhan.finance_app.restController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import kz.quhan.finance_app.dto.BillWithCreatorAndMembersDTO;
import kz.quhan.finance_app.dto.JoinToken;
import kz.quhan.finance_app.entity.Bill;
import kz.quhan.finance_app.repository.BillRepository;
import kz.quhan.finance_app.service.BillService;
import kz.quhan.finance_app.utils.NoSuchBillException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bill")
@RequiredArgsConstructor
public class BillRestController {

    private final BillService billService;

    private final BillRepository billRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public Page<BillWithCreatorAndMembersDTO> getAllByUserId(@RequestParam("page") Integer page, @RequestParam("size") Integer size, Principal principal) {
        System.out.println(principal.getName());

        Pageable pageable = PageRequest.of(page, size);

        return billService.getBillsWithCreatorAndMembersByUserId(Integer.parseInt(principal.getName()), pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        Optional<Bill> billOptional = billService.getOne(id);

        if(billOptional.isPresent()) {
            Bill bill = billOptional.get();

            BillWithCreatorAndMembersDTO dto = objectMapper.convertValue(bill, BillWithCreatorAndMembersDTO.class);

            return ResponseEntity.ok(dto);
        } else {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "bill with this id doesn't exist"
            );

            return ResponseEntity.badRequest().body(problemDetail);
        }
    }

    @GetMapping("/by-ids")
    public List<Bill> getMany(@RequestParam List<Integer> ids) {
        return billService.getMany(ids);
    }

    @PostMapping
    public Bill create(@RequestBody Bill bill) {
        return billService.create(bill);
    }

    @GetMapping("/join-token/{billId}")
    public ResponseEntity<?> generateJoinToken(@PathVariable("billId") Integer billId) {
        Optional<JoinToken> tokenOptional = billService.generateJoinToken(billId);

        if(tokenOptional.isPresent()) {
            return ResponseEntity.ok(tokenOptional.get());
        } else {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND,
                    "id doesn't exist"
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
        }
    }

    @PostMapping("/join-token")
    public ResponseEntity<?> validateAndJoinToBill(@RequestBody JoinToken joinToken) throws NoSuchBillException {
        billService.validateAndJoinToBill(joinToken.getJoinToken());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @PatchMapping("/{id}")
//    public Bill patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) throws IOException {
//        Bill bill = billRepository.findById(id).orElseThrow(() ->
//                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
//
//        objectMapper.readerForUpdating(bill).readValue(patchNode);
//
//        return billService.patch(bill);
//    }

//    @PatchMapping
//    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
//        Collection<Bill> bills = billRepository.findAllById(ids);
//
//        for (Bill bill : bills) {
//            objectMapper.readerForUpdating(bill).readValue(patchNode);
//        }
//
//        List<Bill> resultBills = billService.patchMany(bills);
//        List<Integer> ids1 = resultBills.stream()
//                .map(Bill::getId)
//                .toList();
//        return ids1;
//    }

    @DeleteMapping("/{id}")
    public Bill delete(@PathVariable Integer id) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill != null) {
            billService.delete(bill);
        }
        return bill;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        billService.deleteMany(ids);
    }
}
