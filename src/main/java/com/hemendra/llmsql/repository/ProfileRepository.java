package com.hemendra.llmsql.repository;

import com.hemendra.llmsql.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, String> {
}
