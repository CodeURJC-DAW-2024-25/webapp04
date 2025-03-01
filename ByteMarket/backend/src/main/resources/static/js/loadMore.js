$(document).ready(function() {
    let currentPage = $('#load-more').data('current-page');  // Obtain the value of the currentPage from atribute data

    $('#load-more').on('click', function() {
        currentPage++;  // Increments the page for the next charge
        $.ajax({
            url: '/?page=' + currentPage,  // Pasar el parámetro de página
            method: 'GET',
            success: function(data) {
                // Obtain new products and add them to container
                let newProducts = $(data).find('.otherProducts').html();
                $('#products-list').append(newProducts); // Add products to final
            }
        });
    });
});
