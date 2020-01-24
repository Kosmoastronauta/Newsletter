package com.kosmoastronauta.newsletter.repository;

import com.kosmoastronauta.newsletter.domain.EmailAddress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailRepository extends CrudRepository<EmailAddress, Long>
{
     EmailAddress getEmailAddressesByIdEquals(long id);

     EmailAddress getEmailAddressByPubKeyEquals(String pubKey);

     EmailAddress getEmailAddressByAddressEquals(String address);

     @Query(value = "SELECT email_address.id, email_adress.address, email_address.group_id " +
             "FROM " + "email_address" +
             " INNER JOIN email_group" + " " +
             "ON " + "email_address.group_id = " + "email_group.id " +
             "WHERE " + "email_group.name =:groupName", nativeQuery = true)
     List<EmailAddress> getListOfEmailAddressesByGroupNameEquals(String groupName);
}
