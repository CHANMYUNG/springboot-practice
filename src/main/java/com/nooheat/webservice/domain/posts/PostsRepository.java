package com.nooheat.webservice.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by NooHeat on 03/02/2018.
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {

}
