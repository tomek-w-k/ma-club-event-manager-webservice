package com.app.em.controller.user;

import com.app.em.persistence.entity.user.BranchChief;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.user.BranchChiefRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.ListToResponseEntityWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class BranchChiefController
{
    private final BranchChiefRepository branchChiefRepository;
    private final UserRepository userRepository;
    private final ListToResponseEntityWrapper listToResponseEntityWrapper;


    public BranchChiefController(   BranchChiefRepository branchChiefRepository,
                                    UserRepository userRepository,
                                    ListToResponseEntityWrapper listToResponseEntityWrapper
    ) {
        this.branchChiefRepository = branchChiefRepository;
        this.userRepository = userRepository;
        this.listToResponseEntityWrapper = listToResponseEntityWrapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/branch_chiefs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBranchChief(@RequestBody BranchChief branchChief)
    {
        return saveIfNotExist(branchChief);
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
        return listToResponseEntityWrapper.wrapListInResponseEntity(branchChiefRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/branch_chiefs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateBranchChief(@RequestBody BranchChief branchChief)
    {
        return saveIfNotExist(branchChief);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/branch_chiefs/{id}")
    public ResponseEntity deleteBranchChief(@PathVariable Integer id)
    {
        return branchChiefRepository.findById(id)
                .map(branchChief -> {
                    return userRepository.findByBranchChief(branchChief)
                            .stream()
                            .findAny()
                            .map(this::branchChiefHasUsersAssigned)
                            .orElseGet(() -> {
                                branchChiefRepository.delete(branchChief);
                                return ResponseEntity.ok().build();
                            });
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity saveIfNotExist(BranchChief branchChief)
    {
        return branchChiefRepository.findByBranchChiefName(branchChief.getBranchChiefName())
                .map(this::branchChiefAlreadyExists)
                .orElseGet(() -> ResponseEntity.ok(branchChiefRepository.save(branchChief)));
    }

    private ResponseEntity branchChiefAlreadyExists(BranchChief branchChief)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("branch_chief " + branchChief.getBranchChiefName() + " already_exists"));
    }

    private ResponseEntity branchChiefHasUsersAssigned(User user)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("branch_chief " + user.getBranchChief().getBranchChiefName() +
                    " branch_chief_cannot_be_removed_because"));
    }
}
