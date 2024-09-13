package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
public class OrderControllerTest {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
    private ItemRepository itemRepository;
	
	@Autowired
	private OrderController orderController;

	@Test
	public void submitOrderSuccessTest() {
		User user = createUser();
		Item item = createItem();
		item = itemRepository.save(item);
		Cart cart = user.getCart();
		cart.addItem(item);
		cart.setUser(user);
		user.setCart(cart);
		userRepository.save(user);
		ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
		UserOrder userOrder = response.getBody();
		assertNotNull(userOrder);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void submitOrderFailedTest() {
		ResponseEntity<UserOrder> response = orderController.submit("xxxxx");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void getOrdersForUserSuccessTest() {
		User user = createUser();
		Item item = createItem();
		Cart cart = user.getCart();
		cart.addItem(item);
		cart.setUser(user);
		user.setCart(cart);

		userRepository.save(user);
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("chuongtp");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void getOrdersForUserFailedTest() {
		User user = createUser();
		Item item = createItem();
		item = itemRepository.save(item);
		Cart cart = user.getCart();
		cart.addItem(item);
		cart.setUser(user);
		user.setCart(cart);

		userRepository.save(user);
		
		
		orderController.submit(user.getUsername());
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Username2");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	public User createUser() {
		User user = new User();
		user.setId(1);
		user.setUsername("chuongtp");
		user.setPassword("123456");

		Cart cart = new Cart();
		cart.setUser(null);
		cart.setItems(new ArrayList<>());
		cart.setTotal(BigDecimal.valueOf(0.0));
		
		cart = cartRepository.save(cart);
		user.setCart(cart);

		return user;
	}

	public static Item createItem() {
		Item item = new Item();
		item.setName("Item 1");
		item.setDescription("Item 1");
		item.setPrice(BigDecimal.valueOf(20.0));
		return item;
	}
}