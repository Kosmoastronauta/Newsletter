package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.EmailGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailGroupRepository extends CrudRepository<EmailGroup, Long>
{}
