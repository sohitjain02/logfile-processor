package com.application.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {

}
