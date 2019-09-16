package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.EmailToGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmailToGroupRepository extends CrudRepository<EmailToGroup,Long>
{
    EmailToGroup getEmailToGroupByEmailIdEqualsAndGroupIdEquals(long emailID, long groupId);

    List<EmailToGroup> getEmailToGroupByGroupIdEquals(long groupId);
}
