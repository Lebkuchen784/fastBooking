package com.project.bookingService.user.businessOwner;

import com.project.bookingService.config.authentication.AuthRequestDTO;
import com.project.bookingService.organization.Organization;
import com.project.bookingService.organization.OrganizationCreationDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/generateToken")
    public java.util.Map<String, String> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response) {
        return ownerService.generateJWTToken(authRequestDTO, response);
    }

    @GetMapping("/getOwner/{owner_id}")
    public ResponseEntity<Owner> getOwner(@PathVariable String owner_id) {
        return ResponseEntity.status(HttpStatus.OK).body(ownerService.getOwner(owner_id));
    }

    @GetMapping("/{owner_id}/org")
    public ResponseEntity<Organization> getOrganization(@PathVariable String owner_id) {
        return ResponseEntity.status(HttpStatus.OK).body(ownerService.getOwnerOrganization(owner_id));
    }

    @PostMapping("/register")
    public ResponseEntity<Owner> register(@RequestBody OwnerRegistrationDTO request) {
        Owner saved = ownerService.registerOwner(request);
        if (saved == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestBody String ownerId) {
        ownerService.removeOwner(ownerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{owner_id}/org")
    public ResponseEntity<Owner> removeOrgFromOwner(@PathVariable String owner_id) {
        Owner result = ownerService.removeOrgFromOwner(owner_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{owner_id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable String owner_id, @RequestBody OwnerRegistrationDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerService.updateOwner(owner_id, request));
    }

    @PostMapping("/{owner_id}/updateOrg")
    public ResponseEntity<Organization> updateOwnerOrganization(@PathVariable String owner_id, @RequestBody OrganizationCreationDTO requestBodyForOrganization) {
        Organization result = ownerService.updateOwnerOrganization(owner_id, requestBodyForOrganization);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
