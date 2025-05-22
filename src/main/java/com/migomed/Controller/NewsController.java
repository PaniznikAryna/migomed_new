package com.migomed.Controller;

import com.migomed.Entity.News;
import com.migomed.Service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        News createdNews = newsService.saveNews(news);
        return ResponseEntity.ok(createdNews);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        News updatedNews = newsService.updateNews(id, news);
        return ResponseEntity.ok(updatedNews);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = newsService.getAllNews();
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        News news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<News>> getLatestNews() {
        return ResponseEntity.ok(newsService.getLatestNews());
    }

}
