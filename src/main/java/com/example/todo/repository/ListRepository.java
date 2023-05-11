package com.example.todo.repository;

import com.example.todo.entity.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends CrudRepository<List, Long> {
    Iterable<List> findAllByUserId(Long userId);

    @Query("SELECT l FROM List l WHERE l.user.id = ?#{principal?.id}")
    Iterable<List> findAllByCurrentUser();
}
