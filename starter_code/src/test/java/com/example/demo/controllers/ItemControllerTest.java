package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
public class ItemControllerTest {

	@Autowired
	private ItemController itemController;
	
	@Autowired
	private ItemRepository itemRepository;

	@Test
	public void findItemByIdSuccessTest() {
		Item item1 = newItem("item", "test", "1000");
		item1 = itemRepository.save(item1);
		ResponseEntity<Item> responseEntity = itemController.getItemById(item1.getId());

		compareItem(item1, responseEntity);

		Item item = responseEntity.getBody();

		assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
		assertNotNull(item);
		assertEquals(item1.getId(),item.getId());
	}

	@Test
	public void findItemByIdFailedTest() {
		ResponseEntity<Item> responseEntity = itemController.getItemById(999L);

		assertEquals(responseEntity.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void findItemByNameSuccessTest() {
		Item item1 = newItem("item1", "test", "1000");
		Item item2 = newItem("item2", "test", "1000");
		Item item3 = newItem("item3", "test", "1000");

		itemRepository.save(item1);
		itemRepository.save(item2);
		itemRepository.save(item3);

		ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("item2");

		List<Item> items = responseEntity.getBody();

		assertEquals(responseEntity.getStatusCodeValue(), HttpStatus.OK.value());
		assertNotNull(items);
		assertEquals(1, items.size());
	}

	@Test
	public void findItemByNameFailedTest() {
		Item item1 = newItem("item1", "test", "1000");
		Item item2 = newItem("item2", "test", "1000");
		itemRepository.save(item1);
		itemRepository.save(item2);
		ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("item3");

		assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
	}

	@Test
	public void findAllItemsTest() {
		Item item1 = newItem("item1", "test", "1000");
		Item item2 = newItem("item2", "test", "1000");
		Item item3 = newItem("item3", "test", "1000");
		itemRepository.save(item1);
		itemRepository.save(item2);
		itemRepository.save(item1);
		itemRepository.save(item3);
		ResponseEntity<List<Item>> responseEntity = itemController.getItems();

		assertEquals(responseEntity.getStatusCodeValue(), 200);
	}

	private static Item newItem(String name, String descriotion, String price) {
		Item item = new Item();
		item.setName(name);
		item.setDescription(descriotion);
		item.setPrice(new BigDecimal(price));

		return item;
	}

	private static void compareItem(Item item1, ResponseEntity<Item> item2) {
		Assertions.assertEquals(item1.getId(), item2.getBody().getId());
		Assertions.assertEquals(item1.getName(), item2.getBody().getName());
		Assertions.assertEquals(item1.getDescription(), item2.getBody().getDescription());
		Assertions.assertEquals(item1.getPrice(), item2.getBody().getPrice());
	}
}