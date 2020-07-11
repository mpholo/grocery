package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.mapper.ProductMapper;
import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mpholo.project.grocery.util.ProductMappings.PRODUCTURL;

@Service
public class ProductServiceImpl  implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Optional<ProductDTO> findById(Integer id) {

        return Optional.of(productMapper.ProductToProductDTO(
                productRepository.findById(id)
                        .orElseThrow(RuntimeException::new))); //todo implement better exception handling
    }

    @Override
    public List<ProductDTO> findAll() {

        return productRepository.findAll()
                .stream()
                .map(p ->{
                    ProductDTO productDto = productMapper.ProductToProductDTO(p);
                    productDto.setProductUrl(PRODUCTURL+"/"+p.getProductId());
                    return  productDto;
                   }

                )
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
      productRepository.deleteById(id);
    }

    @Override
    public void delete(ProductDTO productDTO) {
       productRepository.delete(productMapper.ProductDTOToProduct(productDTO));
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product product = productMapper.ProductDTOToProduct(productDTO);
        return  saveAndReturnDTO(product);
    }


    @Override
    public ProductDTO findByProductName(String name) {

        ProductDTO productDTO= productMapper.ProductToProductDTO(productRepository.findByProductName(name));
        productDTO.setProductUrl(PRODUCTURL+"/"+productDTO.getProductId());
        return  productDTO;
    }

    private ProductDTO saveAndReturnDTO(Product product) {
        Product saveProduct = productRepository.save(product);
        ProductDTO returnDTo=  productMapper.ProductToProductDTO(saveProduct);
        returnDTo.setProductUrl(PRODUCTURL+product.getProductId());

        return returnDTo;
    }

    @Override
    public ProductDTO edit(Integer id, ProductDTO productDTO) {
        Product product = productMapper.ProductDTOToProduct(productDTO);
        product.setProductId(id);

        return  saveAndReturnDTO(product);


    }

    @Override
    public ProductDTO patchProduct(Integer productId, ProductDTO productDTO) {
        Product product = productMapper.ProductDTOToProduct(productDTO);
        product.setProductId(productId);

        Optional<Product> foundProduct = productRepository.findById(productId);

        if(foundProduct.isPresent()) {
            if (product.getProductName() != null && !product.getProductName().equals(foundProduct.get().getProductName())) {
                        foundProduct.get().setProductName(product.getProductName());
            }
            if (product.getProductDescription() != null && !product.getProductDescription().equals(foundProduct.get().getProductDescription())) {
                foundProduct.get().setProductDescription(product.getProductDescription());
            }
             return  saveAndReturnDTO(foundProduct.get());

        }

        return  productDTO;

    }
}
