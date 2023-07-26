package com.uk.bootintegrationall.jpa.entity.repository;

import com.uk.bootintegrationall.jpa.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description TODO
 */
//public interface CustomerRepository extends Repository {
//public interface CustomerRepository extends CrudRepository<Customer, Long> {
//public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    CustomerOnlyName findByName(String name);
}
