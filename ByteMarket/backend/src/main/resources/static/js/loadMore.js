$(document).ready(function() {
    let currentPage = $('#load-more').data('current-page');  // Obtenemos el valor de currentPage desde el atributo data

    $('#load-more').on('click', function() {
        currentPage++;  // Incrementar la página para la próxima carga
        $.ajax({
            url: '/?page=' + currentPage,  // Pasar el parámetro de página
            method: 'GET',
            success: function(data) {
                // Obtener los nuevos productos y agregarlos al contenedor
                let newProducts = $(data).find('.otherProducts').html();
                $('#products-list').append(newProducts); // Agregar productos al final
            }
        });
    });
});
