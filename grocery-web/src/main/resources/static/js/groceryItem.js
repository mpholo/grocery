(function($) {
    "use strict";

    $('#submitButton').on('click',function(){
       $("form").submit();
    });


    //==populate form fields for update ==//
    $("#grocery-item-table").on("click",".editGroceryItem",function() {

      console.log("update clicked");

      $("#formModalCenterTitle").text("Update Grocery Item")

      var id =$(this).closest("tr").find("td").eq(0).text();
     var productId =$(this).closest("tr").find("td").eq(1).find("input.productId").val();
      var price =$(this).closest("tr").find("td").eq(3).text();
      var quantity =$(this).closest("tr").find("td").eq(4).text();

      $("#groceryItemId").val(id);
      $("select#formproduct").val(productId); // Select the option with a value of productId
      $("select#formproduct").trigger("change"); // Notify any JS components that the value changed
      $("#formPrice").val(price);
      $("#formQuantity").val(quantity);

      console.log("groceryItemId "+id);
      console.log("product id "+productId);


    });

    $("#addItem").on("click",function() {
        $("#formModalCenterTitle").text("Add Grocery Item");
    });

    //looping in the table to add numbers
    var totalPrice1=0;
    var totalQuantity=0;
    var totalPrice2=0
    $("table tbody tr").each(function() {
      totalPrice1+=parseFloat($(this).find("td").eq(3).text());
      totalQuantity+=parseInt($(this).find("td").eq(4).text());
      totalPrice2+=parseFloat($(this).find("td").eq(5).text());
    })

    //displaying total in the footer of table
    $("table tfoot tr").find("td").eq(3).text("R "+parseFloat(totalPrice1).toFixed(2));
    $("table tfoot tr").find("td").eq(4).text(totalQuantity);
    $("table tfoot tr").find("td").eq(5).text("R "+parseFloat(totalPrice2).toFixed(2));


    $("#grocery-item-table").on("click",".deleteGroceryItem",function() {
          deleteItem($(this),"td");
   });

//set price of select product
  $("select#formproduct").on("change",function() {
       var price = $(this).children("option:selected").data("price");
       console.log("price = "+price);
       $("#formPrice").val(price);
  });

})(jQuery);
