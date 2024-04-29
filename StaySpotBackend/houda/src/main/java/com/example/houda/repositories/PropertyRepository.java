package com.example.houda.repositories;

import com.example.houda.entities.Property;
import com.example.houda.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property,Integer> {
    List<Property> findByUser(User user);
}
