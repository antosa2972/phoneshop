package com.es.core;


import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.HttpSessionCartService;
import com.es.core.exception.NoElementWithSuchIdException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpCartServiceTest {

    @Mock
    private HttpSession httpSession;

    @Mock
    private PhoneDao jdbcPhoneDao;

    @Mock
    private StockDao jdbcStockDao;

    private Cart cart;
    private Phone phone = new Phone();
    private Stock stock = new Stock();
    private CartItem cartItem;
    private Long nonExistingId = 4L;
    private HashMap<Long, Long> updateMap;
    private List<Phone> outOfStockPhones;
    private Long newQuantity = 5L;
    private Long newQuantityForOutOfStock = 100L;

    @InjectMocks
    private CartService httpSessionCartService = new HttpSessionCartService();

    @Before
    public void setup() {
        cart = new Cart();
        phone.setId(1L);
        phone.setPrice(BigDecimal.valueOf(100L));
        stock.setPhone(phone);
        stock.setStock(10);
        stock.setReserved(1);
        cartItem = new CartItem(phone, 2L, phone.getPrice().multiply(BigDecimal.valueOf(2L)));
        cart.getCartItems().add(cartItem);
        cart.setTotalQuantity(2L);
        cart.setTotalCost(BigDecimal.valueOf(200L));
        updateMap = new HashMap<>();
        outOfStockPhones = new ArrayList<>();
    }

    @Test
    public void testGetCart() {
        when(httpSession.getAttribute(anyString())).thenReturn(cart);
        Cart newCart = httpSessionCartService.getCart(httpSession);
        assertEquals(newCart, cart);
    }

    @Test
    public void testAddPhoneExisting() throws IllegalArgumentException, OutOfStockException {
        when(jdbcPhoneDao.get(anyLong())).thenReturn(Optional.of(phone));
        when(jdbcStockDao.get(anyLong())).thenReturn(Optional.of(stock));
        httpSessionCartService.addPhone(phone.getId(), 2L, cart);
        assertEquals(cart.getTotalCost(), BigDecimal.valueOf(400L));
    }

    @Test
    public void testAddPhoneNew() throws IllegalArgumentException, OutOfStockException {
        cart.getCartItems().clear();
        when(jdbcPhoneDao.get(anyLong())).thenReturn(Optional.of(phone));
        when(jdbcStockDao.get(anyLong())).thenReturn(Optional.of(stock));
        httpSessionCartService.addPhone(phone.getId(), 2L, cart);
        assertEquals(cart.getTotalCost(), BigDecimal.valueOf(200L));
    }

    @Test(expected = OutOfStockException.class)
    public void testAddPhoneThrowsException() throws IllegalArgumentException, OutOfStockException {
        when(jdbcPhoneDao.get(anyLong())).thenReturn(Optional.of(phone));
        when(jdbcStockDao.get(anyLong())).thenReturn(Optional.of(stock));
        httpSessionCartService.addPhone(phone.getId(), newQuantityForOutOfStock, cart);
    }

    @Test
    public void testRemove() throws NoElementWithSuchIdException {
        httpSessionCartService.remove(phone.getId(), cart);
        assertEquals(cart.getTotalCost(), BigDecimal.valueOf(0L));
    }

    @Test(expected = NoElementWithSuchIdException.class)
    public void testRemoveThrowsException() throws NoElementWithSuchIdException {
        httpSessionCartService.remove(nonExistingId, cart);
    }

    @Test
    public void testUpdate() {
        updateMap.put(phone.getId(), newQuantity);
        when(jdbcStockDao.get(anyLong())).thenReturn(Optional.of(stock));
        List<Phone> outOfStockPhones = httpSessionCartService.update(updateMap, cart);
        assertEquals(cart.getTotalCost(), BigDecimal.valueOf(500L));
        assertTrue(outOfStockPhones.isEmpty());
    }
}
