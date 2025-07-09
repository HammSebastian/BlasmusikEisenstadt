package at.sebastianhamm.backend.controller;

import at.sebastianhamm.backend.exception.ResourceNotFoundException;
import at.sebastianhamm.backend.models.user.Member;
import at.sebastianhamm.backend.payload.response.MemberResponse;
import at.sebastianhamm.backend.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/public/members") @RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MemberResponse> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MemberResponse getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(memberService::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse createMember(@Valid @RequestBody Member member) {
        return memberService.createMember(member);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.OK)
    public MemberResponse updateMember(@PathVariable Long id, @Valid @RequestBody Member member) {
        return memberService.updateMember(id, member);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REPORTER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
    }
}
