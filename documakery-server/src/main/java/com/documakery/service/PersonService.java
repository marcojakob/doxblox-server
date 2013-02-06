package com.documakery.service;

import java.util.List;

import com.documakery.dto.Person;

/**
 * Service to handle Persons.
 * By nidi.
 */
public interface PersonService {
    List<Person> getAllPersons();

    void addPerson(Person person);

    void deletePerson(int id);
}