package com.soma.domain.notification.service;


import com.soma.domain.group.entity.Group;
import com.soma.domain.notification.NotificationMessage;
import com.soma.domain.notification.dto.request.NotificationCreateRequest;
import com.soma.exception.group.GroupNotFoundException;
import com.soma.exception.member.FCMTokenNotFoundException;
import com.soma.exception.member.MemberNotFoundException;
import com.soma.domain.group.repository.GroupRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.domain.notification.dto.request.MemberUpdateFcmTokenRequest;
import com.soma.domain.notification.dto.response.MemberUpdateFcmTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static com.soma.common.constant.Status.ACTIVE;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private static Random random = new Random();
    private static List<NotificationMessage> notificationMessages = List.of(NotificationMessage.values());

    @Transactional
    public MemberUpdateFcmTokenResponse updateFcmToken(MemberUpdateFcmTokenRequest request, String email){
        Member member = memberRepository.findByEmailAndStatus(email, ACTIVE).orElseThrow(MemberNotFoundException::new);
        member.updateFcmToken(request.getFcmToken());
        return MemberUpdateFcmTokenResponse.toDto(member);
    }


    @Transactional(noRollbackFor = FCMTokenNotFoundException.class)
    public void create(NotificationCreateRequest request, String email) {
        log.error("request.getFcmToken: {}", request.getFcmToken());
        log.error("request.getGroupId: {}", request.getGroupId());
        log.error("request.getToUserId: {}", request.getToUserId());
        Group group = groupRepository.findById(request.getGroupId()).orElseThrow(GroupNotFoundException::new);
        Member fromMember = memberRepository.findByEmailAndStatus(email, ACTIVE).orElseThrow(MemberNotFoundException::new);
        fromMember.updateFcmToken(request.getFcmToken());
        Member toMember = memberRepository.findByIdAndStatus(request.getToUserId(), ACTIVE).orElseThrow(MemberNotFoundException::new);
        if(toMember.getFcmToken() == null){
            throw new FCMTokenNotFoundException();
        }
        int idx = random.nextInt(notificationMessages.size());

        firebaseCloudMessageService.sendByToken(toMember.getFcmToken(), notificationMessages.get(idx).getTitleWithNicknameAndGroupName(group.getName(), fromMember.getName()), notificationMessages.get(idx).getBody());
    }
}