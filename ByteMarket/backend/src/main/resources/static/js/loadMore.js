$(document).ready(function() {
    let currentPage = $('#load-more').data('current-page'); 
    let totalPages = $('#load-more').data('total-pages');
    
    if (currentPage === totalPages-1) {  // If there are no more pages, hide button
        $('#load-more').hide();
    }

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
                    $('#load-more').data('current-page', currentPage+1);
                    if (currentPage === totalPages-1) {  // If there are no more pages, hide button
                        $('#load-more').hide();
                    }
                }

            },
            complete: function() {
                $spinner.hide();  
                $button.prop('disabled', false);  
            }
        });
    });
});
