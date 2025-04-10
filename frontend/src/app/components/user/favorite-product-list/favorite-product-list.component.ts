import { Component } from '@angular/core';
import { ProductDTO } from '../../../dtos/product.dto';
import { UserService } from '../../../services/user.service';

@Component({
    selector: 'app-favorite-product-list',
    templateUrl: './favorite-product-list.component.html',
    styleUrl: './favorite-product-list.component.css'
})
export class FavoriteProductListComponent {

    //TODO: Revisar esto y unificiarlo con la product-list

    favoriteProducts: ProductDTO[] = [];
    isLast: boolean = false;
    currentPage: number = 0;
    isLoading: boolean = false;
    userId: number = 1;

    constructor(private userService: UserService) { }

    ngOnInit() {
        this.userService.getUser().subscribe({
            next: (user) => {
                this.userId = user.id;
                this.loadFavorites();
            },
            error: (err) => {
                console.error('No se pudo obtener el usuario:', err);
            }
        });
    }

    loadMore(): void {
        this.isLoading = true;
        this.currentPage++;
        this.loadFavorites();
    }

    loadFavorites(): void {
        this.userService.getFavorites(this.userId, this.currentPage).subscribe({
            next: (response) => {
                // Filter products that are not already in favoriteProducts
                const newProducts = response.content.filter(product => 
                    !this.favoriteProducts.some(existingProduct => existingProduct.id === product.id)
                );
    
                // Add the new products (without duplicates) to the favorite products list
                this.favoriteProducts = [...this.favoriteProducts, ...newProducts];
                this.isLast = response.last;
                this.isLoading = false;
            },
            error: (error) => {
                console.error(error);
                this.isLoading = false;
            }
        });
    }
    

    removeFromFavorites(productId: number): void {
        this.userService.deleteFavorite(this.userId, productId).subscribe({
            next: () => {
                this.favoriteProducts = this.favoriteProducts.filter(p => p.id !== productId);
            },
            error: (error) => {
                console.error(error);
            }
        });
    }

    addToFavorites(productId: number): void {
        if (!this.userId || !productId) {
            console.error('User ID or Product ID is missing!');
            return;
        }

        
        if (this.favoriteProducts.some(product => product.id === productId)) {
            console.log('Este producto ya está en tus favoritos.');
            return; 
        }

        this.isLoading = true;

        this.userService.addFavorite(this.userId, productId).subscribe({
            next: () => {
                console.log(`Producto ${productId} añadido a favoritos.`);
                this.loadFavorites(); 
            },
            error: (err) => {
                console.error('Error al añadir a favoritos:', err);
            },
            complete: () => {
                this.isLoading = false;
            }
        });
    }

}
