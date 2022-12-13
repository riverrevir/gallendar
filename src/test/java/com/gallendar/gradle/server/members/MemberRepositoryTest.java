package com.gallendar.gradle.server.members;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.gallendar.gradle.server.members.domain.MemberRole;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.domain.MembersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
public class MemberRepositoryTest {
    @Autowired
    private MembersRepository membersRepository;

    @DisplayName("아이디 존재 여부 확인")
    @Test
    public void isMemberIdTest(){
        //given
        Members member=members();
        membersRepository.save(member);

        //when
        boolean isMemberId= membersRepository.existsById("test");

        //then
        assertThat(isMemberId).isEqualTo(true);
    }

    @DisplayName("이메일 존재 여부 확인")
    @Test
    public void isMemberEmailTest(){
        //given
        Members member=members();
        membersRepository.save(member);

        //when
        boolean isMemberId= membersRepository.existsByEmail("test@co.kr");

        //then
        assertThat(isMemberId).isEqualTo(true);
    }

    private Members members(){
        return Members.builder().id("test").email("test@co.kr").password("test").role(MemberRole.USER).build();
    }
}
