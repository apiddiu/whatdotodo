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

import com.aldo.whatdotodo.config.Application;
import com.aldo.whatdotodo.model.Status;
import com.aldo.whatdotodo.model.ToDoItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request
    .SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request
    .SecurityMockMvcRequestPostProcessors.user;
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
    private Gson gson;
    @Autowired
    private MockMvc mvc;

    private final static String TITLE = "test-title";

    private final static String DESCRIPTION = "test-description";
    private final static String UPDATED_TITLE = "test-title-updated";
    private final static String UPDATED_DESCRIPTION = "test-description-updated";
    private final static Status CLOSED = new Status("Closed");
    private final static String ITEM_NOT_FOUND_ID = "-1";
    private final static String UNKNOWN = "unknown";
    public final static String TESTER_USER = "test-user";
    public final static String TESTER_PASSWORD = "test-password";
    public final static String TESTER_ROLE = "TESTER";
    public final static String ADMIN_ROLE = "ADMIN";
    public final static String BASE_API_TEMPLATE = "/api/";
    public static final String ITEMS_TEMPLATE = "items";
    public static final String STATUS_TEMPLATE = "status";

    @Before
    public void setup() throws Exception {
        clearItems();
    }

    @Test
    public void initialGetReturnsEmptyList() throws Exception {
        getItems()
            .andExpect(itemsSize(0));
    }

    @Test
    public void createElement() throws Exception {
        insertItem()
            .andExpect(itemTitle(TITLE))
            .andExpect(itemDescription(DESCRIPTION));

        getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(TITLE, DESCRIPTION));
    }

    @Test
    public void getElement() throws Exception {
        MvcResult insertResult = insertItem()
            .andExpect(itemTitle(TITLE))
            .andExpect(itemDescription(DESCRIPTION))
            .andReturn();

        getItem(fromResult(insertResult).getId())
            .andExpect(itemTitle(TITLE))
            .andExpect(itemDescription(DESCRIPTION));
    }

    @Test
    public void getNonExistingElementProducesNotFoundResult() throws Exception {
        getItemNotFound();
    }

    @Test
    public void deleteElement() throws Exception {
        insertItem()
            .andExpect(itemTitle(TITLE))
            .andExpect(itemDescription(DESCRIPTION));

        MvcResult getResult = getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(TITLE, DESCRIPTION))
            .andReturn();

        deleteItem(firstItem(getResult).getId())
            .andExpect(itemTitle(TITLE))
            .andExpect(itemDescription(DESCRIPTION));

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
            .andExpect(itemTitle(TITLE))
            .andExpect(itemDescription(DESCRIPTION));

        MvcResult getResult = getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(TITLE, DESCRIPTION))
            .andReturn();

        updateItem(firstItem(getResult).getId())
            .andExpect(itemTitle(UPDATED_TITLE))
            .andExpect(itemDescription(UPDATED_DESCRIPTION));

        getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(UPDATED_TITLE, UPDATED_DESCRIPTION));
    }

    @Test
    public void updateNonExistingElementProducesNotFoundResult() throws Exception {
        updateItemNotFound();
    }

    @Test
    public void updateElementStatus() throws Exception {
        insertItem()
            .andExpect(itemTitle(TITLE))
            .andExpect(itemDescription(DESCRIPTION));

        MvcResult getResult = getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(TITLE, DESCRIPTION))
            .andReturn();

        updateItemStatus(firstItem(getResult).getId())
            .andExpect(returnedStatus(CLOSED));

        getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(TITLE, DESCRIPTION, CLOSED));
    }

    @Test
    public void updateNonExistingElementStatusProducesNotFoundResult() throws Exception {
        updateItemStatusItemNotFound();
    }

    @Test
    public void updateStatusWithUnknownStatusProducesNotFoundResult() throws Exception {
        insertItem()
            .andExpect(itemTitle(TITLE))
            .andExpect(itemDescription(DESCRIPTION));

        MvcResult getResult = getItems()
            .andExpect(itemsSize(1))
            .andExpect(itemsContain(TITLE, DESCRIPTION))
            .andReturn();

        updateItemStatusNotFound(firstItem(getResult).getId());
    }

    private ResultActions perform(MockHttpServletRequestBuilder request) throws Exception {
        return mvc.perform(
            request
                .with(user(TESTER_USER).password(TESTER_PASSWORD).roles(TESTER_ROLE))
                .with(csrf())
        );
    }

    private ResultActions performAdmin(MockHttpServletRequestBuilder request) throws Exception {
        return mvc.perform(
            request
                .with(user(TESTER_USER).password(TESTER_PASSWORD).roles(ADMIN_ROLE))
                .with(csrf())
        );
    }

    private ResultActions getItems() throws Exception {
        return perform(get(composePattern(ITEMS_TEMPLATE))).andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions insertItem() throws Exception {
        return perform(
            post(composePattern(ITEMS_TEMPLATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new ToDoItem(TITLE, DESCRIPTION))))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions getItem(Long itemId) throws Exception {
        return perform(get(composePattern(ITEMS_TEMPLATE, itemId.toString())))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions getItemNotFound() throws Exception {
        return perform(get(composePattern(ITEMS_TEMPLATE, ITEM_NOT_FOUND_ID)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions deleteItem(Long itemId) throws Exception {
        return perform(
            delete(composePattern(ITEMS_TEMPLATE, itemId.toString())))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions deleteItemNotFound() throws Exception {
        return perform(
            delete(composePattern(ITEMS_TEMPLATE, ITEM_NOT_FOUND_ID)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions updateItem(Long itemId) throws Exception {
        return perform(
            put(composePattern(ITEMS_TEMPLATE, itemId.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new ToDoItem(UPDATED_TITLE, UPDATED_DESCRIPTION))))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions updateItemNotFound() throws Exception {
        return perform(
            put(composePattern(ITEMS_TEMPLATE, ITEM_NOT_FOUND_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(new ToDoItem(UPDATED_TITLE, UPDATED_DESCRIPTION))))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions updateItemStatus(Long itemId) throws Exception {
        return perform(
            put(composePattern(ITEMS_TEMPLATE, itemId.toString(), STATUS_TEMPLATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(CLOSED)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private ResultActions updateItemStatusItemNotFound() throws Exception {
        return perform(
            put(composePattern(ITEMS_TEMPLATE, ITEM_NOT_FOUND_ID, STATUS_TEMPLATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(CLOSED)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions updateItemStatusNotFound(Long itemId) throws Exception {
        return perform(
            put(composePattern(ITEMS_TEMPLATE, itemId.toString(), STATUS_TEMPLATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    gson.toJson(UNKNOWN)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    private ResultActions clearItems() throws Exception {
        return performAdmin(
            put(composePattern(ITEMS_TEMPLATE)))
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

    private ResultMatcher returnedStatus(Status value) {
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

    private String composePattern(String... patternElements) {
        return BASE_API_TEMPLATE + StringUtils.join(patternElements, "/");
    }
}
