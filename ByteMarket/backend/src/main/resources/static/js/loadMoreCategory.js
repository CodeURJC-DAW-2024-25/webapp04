$(document).ready(function() {
    let currentPage = $('#load-more').data('current-page');
    let totalPages = $('#load-more').data('total-pages');  
    let category = new URLSearchParams(window.location.search).get("category") || "";
    let search = new URLSearchParams(window.location.search).get("search") || "";
    
    if (currentPage === totalPages-1) {  // If there are no more pages, hide button
        $('#load-more').hide();
    }

    $('#load-more').on('click', function() {
        if (currentPage + 1 >= totalPages) {
            $('#load-more').hide(); // Hide button if no more pages
            return;
        }

        let $button = $(this);
        let $spinner = $('#spinner');

        $spinner.show();  
        $button.prop('disabled', true);  
        
        currentPage++;

        $.ajax({
            url: `/products?page=${currentPage}&category=${category}&search=${search}`,
            method: 'GET',
            success: function(data) {
                let newProducts = $(data).find('.otherProducts').html(); 
                if (newProducts.trim() === '') {
                    $('#load-more').hide(); // Hide button if no more products
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
