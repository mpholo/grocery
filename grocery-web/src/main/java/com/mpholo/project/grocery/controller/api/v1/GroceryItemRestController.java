package com.mpholo.project.grocery.controller.api.v1;

import com.mpholo.project.grocery.controller.api.v1.dto.GroceryItemPatchRequest;
import com.mpholo.project.grocery.controller.api.v1.dto.GroceryItemResponse;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.GroceryItemUpdateDTO;
import com.mpholo.project.grocery.service.GroceryItemService;
import com.mpholo.project.grocery.service.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * JSON API for grocery items, consumed by the Android app.
 *
 * <ul>
 *   <li>{@code GET  /api/v1/groceryitems?monthlyGroceryId=1} &ndash; list items of a month</li>
 *   <li>{@code GET  /api/v1/groceryitems/{id}} &ndash; a single item</li>
 *   <li>{@code PATCH /api/v1/groceryitems/{id}} &ndash; change quantity / price / basket flag</li>
 * </ul>
 */
@Api("Grocery items API: list and update price, quantity and basket state")
@RestController("GroceryItemApi")
@RequestMapping("/api/v1/groceryitems")
public class GroceryItemRestController {

    private final GroceryItemService groceryItemService;

    public GroceryItemRestController(GroceryItemService groceryItemService) {
        this.groceryItemService = groceryItemService;
    }

    @ApiOperation(value = "List the grocery items of a monthly grocery")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GroceryItemResponse> getItems(@RequestParam("monthlyGroceryId") Integer monthlyGroceryId) {
        return groceryItemService.findByMonthlyGroceryId(monthlyGroceryId)
                .stream()
                .map(GroceryItemResponse::from)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Add a product to a monthly grocery as a new item")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroceryItemResponse createItem(@RequestBody GroceryItemUpdateDTO request) {
        if (request.getQuantity() <= 0) {
            request.setQuantity(1);
        }
        GroceryItemDTO created = groceryItemService.save(request);
        return GroceryItemResponse.from(created);
    }

    @ApiOperation(value = "Get a single grocery item")
    @GetMapping("/{groceryItemId}")
    @ResponseStatus(HttpStatus.OK)
    public GroceryItemResponse getItem(@PathVariable Integer groceryItemId) {
        GroceryItemDTO dto = groceryItemService.findById(groceryItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grocery item not found for id " + groceryItemId));
        return GroceryItemResponse.from(dto);
    }

    @ApiOperation(value = "Update a grocery item's quantity, price and/or basket (purchased) flag")
    @PatchMapping("/{groceryItemId}")
    @ResponseStatus(HttpStatus.OK)
    public GroceryItemResponse patchItem(@PathVariable Integer groceryItemId,
                                         @RequestBody GroceryItemPatchRequest request) {
        GroceryItemDTO updated = groceryItemService.patch(
                groceryItemId,
                request.getQuantity(),
                request.getActualPrice(),
                request.getPurchased());
        return GroceryItemResponse.from(updated);
    }

    @ApiOperation(value = "Remove a grocery item from its monthly grocery")
    @DeleteMapping("/{groceryItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Integer groceryItemId) {
        groceryItemService.deleteById(groceryItemId);
    }
}
