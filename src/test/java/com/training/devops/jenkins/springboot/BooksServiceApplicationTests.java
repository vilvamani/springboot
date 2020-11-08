package com.training.devops.jenkins.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

import com.training.devops.jenkins.springboot.controller.BookController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BooksServiceApplicationTests {

    @Test
    public void testHomeController() {
        BookController bookController = new BookController();
        String result = bookController.home();
        assertEquals(result, "Hello World!");
    }
}
