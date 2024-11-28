package org.example.accountservice.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.accountservice.dto.AccountDto;
import org.example.accountservice.model.Account;
import org.example.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/account")
public class AccountController {

    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/get-accounts")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Account>> getAccounts(){
        return new ResponseEntity<>(this.accountService.getAccounts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable(name = "id") Long id){
        // TO DO: CHECK IF THE SAME USER WANT TO GET THE SAME ID
        return this.accountService.getAccount(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create-account")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto){
        return new ResponseEntity<>(this.accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    @PutMapping("/update-account/{id}")
    public ResponseEntity<AccountDto> updateAccounts(@PathVariable("id") Long id, @RequestBody Account account) {
        // TO DO: CHECK IF THE SAME USER WANT TO GET THE SAME ID
        account.setId(id); // Ensure the ID from the path is used
        return new ResponseEntity<>(this.accountService.updateAccounts(account), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAccounts(@PathVariable(name = "id") Long id){
        boolean isDeleted = this.accountService.deleteAccount(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        }
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> transaction(@RequestParam("id") Long recipientId, @RequestParam("amount") double amount){
        // TO Do: Check if the user is the same using authentication
        Long senderId = 1L; // TO DO: GET IT FROM AUTHENTICATION
        try {
            // Perform the transaction logic in the service layer
            this.accountService.doTransaction(senderId, recipientId, amount);

            // Return success response
            return new ResponseEntity<>("Transaction successful!", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Handle any validation or business logic errors (e.g., insufficient funds)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle unexpected errors
            return new ResponseEntity<>("An error occurred during the transaction.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
