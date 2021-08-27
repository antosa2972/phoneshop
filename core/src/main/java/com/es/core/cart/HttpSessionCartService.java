package com.es.core.cart;

import com.es.core.model.phone.*;
import com.es.core.exception.OutOfStockException;
import org.springframework.stereotype.Service;

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
    public synchronized Cart getCart(HttpSession httpSession) {
        Cart cart = (Cart) httpSession.getAttribute(CART_SESSION_ATTR);
        if (cart == null) {
            cart = new Cart();
            httpSession.setAttribute(CART_SESSION_ATTR, cart);
        }
        return cart;
    }

    @Override
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
        jdbcStockDao.update(phone.getId(), stock.getStock() - quantity, stock.getReserved() + quantity);
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
    public synchronized void update(Map<Long, Long> items, Cart cart) {
        items.keySet().stream()
                .map(phoneId -> findCartItem(phoneId, cart))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(cartItem -> cartItem.setQuantity(items.get(cartItem.getPhone().getId())));
        calculateCart(cart);
    }

    @Override
    public synchronized void remove(Long phoneId, Cart cart) {
        Optional<CartItem> optionalCartItem = findCartItem(phoneId, cart);
        optionalCartItem.ifPresent(cartItem -> cart.getCartItems().remove(cartItem));

        Optional<Stock> optionalStock = jdbcStockDao.get(phoneId);
        if (optionalStock.isPresent() && optionalCartItem.isPresent()) {
            Stock stock = optionalStock.get();
            CartItem cartItem = optionalCartItem.get();
            jdbcStockDao.update(phoneId, stock.getStock() + cartItem.getQuantity(),
                    stock.getReserved() - cartItem.getQuantity());
        }
        calculateCart(cart);
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
