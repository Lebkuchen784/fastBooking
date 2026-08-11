package com.project.bookingService.user.businessOwner;

import com.project.bookingService.config.authentication.AuthRequest;
import com.project.bookingService.config.authentication.JWTUtility;
import com.project.bookingService.organization.Organization;
import com.project.bookingService.organization.OrganizationCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final JWTUtility jwtUtility;
    private AuthenticationManager authenticationManager;

    @PostMapping("/genToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return jwtUtility.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
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
    public RedirectView register(@ModelAttribute OwnerRegistrationDTO request) {
        ownerService.registerOwner(request);
        return new RedirectView("/");
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
