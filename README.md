# webapp04

# <p align="center"> 🌐 ByteMarket 🌐</p>
# 📑Index
- [Phase 0](#-phase-0)
   - [Team members](#-team-members)
   - [Entities](#-entities)
   - [Types of users](#-types-of-users)
   - [User requirements](#-user-requirements)
   - [Additional Technologies](#-additional-technologies)
   - [Charts](#-charts)
   - [Advanced Algorithms](#-advanced-algorithms)
   - [Images](#-images)
- [Phase 1](#%EF%B8%8Fphase-1)
   - [Screens](#screens)

***   
# 🌀 Phase 0
***
 

## 👥 Team members
| Name  | URJC Email| GitHub nickname |
| ------------- | ------------- | ----------- |
| Olga Chubinova Bortsova | o.chubinova.2022@alumnos.urjc.es | [@chubi0l](https://github.com/chubi0l) |
| Marcos García García | m.garciaga.2022@alumnos.urjc.es  | [@marcosgrc](https://github.com/marcosgrc) |
| Naroa Martín Simón | n.martins.2022@alumnos.urjc.es  | [@NaroaMS04](https://github.com/NaroaMS04) |
| Adrián Muñoz Serrano  | a.munozse.2022@alumnos.urjc.es  | [@adri04ms](https://github.com/adri04ms) |


## 👣 Entities
- User
- Product
- Reviews
- Purchase
- Chat
  
 The entities *user* and *product* will have **images** as attributes.
 Moreover, the entitie *user* will have **saved products** as an attribute.
 
 The following diagram shows the system's entities, and how they relate to each other. 
  ##### img: Entity-Relationship Diagram
  ![*1.1 entities*](./Capturas/ER-DIAGRAM.png) 



## ⚧️ Types of users

 - **Anonymus** : users who have not logged into a registered account. They can access basic functionalities as viewing products, searching products and filter products.
 - **Registered** :  users who have logged into a registered account. They can access a wide variety of funcionalities like buying, selling, reviewing, saving products they like and also they can message other users.
 - **Admin** :  type of user who has control over the platform, having the most extensive permissions. They are also allowed to ban usual users if they use offensive language or they publish explicit content.
   
## 🔎 User requirements

| Requierement | Anonymus| Registered | Admin |
| ------------- | ------------- | ----------- | --------- |
|   View products  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   Search product  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   View product details  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   View profile information (registered users)  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   Create reviews  | | <p align="center"> ☑️ </p>  | |
|   Save product  | | <p align="center"> ☑️ </p>  | |
|   Buy product  | | <p align="center"> ☑️ </p>  | |
|   Sell product  | | <p align="center"> ☑️ </p>  | |
|   Message seller  | | <p align="center"> ☑️ </p>  | |
|   Modify profile information  | | <p align="center"> ☑️ </p>  | |
|   Modify product details  | | <p align="center"> ☑️ </p>  | |
|   Delete published product  | | <p align="center"> ☑️ </p>  | |
|   View graphics  | | <p align="center"> ☑️ </p>  | <p align="center"> ☑️ </p>  |
|   Delete products (from other users)  | | | <p align="center"> ☑️ </p>  |
|   Delete reviews (from other users)  | | | <p align="center"> ☑️ </p>  |
|   Block users  | | | <p align="center"> ☑️ </p>  |
|   Create/Modify tags  | | | <p align="center"> ☑️ </p>  |

## 🖱️ Additional Technologies
- **Discount email**: users will receive emails when the price of a saved product gets lower.
-  **Verification email**: users will receive an email when they buy or sell products with all the details.
-  **Google Maps**: users could use this aplication to locate the address of the seller (to see if the transportation management is suits them).
  
## 📊 Charts
- **Personal sell chart**: users will be able to visualize a sell chart of the products they have selled throughout the year.
- **Top sell products chart**: users will be able to visualize a sales chart of the top-selling products in a certain category.

## ⚙️ Advanced algorithms
- **Recommend by best seller**: This algorithm will choose products of the higher-rated seller first.
- **Recommend by category**: This algorithm will choose the products to be featured in a registered user's home page based on products the user has searched for or buyed.

## 🎯 Images
-  **User profile**: allow users to upload an image to their profile.
-  **User banner**: allow users to upload a banner to their profile.
-  **Products photos**: allow users to upload images to their products.

***
# 🌀 Phase 1
***

## 🖥️ Screens

### 🏠 Home Screen
This screen will show to whoever the access to webpage. This image represents the anonymus user's header (depending on the user, the header will change), from which you can either login or sign in. Also, any user have access to view the details of the products. 
![*2.1 Home Screen*](./Capturas/Home.PNG) 

###  Product detail Screen
Screen showing information about the product for sale (price, description) and the user selling it. It also offers the possibility to add the product to favorites or chat with the seller.
![*2.2.1 Details Screen*](./Capturas/Detalle1.PNG)
![*2.2.2 Details Screen*](./Capturas/Detalle2.PNG)

If the user has purchased from this seller before, they will have the additional options to post a review or a report the seller.
![*2.2.1 Details Screen*](./Capturas/Detalle3-Reporte.PNG)
![*2.2.2 Details Screen*](./Capturas/Detalle3-Reseña.PNG)

###  Login/Sign in Screen
Depending on whether the user is registered or not, one of the two screens will be displayed. The login screen is shown only to registered users and in case they are not registered, the registration screen will be shown.
![*2.1 Login Screen*](./Capturas/IniciarSesión.PNG)
![*2.1 Sign in Screen*](./Capturas/Registro.PNG)

###  Chat Screen

![*2.1 Chat Screen*](./Capturas/Chat.PNG)

###  Reports Screen

![*2.1 Reports Screen*](./Capturas/Reporte.PNG)

###  Profile Screen

![*2.1 Profile Screen*](./Capturas/Perfil.PNG)
 
