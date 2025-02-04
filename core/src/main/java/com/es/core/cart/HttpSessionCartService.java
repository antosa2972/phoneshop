package com.es.core.cart;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {
    public static final String CART_SESSION_ATTR = "cart";
    @Resource
    private PhoneDao jdbcPhoneDao;
    @Resource
    private StockDao jdbcStockDao;

    @Override
    @Transactional(readOnly = true)
    public synchronized Cart getCart(HttpSession httpSession) {
        Cart cart = (Cart) httpSession.getAttribute(CART_SESSION_ATTR);
        if (cart == null) {
            cart = new Cart();
            httpSession.setAttribute(CART_SESSION_ATTR, cart);
        }
        return cart;
    }

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public synchronized void addPhone(Long phoneId, Long quantity, Cart cart) throws OutOfStockException, IllegalArgumentException {
        Optional<Phone> optionalPhone = jdbcPhoneDao.get(phoneId);
        Optional<Stock> optionalStock = jdbcStockDao.get(phoneId);

        if (optionalPhone.isPresent() && optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            Phone phone = optionalPhone.get();
            if (stock.getStock() - stock.getReserved() >= quantity) {
                addToCart(quantity, stock, phone, cart);
            } else {
                throw new OutOfStockException();
            }
        } else {
            throw new IllegalArgumentException("No stock or phone in data base");
        }
    }

    private void addToCart(Long quantity, Stock stock, Phone phone, Cart cart) {
        jdbcStockDao.update(phone.getId(), stock.getStock().longValue(), stock.getReserved() + quantity);
        Optional<CartItem> cartItem = findCartItem(phone.getId(), cart);
        if (cartItem.isPresent()) {
            CartItem existingCartItem = cartItem.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            BigDecimal price = phone.getPrice().multiply(BigDecimal.valueOf(quantity));
            cart.getCartItems().add(new CartItem(phone, quantity, price));
        }
        calculateCart(cart);
    }

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public synchronized void update(Map<Long, Long> items, Cart cart) {
        items.keySet().stream()
                .map(phoneId -> findCartItem(phoneId, cart))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(cartItem -> {
                    Long phoneId = cartItem.getPhone().getId();
                    Long quantity = items.get(cartItem.getPhone().getId());
                    Long quantityDifference = quantity - cartItem.getQuantity();
                    if (checkQuantity(phoneId, quantityDifference)) {
                        cartItem.setQuantity(cartItem.getQuantity() + quantityDifference);
                    }
                });
        calculateCart(cart);
    }
    private boolean checkQuantity(Long phoneId, Long quantityDifference) {
        Optional<Stock> optionalStock = jdbcStockDao.get(phoneId);
        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            if (stock.getStock() - stock.getReserved() >= quantityDifference) {
                jdbcStockDao.update(phoneId, stock.getStock().longValue(),
                        stock.getReserved() + quantityDifference);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public synchronized void remove(Long phoneId, Cart cart) {
        Optional<CartItem> optionalCartItem = findCartItem(phoneId, cart);
        if (optionalCartItem.isPresent()) {
            cart.getCartItems().remove(optionalCartItem.get());
        } else {
            throw new IllegalArgumentException(phoneId.toString());
        }

        Optional<Stock> optionalStock = jdbcStockDao.get(phoneId);
        if (optionalStock.isPresent() && optionalCartItem.isPresent()) {
            Stock stock = optionalStock.get();
            CartItem cartItem = optionalCartItem.get();
            jdbcStockDao.update(phoneId, stock.getStock() + cartItem.getQuantity(),
                    stock.getReserved() - cartItem.getQuantity());
        }
        calculateCart(cart);
    }

    @Override
    public void deleteCart(HttpSession httpSession) {
        httpSession.removeAttribute("cart");
    }


    private Optional<CartItem> findCartItem(Long phoneId, Cart cart) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getPhone().getId().equals(phoneId))
                .findFirst();
    }

    private void calculateCart(Cart cart) {
        Long totalQuantity = cart.getCartItems().stream()
                .mapToLong(CartItem::getQuantity)
                .sum();
        cart.setTotalQuantity(totalQuantity);
        BigDecimal totalCost = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getPhone().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalCost(totalCost);
    }
}
