package com.minhduong.data.repository;

import com.minhduong.data.entity.Guest;
import org.springframework.data.repository.PagingAndSortingRepository;


import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends PagingAndSortingRepository<Guest, Long> {

}