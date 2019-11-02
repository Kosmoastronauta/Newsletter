package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import com.kosmoastronauta.newsletter.domain.EmailGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailGroupRepository extends CrudRepository<EmailGroup, Long>
{
    EmailGroup getEmailGroupByNameEquals(String name);

    boolean existsEmailGroupByNameEquals(String name);
}
