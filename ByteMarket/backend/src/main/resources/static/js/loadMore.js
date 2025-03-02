$(document).ready(function() {
    let currentPage = $('#load-more').data('current-page');  

    $('#load-more').on('click', function() {
        let $button = $(this);
        let $spinner = $('#spinner');
        
        $spinner.show();  
        $button.prop('disabled', true); 
        
        currentPage++;  
        $.ajax({
            url: '/?page=' + currentPage,
            method: 'GET',
            success: function(data) {
                let newProducts = $(data).find('.otherProducts').html();
                
                if ($.trim(newProducts) === '') {  // If there are no products, hide button
                    $button.hide();
                } else {
                    $('#products-list').append(newProducts);
                }
            },
            complete: function() {
                $spinner.hide();  
                $button.prop('disabled', false);  
            }
        });
    });
});
