package com.migomed.Service;

import com.migomed.Entity.News;
import com.migomed.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    public News updateNews(Long id, News updatedNews) {
        News existing = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found with id " + id));
        existing.setHeadline(updatedNews.getHeadline());
        existing.setPublicationDate(updatedNews.getPublicationDate());
        existing.setContent(updatedNews.getContent());
        existing.setPhotos(updatedNews.getPhotos());
        return newsRepository.save(existing);
    }

    public void deleteNews(Long id) {
        News existing = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found with id " + id));
        newsRepository.delete(existing);
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found with id " + id));
    }

    public List<News> searchNewsByHeadline(String headline) {
        return newsRepository.findByHeadlineContainingIgnoreCase(headline);
    }
}
