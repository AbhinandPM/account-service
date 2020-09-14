package com.abhi.account.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.abhi.account.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}
