package com.milklabs.playground.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.milklabs.playground.dao.ProductDAO;
import com.milklabs.playground.entity.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductDAO productDAO;

  private ProductService service;

  @BeforeEach
  void setUp() {
    service = new ProductService(productDAO);
  }

  @Test
  void findAll_shouldReturnAllFromDao() {
    var p1 = new Product(); p1.setProductId(1); p1.setProductName("Amazon Kindle");
    var p2 = new Product(); p2.setProductId(2); p2.setProductName("Apple Iphone");

    when(productDAO.findAll()).thenReturn(List.of(p1, p2));

    var result = service.findAll();

    assertEquals(2, result.size());
    assertEquals("Amazon Kindle", result.get(0).getProductName());
    assertEquals("Apple Iphone", result.get(1).getProductName());
    verify(productDAO).findAll();
    verifyNoMoreInteractions(productDAO);
  }

  @Test
  void findAllProduct_nullFilter_shouldReturnAll() {
    when(productDAO.findAll()).thenReturn(List.of());

    var result = service.findAllProduct(null);

    assertNotNull(result);
    verify(productDAO).findAll();
    verify(productDAO, never()).findByProductName(anyString());
  }

  @Test
  void findAllProduct_emptyFilter_shouldReturnAll() {
    when(productDAO.findAll()).thenReturn(List.of());

    var result = service.findAllProduct("");

    assertNotNull(result);
    verify(productDAO).findAll();
    verify(productDAO, never()).findByProductName(anyString());
  }

  @Test
  void findAllProduct_nonEmptyFilter_shouldCallFindByProductName() {
    var p = new Product(); p.setProductId(1); p.setProductName("Amazon Kindle");
    when(productDAO.findByProductName("kindle")).thenReturn(List.of(p));

    var result = service.findAllProduct("kindle");

    assertEquals(1, result.size());
    assertEquals("Amazon Kindle", result.get(0).getProductName());
    verify(productDAO, never()).findAll();
    verify(productDAO).findByProductName("kindle");
  }

  @Test
  void deleteProduct_shouldDelegateToDao() {
    var p = new Product();
    service.deleteProduct(p);

    verify(productDAO).delete(p);
    verifyNoMoreInteractions(productDAO);
  }

  @Test
  void saveProduct_null_shouldNotCallDaoSave() {
    service.saveProduct(null);

    verify(productDAO, never()).save(any(Product.class));
    verifyNoMoreInteractions(productDAO);
  }

  @Test
  void saveProduct_nonNull_shouldCallDaoSave() {
    var p = new Product(); p.setProductName("Sapiens");

    service.saveProduct(p);

    ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
    verify(productDAO).save(captor.capture());
    assertEquals("Sapiens", captor.getValue().getProductName());
  }
}
