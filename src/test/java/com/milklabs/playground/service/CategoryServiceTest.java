package com.milklabs.playground.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.milklabs.playground.dao.CategoryDAO;
import com.milklabs.playground.entity.Category;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Mock
  private CategoryDAO categoryDAO;

  private CategoryService service;

  @BeforeEach
  void setUp() {
    service = new CategoryService(categoryDAO);
  }

  @Test
  void findAll_shouldReturnAllFromDao() {
    var c1 = new Category(); c1.setCategoryId(1); c1.setCategoryName("Electronics");
    var c2 = new Category(); c2.setCategoryId(2); c2.setCategoryName("Books");

    when(categoryDAO.findAll()).thenReturn(List.of(c1, c2));

    var result = service.findAll();

    assertEquals(2, result.size());
    assertEquals("Electronics", result.get(0).getCategoryName());
    assertEquals("Books", result.get(1).getCategoryName());
    verify(categoryDAO).findAll();
    verifyNoMoreInteractions(categoryDAO);
  }

  @Test
  void findAllCategory_nullFilter_shouldReturnAll() {
    var c1 = new Category(); c1.setCategoryId(1); c1.setCategoryName("Electronics");
    when(categoryDAO.findAll()).thenReturn(List.of(c1));

    var result = service.findAllCategory(null);

    assertEquals(1, result.size());
    verify(categoryDAO).findAll();
    verify(categoryDAO, never()).findByCategoryName(anyString());
  }

  @Test
  void findAllCategory_emptyFilter_shouldReturnAll() {
    when(categoryDAO.findAll()).thenReturn(List.of());

    var result = service.findAllCategory("");

    assertNotNull(result);
    assertEquals(0, result.size());
    verify(categoryDAO).findAll();
    verify(categoryDAO, never()).findByCategoryName(anyString());
  }

  @Test
  void findAllCategory_nonEmptyFilter_shouldCallFindByCategoryName() {
    var c = new Category(); c.setCategoryId(5); c.setCategoryName("Mobile Phones");
    when(categoryDAO.findByCategoryName("phone")).thenReturn(List.of(c));

    var result = service.findAllCategory("phone");

    assertEquals(1, result.size());
    assertEquals("Mobile Phones", result.get(0).getCategoryName());
    verify(categoryDAO, never()).findAll();
    verify(categoryDAO).findByCategoryName("phone");
  }

  @Test
  void deleteCategory_shouldDelegateToDao() {
    var c = new Category();
    service.deleteCategory(c);

    verify(categoryDAO).delete(c);
    verifyNoMoreInteractions(categoryDAO);
  }

  @Test
  void saveCategory_null_shouldNotCallDaoSave() {
    service.saveCategory(null);

    verify(categoryDAO, never()).save(any(Category.class));
    verifyNoMoreInteractions(categoryDAO);
  }

  @Test
  void saveCategory_nonNull_shouldCallDaoSave() {
    var c = new Category(); c.setCategoryName("Books");

    service.saveCategory(c);

    ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
    verify(categoryDAO).save(captor.capture());
    assertEquals("Books", captor.getValue().getCategoryName());
  }

  @Test
  void findAllPrecedingCategories_whenNotFound_shouldReturnEmpty() {
    when(categoryDAO.findById(999)).thenReturn(Optional.empty());

    var result = service.findAllPrecedingCategories(999);

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(categoryDAO).findById(999);
    verifyNoMoreInteractions(categoryDAO);
  }

  @Test
  void findAllPrecedingCategories_whenChainExists_shouldReturnFromRootToLeaf() {
    // leaf -> parent -> root
    var root = new Category(); root.setCategoryId(1); root.setCategoryName("Electronics");
    var parent = new Category(); parent.setCategoryId(2); parent.setCategoryName("Mobile Phones"); parent.setCategoryParent(root);
    var leaf = new Category(); leaf.setCategoryId(3); leaf.setCategoryName("Smartphones"); leaf.setCategoryParent(parent);

    when(categoryDAO.findById(3)).thenReturn(Optional.of(leaf));

    var result = service.findAllPrecedingCategories(3);

    // service builds [leaf, parent, root] then reverse => [root, parent, leaf]
    assertEquals(3, result.size());
    assertEquals("Electronics", result.get(0).getCategoryName());
    assertEquals("Mobile Phones", result.get(1).getCategoryName());
    assertEquals("Smartphones", result.get(2).getCategoryName());

    verify(categoryDAO).findById(3);
    verifyNoMoreInteractions(categoryDAO);
  }

  @Test
  void findAllPrecedingCategories_whenNoParent_shouldReturnSingle() {
    var root = new Category(); root.setCategoryId(1); root.setCategoryName("Electronics");
    when(categoryDAO.findById(1)).thenReturn(Optional.of(root));

    var result = service.findAllPrecedingCategories(1);

    assertEquals(1, result.size());
    assertEquals("Electronics", result.get(0).getCategoryName());
    verify(categoryDAO).findById(1);
    verifyNoMoreInteractions(categoryDAO);
  }
}
