package com.soma.domain.group.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.domain.group.dto.response.GroupListResponse;
import com.soma.domain.group.entity.Group;
import com.soma.domain.group.entity.QGroup;
import com.soma.domain.joinlist.entity.JoinList;
import com.soma.domain.joinlist.entity.QJoinList;
import com.soma.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.*;

import static com.soma.domain.joinlist.entity.QJoinList.joinList;


@RequiredArgsConstructor
public class GroupRepositoryCustomImpl implements GroupRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<GroupListResponse> findAllByCursor(Member member, Long groupIdCursor, Pageable pageable) {
        QJoinList subJoinList = new QJoinList("subJoinList");
        QGroup subGroup = new QGroup("subGroup");

        List<Long> groupIds = jpaQueryFactory.select(subJoinList.group.id)
                .from(subJoinList)
                .where(subJoinList.member.eq(member)
                        .and(((subJoinList.group.startDate).lt(getStartDateByGroupId(groupIdCursor, subGroup))
                        .or(((subJoinList.group.startDate).eq(getStartDateByGroupId(groupIdCursor, subGroup))).and((subJoinList.group.id).lt(groupIdCursor))))))
                .orderBy(subJoinList.group.startDate.desc(), subJoinList.group.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<JoinList> result = jpaQueryFactory.selectFrom(joinList)
                .join(joinList.group).fetchJoin()
                .join(joinList.member).fetchJoin()
                .where(joinList.group.id.in(groupIds))
                .fetch();

        ArrayList<GroupListResponse> content = getGroupListResponses(result);
        Collections.sort(content, new GroupListResponseComparator());
        return new SliceImpl<>(content, pageable, isHasNext(pageable, content));
    }

    private static JPQLQuery<LocalDate> getStartDateByGroupId(Long groupIdCursor, QGroup subGroup) {
        return JPAExpressions.select(subGroup.startDate).from(subGroup).where(subGroup.id.eq(groupIdCursor));
    }


    @Override
    public Slice<GroupListResponse> findFirstGroupList(Member member, Pageable pageable) {
        QJoinList subJoinList = new QJoinList("subJoinList");

        List<Long> groupIds = jpaQueryFactory.select(subJoinList.group.id)
                .from(subJoinList)
                .where(subJoinList.member.eq(member))
                .orderBy(subJoinList.group.startDate.desc(), subJoinList.group.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<JoinList> result = jpaQueryFactory.selectFrom(joinList)
                .join(joinList.group).fetchJoin()
                .join(joinList.member).fetchJoin()
                .where(joinList.group.id.in(groupIds))
                .fetch();

        ArrayList<GroupListResponse> content = getGroupListResponses(result);
        Collections.sort(content, new GroupListResponseComparator());
        return new SliceImpl<>(content, pageable, isHasNext(pageable, content));
    }

    private static ArrayList<GroupListResponse> getGroupListResponses(List<JoinList> result) {
        HashMap<Group, ArrayList<JoinList>> map = new HashMap<>();
        for (JoinList list : result) {
            if(!map.containsKey(list.getGroup())){
                map.put(list.getGroup(), new ArrayList<>());
            }
            map.get(list.getGroup()).add(list);
        }

        ArrayList<GroupListResponse> content = new ArrayList<>();
        for (Group group : map.keySet()) {
            content.add(GroupListResponse.toDto(group, map.get(group)));
        }
        return content;
    }

    private static boolean isHasNext(Pageable pageable, ArrayList<GroupListResponse> content) {
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return hasNext;
    }

}

class GroupListResponseComparator implements Comparator<GroupListResponse>{
    @Override
    public int compare(GroupListResponse o1, GroupListResponse o2) {
        if(o1.getStartDate().isBefore(o2.getStartDate()) || (o1.getStartDate().isEqual(o2.getStartDate()) && o1.getGroupId() < o2.getGroupId())){
            return 1;
        }
        return -1;
    }
}

