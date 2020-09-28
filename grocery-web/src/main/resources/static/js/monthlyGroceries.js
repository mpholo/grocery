(function($) {
    "use strict";

    $('#submitButton').on('click',function(){
       $("form").submit();
    });


    //==populate form fields for update ==//
    $(".editMonthlyGrocery").on("click",function() {

    console.log("update clicked");

      var allValues =$(this).closest(".card-body").find(".data").text().split(',');
      var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

      $("#formModalCenterTitle").text("Update Monthly Grocery")

      var id =allValues[0];
      var monthlyGroceryMonth=allValues[1];
      var startDate =allValues[2];
      var endDate =allValues[3];
      var budget =allValues[4];
      var month = monthlyGroceryMonth.split(' ')[0];
      var year = monthlyGroceryMonth.split(' ')[1];
      var monthNumber;
      console.log("Month to update "+month);
      for(var i=0;i<months.length;i++) {
         console.log(i+" Month "+months[i]);
         if(months[i]==month) {
           console.log("month number "+i);
           var number=i+1;
           monthNumber=number<10?'0'+number:number;
         }
      }

      $("#formMonmthlyGroceryId").val(id);
      $("#formperiod").val(year+'-'+monthNumber);
      $("#formbudget").val(budget);

      console.log("allValues "+$(this).closest(".card-body").find(".data").text());
      console.log("monthlyGroceryMonth "+monthlyGroceryMonth);
      console.log("budget "+budget);

    });

     $("#addItem").on("click",function() {
            $("#formModalCenterTitle").text("Add Monthly Grocery");
        });

     $(".btn-group").on("click",".deleteMonthlyGroceries",function() {
         deleteItem($(this),"div");
     });

      $(".yearpicker").yearpicker({
                         year: getParameter('year')==null?parseInt((new Date).getFullYear()):parseInt(getParameter('year')),
                         startYear: 2020,
                         endYear:  parseInt((new Date).getFullYear())
                       });


      $(".yearpicker").on("change",function() {
         console.log("selected year: "+ $(this).val());
         console.log("url parameter year: "+getParameter('year'));
         console.log("current year: "+(new Date).getFullYear());

         window.location.href="?year="+$(this).val();
      });



})(jQuery);

function getParameter(name){
              var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
              if (results==null) {
                 return null;
              }
              return decodeURI(results[1]) || 0;
          }
