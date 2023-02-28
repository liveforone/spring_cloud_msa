package spring_cloud_msa.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring_cloud_msa.catalogservice.service.CatalogService;

@RestController
@RequiredArgsConstructor
public class CatalogController {

    private final Environment environment;
    private final CatalogService catalogService;

    @GetMapping("/health-check")
    public String status() {
        return "Port Num : " + environment.getProperty("local.server.port");
    }

    @GetMapping("/catalogs")
    public ResponseEntity<?> getCatalogs() {
        return ResponseEntity.ok(catalogService.getAllCatalog());
    }
}
