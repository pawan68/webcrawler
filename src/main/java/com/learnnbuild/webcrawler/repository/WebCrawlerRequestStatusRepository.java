package com.learnnbuild.webcrawler.repository;

import com.learnnbuild.webcrawler.entity.WebCrawlerRequestStatusEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebCrawlerRequestStatusRepository extends CrudRepository<WebCrawlerRequestStatusEntity, String> {
}
