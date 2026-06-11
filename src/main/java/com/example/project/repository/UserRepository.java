package com.example.project.repository;

import com.example.project.model.dto.response.UserProjectionDTO;
import com.example.project.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            select new com.example.project.model.dto.response.UserProjectionDTO(u.fullName, u.email, u.phone) from User u
            where u.fullName like %:keyword%
            """)
    Page<UserProjectionDTO> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);
}
