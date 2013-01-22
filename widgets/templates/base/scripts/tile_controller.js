$('#tile').live('pageinit',function(event) { 
    $('#tile').click(function() {
        window.widget.views.openUrl("${widget.id}", null, "_blank");
    });
});
