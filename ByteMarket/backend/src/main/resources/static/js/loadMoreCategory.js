$(document).ready(function() {
    let currentPage = $('#load-more').data('current-page');
    let totalPages = $('#load-more').data('total-pages');  
    let category = new URLSearchParams(window.location.search).get("category") || "";
    let search = new URLSearchParams(window.location.search).get("search") || "";

    $('#load-more').on('click', function() {
        if (currentPage + 1 >= totalPages) {
            $('#load-more').hide(); // Ocultar botón si ya no hay más páginas
            return;
        }

        currentPage++;

        $.ajax({
            url: `/products?page=${currentPage}&category=${category}&search=${search}`,
            method: 'GET',
            success: function(data) {
                let newProducts = $(data).find('.otherProducts').html(); // Extraer productos del HTML
                if (newProducts.trim() === '') {
                    $('#load-more').hide(); // Ocultar botón si no hay más productos
                } else {
                    $('#products-list').append(newProducts);
                }
            }
        });
    });
});
