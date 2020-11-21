(function($) {
    "use strict";


    //global variables
    var newItem;

    $('#submitButton').on('click',function(){
       $("form").submit();
    });

   //remove added product on form close
    $('#formModalCenter').on('hidden.bs.modal', function (e) {
         $("select#formproduct option[value="+newItem+"]").remove();
          console.log("remove "+newItem+" from products");
    })


    //==populate form fields for update ==//
    $("#grocery-item-table").on("click",".editGroceryItem",function() {

      console.log("update clicked");

      $("#formModalCenterTitle").text("Update Grocery Item")


     var productName =$(this).closest("tr").find("td").eq(0).text();
     var productId =$(this).closest("tr").find("td").eq(0).find("input.productId").val();
     var price =$(this).closest("tr").find("td").eq(2).text();
     var quantity =$(this).closest("tr").find("td").eq(3).text();
     var id =$(this).closest("tr").find("td").eq(5).find("input.groceryItemId").val();

      $("#groceryItemId").val(id);
      $("#formPrice").val(price);
      $("#formQuantity").val(quantity);

      // Set the value, creating a new option if necessary
      if($("select#formproduct").find("option[value='" + productId + "']").length) {
          $("select#formproduct").val(productName).trigger('change');
      } else {
        // Create a DOM Option and pre-select by default

           var newOption = new Option(productName,productId,true,true);
           newOption.setAttribute("data-price",price);
           $("select#formproduct").append(newOption).trigger('change');
            newItem=productId;
      }


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
