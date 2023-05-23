package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.used.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAClubRepository extends JpaRepository<Post,Long> {

}
