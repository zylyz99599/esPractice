package com.lyz.es.esspring;

import com.lyz.es.esspring.DAO.ProductDao;
import com.lyz.es.esspring.model.Product;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EsSpringApplicationTests {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private ProductDao productDao;

    @Test
    void contextLoads() {
    }

    @Test
    void deleteIndex() {
        boolean delete = elasticsearchRestTemplate.indexOps(Product.class).delete();
        System.out.println("删除索引" + delete);
    }

    @Test
    void testDao() {
        Product product = new Product(1L, "huawei", "phone", 2999.0, "http://123.com");
        productDao.save(product);
    }

    @Test
    void testUpdate() {
        Product product = new Product(2L, "华为", "phone", 2999.0, "http://123.com");
        productDao.save(product);
    }

    @Test
    void findAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Iterable<Product> all = productDao.findAll(sort);
        for (Product product : all) {
            System.out.println(product);
        }
    }

    @Test
    void findByPage() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        int curPage = 0;
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(curPage, pageSize, sort);
        Page<Product> productPage = productDao.findAll(pageRequest);
        for (Product product : productPage) {
            System.out.println(product);
        }
    }

    @Test
    void delete() {
        Product product = new Product();
        product.setId(2L);
        productDao.delete(product);
    }

    @Test
    void saveAll() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setId(Long.valueOf(i + 1));
            product.setCategory("phone");
            product.setTitle("xiaomi");
            product.setPrice(2499.0);
            product.setImages("test" + String.valueOf(i + 1));
            productList.add(product);
        }
        productDao.saveAll(productList);
    }

    @Test
    void advancedQuery(){
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                //查询条件
                .withQuery(QueryBuilders.queryStringQuery("xiaomi").defaultField("title"))
                //分页
                .withPageable(PageRequest.of(0, 5))
                //排序
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                //高亮字段显示
                .withHighlightFields(new HighlightBuilder.Field("price"))
                .build();
        SearchHits<Product> search = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
        for (SearchHit<Product> productSearchHit : search) {
            System.out.println(productSearchHit.getContent());
        }


    }

}
