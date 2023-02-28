package spring_cloud_msa.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_cloud_msa.catalogservice.domain.Catalog;
import spring_cloud_msa.catalogservice.repository.CatalogRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogService {

    private final CatalogRepository catalogRepository;

    public List<Catalog> getAllCatalog() {
        return catalogRepository.findAll();
    }
}
