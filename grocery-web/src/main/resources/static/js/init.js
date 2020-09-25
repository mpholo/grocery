(function ($) {
  $(function () {
 var alert= $("#alert").val();
 var msg;

 if(alert=="deleted") {
    msg="Record deleted successfully."
  }
  else if(alert=="updated") {
    msg="Record updated successfully."
  }
  else if(alert=="saved") {
      msg="Record saved successfully."
    }

  if(alert!="") {
     const body = document.body;
     body.onload = myFunction(msg);
   }

    $('.snackbar-callback').click(function() {
      Snackbar.show({
        text: 'I have a custom callback when action button is clicked.',
        width: '475px',
        onActionClick: function(element) {
          //Set opacity of element to 0 to close Snackbar
          $(element).css('opacity', 0);
          Snackbar.show({
            text: 'Thanks for clicking the  <strong>Dismiss</strong>  button!',
            showActionButton: false
          });
        }
      });
    });

    $('.button-collapse').sideNav();
    $('.scrollspy').scrollSpy();
  }); // end of document ready

})(jQuery); // end of jQuery name space


function myFunction(msg) {

      //e.preventDefault();
      var pos = 'top-right';
      var actionText = $(this).attr('data-actionText');
      var actionColor = $(this).attr('data-actionColor');
      if (actionText == null)
        actionText = 'Dismiss'
      if (actionColor == null)
        actionColor = '#f66496';
      var showAction = $(this).attr('data-showAction');
      if (showAction == null)
        showAction = true;
      else if (showAction == 'true')
        showAction = true;
      else
        showAction = false;

      Snackbar.show({
        text: msg,
        height: '10px',
        showAction: showAction,
        actionText: actionText,
        actionTextColor: actionColor,
        backgroundColor: '#232323',
        width: 'auto',
        pos: pos
      });

  }