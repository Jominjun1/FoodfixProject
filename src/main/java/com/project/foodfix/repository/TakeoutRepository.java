package com.project.foodfix.repository;

import com.project.foodfix.model.TakeoutOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeoutRepository extends JpaRepository<TakeoutOrder, Long>  {

}
