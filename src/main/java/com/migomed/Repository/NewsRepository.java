package com.migomed.Repository;

import com.migomed.Entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByHeadlineContainingIgnoreCase(String headline);
    List<News> findAllByOrderByPublicationDateDesc();
}
