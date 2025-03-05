$(document).ready(function() { 
    let $button = $('#load-more');
    let currentPage = $button.data('current-page');
    let totalPages = $button.data('total-pages');
    let filter = $button.data('filter'); // Obtener el valor de filter del botón

    // Extraer el profileId de la URL o usar el ID del usuario si está en su propio perfil
    let profileId = window.location.pathname.split('/')[2];  // Extrae el id del usuario desde la URL

    if (!profileId) {
        profileId = $('#userId').val();  // Fallback al ID del usuario actual si no se encuentra en la URL
    }

    if (!profileId) {
        console.error('No se pudo obtener el profileId');
        return;  
    }

    // Si estamos en la última página o no hay más productos, esconder el botón
    if (currentPage >= totalPages || $('.otherProduct').length === 0) {
        $button.hide();
    }

    // Cuando el usuario hace clic en el botón 'load more', se hace la petición Ajax
    $button.on('click', function() {
        let $spinner = $('#spinner');
        $spinner.show();
        $button.prop('disabled', true);

        currentPage++;

        let url;

        // Construir la URL de la solicitud en función de si es para favoritos o perfil
        if (filter === "favorites") {
            // Para favoritos no necesitamos el profileId en la URL
            url = '/profile?page=' + currentPage + (filter ? '&filter=' + filter : '');
        } else {
            // Para productos del perfil usamos el profileId
            url = '/profile/' + profileId + '?page=' + currentPage + (filter ? '&filter=' + filter : '');
        }

        $.ajax({
            url: url,
            method: 'GET',
            success: function(data) {
                let newProducts = $(data).find('.otherProducts').filter(function() {
                    return $('#products-list .otherProducts[data-id="' + $(this).data('id') + '"]').length === 0;
                });

                // Si no hay productos nuevos o hemos llegado a la última página, esconder el botón
                if (newProducts.length === 0 || currentPage >= totalPages) {
                    $button.hide();
                } else {
                    newProducts.appendTo('#products-list'); // Añadir los nuevos productos
                }
            },
            complete: function() {
                $spinner.hide();
                $button.prop('disabled', false);
            }
        });
    });
});
