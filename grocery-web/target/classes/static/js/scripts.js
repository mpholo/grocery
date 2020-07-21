/*!
    * Start Bootstrap - SB Admin v6.0.1 (https://startbootstrap.com/templates/sb-admin)
    * Copyright 2013-2020 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    (function($) {
    "use strict";

    // Add active state to sidbar nav links
    var path = window.location.href; // because the 'href' property of the DOM element is the absolute path
        $("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function() {
            if (this.href === path) {
                $(this).addClass("active");
            }
        });

    // Toggle the side navigation
    $("#sidebarToggle").on("click", function(e) {
        e.preventDefault();
        $("body").toggleClass("sb-sidenav-toggled");
    });

    //show modal window
    $('#myModal').on('shown.bs.modal', function () {
      $('#myInput').trigger('focus')
    })

    $('#submitButton').on('click',function(){
      $("form").submit();
    });


    //==populate form fields for update ==//
    $(".editProduct").on("click",function() {

      var id =$(this).closest("tr").find("td").eq(0).text();
      var name =$(this).closest("tr").find("td").eq(1).text();
      var desc =$(this).closest("tr").find("td").eq(2).text();
      var price =$(this).closest("tr").find("td").eq(3).text();

      $("#formProductId").val(id);
      $("#formProductName").val(name);
      $("#formProductDescription").val(desc);
      $("#formProductPrice").val(price);

    });

})(jQuery);
