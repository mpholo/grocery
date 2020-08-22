(function($) {
    "use strict";

    //show modal window
//    $('#myModal').on('shown.bs.modal', function () {
//      $('#myInput').trigger('focus')
//    })

    $('#submitButton').on('click',function(){
       $("form").submit();
    });


    //==populate form fields for update ==//
    $(".editGroceryItem").on("click",function() {

      console.log("update clicked");

      var id =$(this).closest("tr").find("td").eq(0).text();
      //var name =$(this).closest("tr").find("td").eq(1).text();
      var productId =$(this).closest("tr").find("td").eq(1).find("input.productId").val();
      var price =$(this).closest("tr").find("td").eq(3).text();
      var quantity =$(this).closest("tr").find("td").eq(4).text();

      $("#groceryItemId").val(id);
      $("select#formproduct").val(productId);
      $("#formPrice").val(price);
      $("#formQuantity").val(quantity);

      console.log("groceryItemId "+id);
      console.log("product id "+productId);


    });

})(jQuery);
