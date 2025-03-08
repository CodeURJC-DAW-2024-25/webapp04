$(document).ready(function() { 
    let $button = $('#load-more');
    let currentPage = $button.data('current-page');
    let totalPages = $button.data('total-pages');
    let filter = $button.data('filter'); // Get the value of filter from the button
    
    if (currentPage === totalPages-1) {  // If there are no more pages, hide button
        $('#load-more').hide();
    }

    // Extract the profileId from the URL or use the user's ID if on their own profile
    let profileId = window.location.pathname.split('/')[2];  // Extract the user id from the URL

    if (!profileId) {
        profileId = $('#userId').val();  // Fallback to the current user's ID if not found in the URL
    }

    if (!profileId) {
        console.error('Could not obtain profileId');
        return;  
    }

    // If we are on the last page or there are no more products, hide the button
    if (currentPage >= totalPages || $('.otherProduct').length === 0) {
        $button.hide();
    }

    // When the user clicks the 'load more' button, make the Ajax request
    $button.on('click', function() {
        let $spinner = $('#spinner');
        $spinner.show();
        $button.prop('disabled', true);

        currentPage++;

        let url;

        // Build the request URL based on whether it is for favorites or profile
        if (filter === "favorites") {
            // For favorites we don't need the profileId in the URL
            url = '/profile?page=' + currentPage + (filter ? '&filter=' + filter : '');
        } else {
            // For profile products we use the profileId
            url = '/profile/' + profileId + '?page=' + currentPage + (filter ? '&filter=' + filter : '');
        }

        $.ajax({
            url: url,
            method: 'GET',
            success: function(data) {
                let newProducts = $(data).find('.otherProducts').filter(function() {
                    return $('#products-list .otherProducts[data-id="' + $(this).data('id') + '"]').length === 0;
                });

                // If there are no new products or we have reached the last page, hide the button
                if (newProducts.length === 0 || currentPage >= totalPages) {
                    $button.hide();
                } else {
                    newProducts.appendTo('#products-list'); // Add the new products
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
