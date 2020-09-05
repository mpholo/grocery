(function($) {
    "use strict";

    $('#submitButton').on('click',function(){
       $("form").submit();
    });


    //==populate form fields for update ==//
    $(".editMonthlyGrocery").on("click",function() {

    console.log("update clicked");

      var allValues =$(this).closest(".card-body").find(".data").text().split(',');

      $("#formModalCenterTitle").text("Update Monthly Grocery")

      var id =allValues[0];
      var monthlyGroceryMonth=allValues[1];
      var startDate =allValues[2];
      var endDate =allValues[3];
      var budget =allValues[4];


      $("#formMonmthlyGroceryId").val(id);
      $("#formperiod").val(monthlyGroceryMonth);
      $("#formStartDate").val(startDate);
      $("#formEndDate").val(endDate);
      $("#formbudget").val(budget);

       console.log("allValues "+$(this).closest(".card-body").find(".data").text());
       console.log("monthlyGroceryMonth "+monthlyGroceryMonth);
       console.log("startDate "+startDate);
       console.log("endDate "+endDate);
       console.log("budget "+budget);

    });

     $("#addItem").on("click",function() {
            $("#formModalCenterTitle").text("Add Monthly Grocery");
        });

     $(".btn-group").on("click",".deleteMonthlyGroceries",function() {
         deleteItem($(this),"div");
     });

})(jQuery);
