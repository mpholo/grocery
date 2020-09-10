(function($) {
    "use strict";

    //show modal window
//    $('#myModal').on('shown.bs.modal', function () {
//      $('#myInput').trigger('focus')
//    })

//    $('#submitButton').on('click',function(){
//       $("form").submit();
//    });


    //==populate form fields for update ==//
    $("#product-table").on("click",".editProduct",function() {

      console.log("update clicked");

       $("#formModalCenterTitle").text("Update Product");

      var id =$(this).closest("td").find("input#productId").val();
      var name =$(this).closest("tr").find("td").eq(0).text();
      var desc =$(this).closest("tr").find("td").eq(1).text();
      var price =$(this).closest("tr").find("td").eq(2).text();

       console.log("selected product id "+id);
      $("#formProductId").val(id);
      $("#formProductName").val(name);
      $("#formProductDescription").val(desc);
      $("#formProductPrice").val(price);

    });

     $("#addItem").on("click",function() {
            $("#formModalCenterTitle").text("Add Product");
     });

     $("#product-table").on("click",".deleteProduct",function() {
       deleteItem($(this),"td");
     });


})(jQuery);
