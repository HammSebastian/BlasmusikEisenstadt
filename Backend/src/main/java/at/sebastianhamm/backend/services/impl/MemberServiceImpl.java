package at.sebastianhamm.backend.services.impl;

import at.sebastianhamm.backend.exception.ConflictException;
import at.sebastianhamm.backend.models.Member;
import at.sebastianhamm.backend.payload.response.MemberResponse;
import at.sebastianhamm.backend.payload.response.RemarkResponse;
import at.sebastianhamm.backend.repository.MemberRepository;
import at.sebastianhamm.backend.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public MemberResponse createMember(Member member) {
        if (member.getId() != null || memberRepository.existsById(member.getId())) {
            throw new ConflictException("Member with id " + member.getId() + " already exists");
        }

        Member saved = memberRepository.save(member);
        return mapToResponse(saved);
    }

    @Override
    public MemberResponse updateMember(Long id, Member member) {
        return memberRepository.findById(id).map(existing -> {
                    existing.setName(member.getName());
                    existing.setInstrument(member.getInstrument());
                    existing.setSection(member.getSection());
                    existing.setJoinDate(member.getJoinDate());
                    existing.setAvatarUrl(member.getAvatarUrl());
                    existing.setRemarks(member.getRemarks());
                    return memberRepository.save(existing);
                }).map(this::mapToResponse)
                .orElseThrow(() -> new ConflictException("Member with id " + id + " does not exist"));
    }

    @Override
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ConflictException("Member with id " + id + " does not exist");
        }

        memberRepository.deleteById(id);
    }

    @Override
    public List<Member> findByInstrument(String instrument) {
        return memberRepository.findMemberByInstrument(instrument);
    }

    @Override
    public List<Member> findBySection(String section) {
        return memberRepository.findMemberByInstrument(section);
    }

    @Override
    public MemberResponse mapToResponse(Member member) {
        if (member == null) {
            return null;
        }
        
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .instrument(member.getInstrument())
                .section(member.getSection())
                .joinDate(member.getJoinDate())
                .avatarUrl(member.getAvatarUrl())
                .remarks(member.getRemarks().stream()
                        .map(RemarkResponse::from)
                        .collect(Collectors.toSet()))
                .build();
    }
}
