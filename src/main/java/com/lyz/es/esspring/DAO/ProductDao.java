package com.lyz.es.esspring.DAO;

import com.lyz.es.esspring.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public interface ProductDao extends ElasticsearchRepository<Product, Long> {
}
