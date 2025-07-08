package at.sebastianhamm.backend.services;

import at.sebastianhamm.backend.models.Member;
import at.sebastianhamm.backend.payload.response.MemberResponse;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    List<MemberResponse> getAllMembers();

    Optional<Member> getMemberById(Long id);
    MemberResponse createMember(Member member);
    MemberResponse updateMember(Long id, Member member);
    void deleteMember(Long id);
    List<Member> findByInstrument(String instrument);
    List<Member> findBySection(String section);
    MemberResponse mapToResponse(Member member);
}
