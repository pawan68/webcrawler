package com.learnnbuild.webcrawler.repository;

import com.learnnbuild.webcrawler.entity.WebCrawlerResponseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebCrawlerResponseRepository extends CrudRepository<WebCrawlerResponseEntity, String> {
}
