package com.app.em.controller.user;

import com.app.em.persistence.entity.user.BranchChief;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.user.BranchChiefRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.ListToResponseEntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class BranchChiefController
{
    @Autowired
    BranchChiefRepository branchChiefRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ListToResponseEntityWrapper listToResponseEntityWrapper;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/branch_chiefs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBranchChief(@RequestBody BranchChief branchChief)
    {
        return branchChiefRepository.findByBranchChiefName(branchChief.getBranchChiefName())
                .map(this::branchChiefAlreadyExists)
                .orElseGet(() -> ResponseEntity.ok(branchChiefRepository.save(branchChief)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/branch_chiefs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBranchChief(@PathVariable Integer id)
    {
        return ResponseEntity.of( branchChiefRepository.findById(id) );
    }

    @GetMapping(value = "/branch_chiefs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllBranchChiefs()
    {
        return Optional.ofNullable(branchChiefRepository.findAll())
                .map(listToResponseEntityWrapper::wrapListInResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/branch_chiefs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateBranchChief(@RequestBody BranchChief branchChief)
    {
        return branchChiefRepository.findById(branchChief.getId())
                .map(existingBranchChief -> ResponseEntity.ok(branchChiefRepository.save(branchChief)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/branch_chiefs/{id}")
    public ResponseEntity deleteBranchChief(@PathVariable Integer id)
    {
        return branchChiefRepository.findById(id)
                .map(branchChief -> {
                    return Optional.ofNullable(userRepository.findByBranchChief(branchChief))
                            .map(this::branchChiefHasUsersAssigned)
                            .orElseGet(() -> {
                                branchChiefRepository.delete(branchChief);
                                return ResponseEntity.ok().build();
                            });
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity branchChiefAlreadyExists(BranchChief branchChief)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("A branch chief " + branchChief.getBranchChiefName() + " already exists."));
    }

    private ResponseEntity branchChiefHasUsersAssigned(List<User> users)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("A branch chief " +
                    users.stream().findAny().map(user -> user.getBranchChief().getBranchChiefName()) +
                    " cannot be removed because it has one or more people assigned. Change their branch chief and try again."));
    }
}
