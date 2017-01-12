/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aldo.whatdotodo.controller;

import com.aldo.whatdotodo.controller.config.Application;
import com.aldo.whatdotodo.controller.model.ToDoItem;
import com.aldo.whatdotodo.controller.model.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ToDoControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    private static Logger log = LoggerFactory.getLogger(ToDoControllerTests.class);

    private final String title = "test-title";
    private final String description = "test-description";
    private final String updatedTitle = "test-title-updated";
    private final String updatedDescription = "test-description-updated";
    private final Status closed = new Status("Closed");
    private final long itemNotFoundId = -1;
    private final String unknown = "unknown";

    @Before
    public void setup() throws Exception {
        setItems(Lists.emptyList());
    }

    @Test
    public void initialGetReturnsEmptyList() throws Exception {
        getItems()
            .andExpect(itemsSize(0));
    }

    @Test
    public void createElement() throws Exception {
        insertItem()
            .andExpect(itemTitle(title))
            .andExpect(itemDescription(description));

        getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(title, description));
    }

    @Test
    public void getElement() throws Exception {
        MvcResult insertResult = insertItem()
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$.description").value(description))
            .andReturn();

        getItem(fromResult(insertResult).getId())
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    public void getNonExistingElementProducesNotFoundResult() throws Exception {
        getItemNotFound();
    }

    @Test
    public void deleteElement() throws Exception {
        insertItem()
            .andExpect(itemTitle(title))
            .andExpect(itemDescription(description));

        MvcResult getResult = getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(title, description))
            .andReturn();

        deleteItem(firstItem(getResult).getId())
            .andExpect(itemTitle(title))
            .andExpect(itemDescription(description));

        getItems()
            .andExpect(itemsSize(0));
    }

    @Test
    public void deleteNonExistingElementProducesNotFoundResult() throws Exception {
        deleteItemNotFound();
    }

    @Test
    public void updateElement() throws Exception {
        insertItem()
            .andExpect(itemTitle(title))
            .andExpect(itemDescription(description));

        MvcResult getResult = getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(title, description))
            .andReturn();

        updateItem(firstItem(getResult).getId())
            .andExpect(itemTitle(updatedTitle))
            .andExpect(itemDescription(updatedDescription));

        getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(updatedTitle, updatedDescription));
    }

    @Test
    public void updateNonExistingElementProducesNotFoundResult() throws Exception {
        updateItemNotFound();
    }

    @Test
    public void updateElementStatus() throws Exception {
        insertItem()
            .andExpect(itemTitle(title))
            .andExpect(itemDescription(description));

        MvcResult getResult = getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(title, description))
            .andReturn();

        updateItemStatus(firstItem(getResult).getId())
            .andExpect(returnedValue(closed.getName()));

        getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(title, description, closed));
    }

    @Test
    public void updateNonExistingElementStatusProducesNotFoundResult() throws Exception {
        updateItemStatusItemNotFound();
    }

    @Test
    public void updateStatusWithUnknownStatusProducesNotFoundResult() throws Exception {
        insertItem()
            .andExpect(itemTitle(title))
            .andExpect(itemDescription(description));

        MvcResult getResult = getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(title, description))
            .andReturn();

        updateItemStatusNotFound(firstItem(getResult).getId());
    }

    @Test
    public void setAllItems() throws Exception {
        insertItem();

        setItems(Arrays.asList(
            new ToDoItem(),
            new ToDoItem(),
            new ToDoItem()));

        getItems()
            .andExpect(itemsSize(3));
    }

    private ResultActions getItems() throws Exception {
        return this.mockMvc.perform(get("/items")).andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions insertItem() throws Exception {
        return this.mockMvc.perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new ToDoItem(title, description))))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions getItem(Long itemId) throws Exception {
        return this.mockMvc.perform(get("/items/" + itemId))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions getItemNotFound() throws Exception {
        return this.mockMvc.perform(get("/items/" + itemNotFoundId))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions deleteItem(Long itemId) throws Exception {
        return this.mockMvc.perform(
            delete("/items/" + itemId))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions deleteItemNotFound() throws Exception {
        return this.mockMvc.perform(
            delete("/items/" + itemNotFoundId))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions updateItem(Long itemId) throws Exception {
        return this.mockMvc.perform(
            put("/items/" + itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new ToDoItem(updatedTitle, updatedDescription))))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions updateItemNotFound() throws Exception {
        return this.mockMvc.perform(
            put("/items/" + itemNotFoundId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new ToDoItem(updatedTitle, updatedDescription))))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions updateItemStatus(Long itemId) throws Exception {
        return this.mockMvc.perform(
            put("/items/" + itemId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson("Closed")))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions updateItemStatusItemNotFound() throws Exception {
        return this.mockMvc.perform(
            put("/items/" + itemNotFoundId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(closed)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions updateItemStatusNotFound(Long itemId) throws Exception {
        return this.mockMvc.perform(
            put("/items/" + itemId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(unknown)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions setItems(List<ToDoItem> items) throws Exception {
        return this.mockMvc.perform(put("/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(items)))
            .andExpect(status().isOk());
    }

    private ResultMatcher itemTitle(String title) {
        return jsonPath("$.title").value(title);
    }

    private ResultMatcher itemDescription(String description) {
        return jsonPath("$.description").value(description);
    }

    private ResultMatcher itemStatus(Status status) {
        return jsonPath("$.status").value(status.getName());
    }

    private ResultMatcher returnedValue(String value) {
        return jsonPath("$").value(value);
    }

    private ResultMatcher itemsContain(String title, String description) {
        return jsonPath(
            String.format("$.[?(@.title=='%s' && @.description=='%s')]", title, description))
            .exists();
    }

    private ResultMatcher itemsContain(String title, String description, Status status) {
        return jsonPath(
            String.format("$.[?(@.title=='%s' && @.description=='%s' && @.status=='%s')]",
                title, description, status.getName()))
            .exists();
    }

    private ResultMatcher itemsSize(int size) {
        return jsonPath("$", hasSize(size));
    }

    private List<ToDoItem> extractItems(MvcResult result) throws UnsupportedEncodingException {
        return gson.fromJson(result.getResponse().getContentAsString(),
            new TypeToken<ArrayList<ToDoItem>>() {}.getType());
    }

    private ToDoItem firstItem(MvcResult result) throws UnsupportedEncodingException {
        return extractItems(result).get(0);
    }

    private ToDoItem fromResult(MvcResult result) throws UnsupportedEncodingException {
        return gson.fromJson(result.getResponse().getContentAsString(), ToDoItem.class);
    }
}
