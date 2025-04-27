# webapp04

# <p align="center"> 🔶 ByteMarket 🔶</p>

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
- Image
- Report
- Message
 
 The following diagram shows the system's entities, and how they relate to each other. 
  ##### img: Entity-Relationship Diagram
  ![*1.1 entities*](./Screenshots/Diagrams/ER%20Diagram.jpg) 



## ⚧️ Types of users

 - **Anonymous** : users who have not logged into a registered account. They can access basic functionalities as viewing products, searching products and filter products.
 - **Registered** :  users who have logged into a registered account. They can access a wide variety of functionalities like buying, selling, reviewing, saving products they like and also they can message other users.
 - **Admin** :  type of user who has control over the platform, having the most extensive permissions. They are also allowed to ban users if they use offensive language, publish explicit content or any other action that could end up in a ban.
   
## 🔎 User requirements

| Requirement | Anonymous| Registered | Admin |
| ------------- | ------------- | ----------- | --------- |
|   View products  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   Search product  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   View product details  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   View profile information (registered users)  | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p> | <p align="center"> ☑️ </p>  |
|   Create reviews  | | <p align="center"> ☑️ </p>  | |
|   Create reports  | | <p align="center"> ☑️ </p>  | |
|   Save product  | | <p align="center"> ☑️ </p>  | |
|   Buy product  | | <p align="center"> ☑️ </p>  | |
|   Sell product  | | <p align="center"> ☑️ </p>  | |
|   Message seller  | | <p align="center"> ☑️ </p>  | |
|   Modify profile information  | | <p align="center"> ☑️ </p>  | |
|   Modify product details  | | <p align="center"> ☑️ </p>  | |
|   Delete own product  | | <p align="center"> ☑️ </p>  | |
|   View graphics  | | <p align="center"> ☑️ </p>  |  |
|   Delete products (from other users)  | | | <p align="center"> ☑️ </p>  |
|   Delete reviews (from other users)  | | | <p align="center"> ☑️ </p>  |
|   Delete users  | | | <p align="center"> ☑️ </p>  |
|   See and solve reports  | | | <p align="center"> ☑️ </p>  |

## 🖱️ Additional Technologies
- **Email**: users will receive emails when their favorite product is sold.
-  **Google Maps**: Users can use this application to locate the seller's address and check proximity.
  
## 📊 Charts
- **Personal sales chart**: users will be able to visualize a sales chart of the products they have sold throughout the year.
- **Personal purchase chart**: users will be able to visualize a buy chart of the products they have bought throughout the year.

## ⚙️ Advanced algorithms
- **Recommend by best seller**: This algorithm will choose the products to be featured in a registered user's home page based on higher-rated seller first.

## 🎯 Images
-  **User profile**: allow users to upload an image to their profile.
-  **Products photos**: allow users to upload images to their products.

***
# 🌀 Phase 1
***

## 🖥️ Screens

### 🏠 Home Screen
This screen will be shown to anyone who accesses the webpage. This image represents the anonymous user's header (depending on the user, the header will change), from which you can either login or sign up. Also, any user have access to view the details of the products. 
Everybody has the same access to all products, only the header changes.
| User | Admin | Anonymous |
| :----: | :------: | :------: |
| ![*2.1.1 Home Screen*](./Screenshots/Updated/homeUser.png) | ![*2.1.2 Home Screen*](./Screenshots/Updated/admin/homeAdmin.jpg) | ![*2.1.3 Home Screen*](./Screenshots/Updated/homeAnonimus.png) |

###  🔍 Searcher Screen
All type of users can search products, only the header will change.
| Product exists | No product |
| :----: | :------: |
| ![*2.14.1 Searcher Screen*](./Screenshots/Updated/navbar.jpg) | ![*2.14.2 Searcher Screen*](./Screenshots/Updated/seachProductError.jpg) | 

###  📂 Category filter Screen
All type of users can search products, only the header will change.
![*2.15 Category Screen*](./Screenshots/Updated/admin/category.png) 

###  🎮 Product detail Screen
Screen showing information about the product for sale (price, description) and the user selling it. It also offers the possibility to add the product to favorites or chat with the seller. If the user has purchased from this seller before, they will have the additional options to post a review or a report the seller.

#### Product detail of other user 
Users can add this product to 'favorites' to save this product or can contact the seller to ask questions about the product.
Admins can only delete the product.
Everybody can see the seller profile throughout the profile photo.
| User | Admin | Anonymous |
| :----: | :------: | :------: |
| ![*2.2.1 Details Screen*](./Screenshots/Updated/othersProductDetail.jpg) | ![*2.2.2 Details Screen*](./Screenshots/Updated/admin/detail.jpg) | ![*2.2.3 Details Screen*](./Screenshots/Updated/admin/anonimusDetail.png) |

#### Product detail of bought product (user) 
![*2.2.3 Details Screen*](./Screenshots/Updated/boughtProductDetail.png)

#### Product detail of own product 
![*2.2.4 Details Screen*](./Screenshots/Updated/ownProductDetail.jpg)


###  👨‍💻 Login/Sign up Screen
Depending on whether the user is registered or not, one of the two screens will be displayed. The login screen is shown only to registered users and in case they are not registered, the registration screen will be shown.
![*2.3.1 Login Screen*](./Screenshots/Updated/login.png)
![*2.3.2 Sign up Screen*](./Screenshots//Updated/register.png)

###  ✉️ Chat Screen
If a buyer wants to ask the seller questions about the product, they can chat. The buyer should contact the seller so that the seller can proceed with the sale.
![*2.4 Chat Screen*](./Screenshots/Updated/Chat.jpg)

###  🚫 Reports Screen
Admin users can manage the reports from this screen.
![*2.5 Reports Screen*](./Screenshots/Updated/admin/reports.jpg)

###  👤 Profile Screen
All type of users can see other profiles. But only registered users and admins will be able to see their own profiles. 

#### Profile of other user 
| User | Admin | Anonymous |
| :----: | :------: | :------: |
| ![*2.6.1.1 Profile Screen*](./Screenshots/Updated/othersProfile.jpg) | ![*2.6.1.2 Profile Screen*](./Screenshots/Updated/admin/otherProfile1.jpg) | ![*2.6.1.3 Profile Screen*](./Screenshots/Updated/admin/otherProfile1Anonimus.png) |

#### Own profile 
| User | Admin |
| :----: | :------: |
| ![*2.6.2.1 Profile Screen*](./Screenshots/Updated/ownProfile.png) | ![*2.6.2.2 Profile Screen*](./Screenshots/Updated/admin/ownProfile.jpg) | 

###  ✏️ Edit profile Screen
Some user (registered) information can be modified. This screen is only shown in the own profile of each registered user (nobody else can see it).

| User | Admin |
| :----: | :------: |
| ![*2.7.1 Edit profile Screen*](./Screenshots/Updated/editProfile.png) | ![*2.7.2 Edit profile Screen*](./Screenshots/Updated/admin/editProfile.jpg) | 


###  ❤️ Favorite products Screen
Registered users can add products to their favorites, which will be displayed in their favorite section. This screen is only shown in the own profile of each registered user (nobody else can see it).

| No empty | Empty |
| :----: | :------: |
| ![*2.8.1 Edit profile Screen*](./Screenshots/Updated/favorites.jpg) | ![*2.8.2 Edit profile Screen*](./Screenshots/Updated/emptyFavorites.jpg) | 

###  🛒 Sell product Screen
If a registered user wants to sell a product, this screen will be shown. The user should fill all sections of the form.

| Normal | Error (no fill) |
| :----: | :------: |
| ![*2.9.1 Sell product Screen*](./Screenshots/Updated/newProduct.png) | ![*2.9.2 Sell product Screen*](./Screenshots/Updated/newProductError.png) |

###  ✏️ Edit product
Only the owners of the product can edit all the parameters.
![*2.16 Edit product Screen*](./Screenshots/Updated/editProduct.png)

###  🔍 User Reviews Screen
All type of users can see the reviews of other users in their profile. Only admins will be able to delete users reviews.

| User | Admin | Anonymous |
| :----: | :------: | :------: |
| ![*2.10.1 User Reviews Screen*](./Screenshots/Updated/admin/othersReviewsUser.png) | ![*2.10.2 User Reviews Screen*](./Screenshots/Updated/admin/othersReviewsAdmin.png) | ![*2.10.3 User Reviews Screen*](./Screenshots/Updated/admin/othersReviewsAnonimus.png) |

### 📊 Sales History  Screen
The products that were sold will be shown in this screen. This screen is only shown in the own profile of each registered user (nobody else can see it).
![*2.11 Sales History Screen*](./Screenshots/Updated/saleHistory.jpg)

### 📜 Purchase History Screen
The purchased products will be shown in this screen. This screen is only shown in the own profile of each registered user (nobody else can see it).
![*2.12 Purchase History Screen*](./Screenshots/Updated/purchaseHistory.jpg)

 
### 📺 Screens Flowchart
For simplicity, the arrows pointing to the "Favorites," "Sell," and "Chat" pages have been omitted since the navigation bar remains the same.
All options under "View Profile," such as history, favorites, and account editing, are private. This means they are only visible from your own account and cannot be accessed when viewing other users' profiles.

- ⚪️ For Anonymous Users
- 🟢 For Registered Users
- 🟠 All type of Users
- 🔵 Admin   
 
 ![*2.13 Flowchart Screen*](./Screenshots/Updated/flowchart.png)

 ***   
# 🌀 Phase 2  - Web with Spring Boot and MySQL
***

## 🛠️ Execution Instructions

# 🚀 Steps 
1. Download this repository
2. Check Requirements 
3. Configure Database
4. Configure IDE
5. Run Application in the IDE
6. Go to https://localhost:8443/

# 📌 Requirements
- Java: JDK 21 → [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- MySQL: v 8.0.28 (Explained in Database Configuration)
- Maven: 4.0.0
- Spring Boot 3.4.2
- IDE (explained in IDE Configuration)

# 🗄️ Database Configuration
- Download MySQL v.8.0.28
- Select default port (port 3306)
- Create a user with name `root` and password `"password"` with DB admin as user role
- Configure MySQL Server as a Windows Service
- Grant full access to the user
- Create a new Schema named `bytemarket` in the server using MySQL Workbench

# 🛠️ IDE Configuration
- We have used Visual Studio Code
- Install Maven and Spring for your IDE

# 🔐 Sample Users
## 👑 Admin
- **Email:** sara@gmail.com
- **Password:** Password1234
  
## 👤 User
- **Email:** pedro@gmail.com
- **Password:** 12341234

## 👤 User
- **Email:** hugo@gmail.com
- **Password:** securePass456

## 👤 User
- **Email:** alex@gmail.com
- **Password:** securePass1234

## 🗃️ Diagrams

### 🏛️ Database Diagram
![*3.1. DataBase*](./Screenshots/Diagrams/BD%20Diagram.jpg) 

### 📌 Classes and Templates Diagram
![*3.2. JavaClass*](./Screenshots/Diagrams/Class%20Diagram.jpg) 

## ⚙️ Members Participation

### 👤 Olga Chubinova Bortsova
I was responsible for the filtering of products by categories and the search functionality. Additionally, I added AJAX to load more products on all pages except the main one. I was in charge of adding all the default initialized products. Furthermore, I developed the recommendation algorithm for the main page.

At the beginning of the project, I modified most of the templates to work with Mustache and created several models along with their controllers, service, and repository. Additionally, I fixed permission issues between user types, including editing and viewing profiles, among others.

| Commit | Description |
| :----: | :---------: |
| [1º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/4245f60fddbd6747087313ccbeabc606873b19d7) | Filter by category and searcher with AJAX refreshing integration |
| [2º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/d56ae86885e7ba4797e5d020725eefadb347a886) | Both navbars and profile views with user restrictions functional |
| [3º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/ed3e581aac8d4e1f9a1adbd24f7e848faa2420ff) | Recommended products |
| [4º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/49b4473dd69503395801ccecebb602728c9a2c80) | Templates upgraded and models created |
| [5º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/f736e4a62efe176da38766c2549f15aeb2a21f16) | Data base initializer products |

| File | Name |
| :----: | :---------: |
| [1º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/DataBaseInitializer.java) | DataBaseInitializer |
| [2º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/ProductService.java) | Product Service |
| [3º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProfileController.java) | Profile Controller |
| [4º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProductController.java) | Product Controller |
| [5º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/ReviewService.java) | Review Service |


### 👤 Marcos García García
I colaborated in the creation of the model classes and the definition of their dependencies for the database.

I was responsible for everything related to image upload, storage and display, as well as the Image model and its relations, needed for the correct storage of the images, and a way of getting the images from the database for display. I implemented this functionality for product creation and edition of multiple images, and for the user's profile picture. In addition, I developed the functionality for editing the user profile information (except for the map) and the rest of the products attributes. 

I developed the charts showing user stats (number of sales and purchases) over time and their style. Beside all this, I also made other changes and additions to the project such as some templates and styling, and other minor changes and fixes.

| Commit | Description |
| :----: | :---------: |
| [1º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/905774803863853dca6f9f6760852833eafea400) | Uploading, storing and showing multiple images for products | 
| [2º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/264dee19f3fb5b2b62dad0b0136140d9edec144b) | Adding and deleting images when editing a product as well as editing the rest of the attributes |
| [3º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/9d234141816e9d0d6fb3f9e0ba4c9155bca20259) | Charts showing user stats over time |
| [4º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/d18e17bbf77f99dee6601d37fed8f2f766ebba9c) | Editing profile information (except map) and profile pic |
| [5º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/da4c6529f7af16de5f581c33faf9aa191c1d48ea) | Calculating and showing rating in the product detail page and first version of delete account |

| File | Description |
| :----: | :---------: |
| [1º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProductController.java) | Product Controller |
| [2º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProfileController.java) | Profile Controller |
| [3º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/UserService.java) | User Service |
| [4º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/ProductService.java) | Product Service |
| [5º](ByteMarket/backend/src/main/java/es/grupo04/backend/service) | Image Service |

### 👤 Naroa Martín Simón
I have been responsible for implementing user registration in the application, ensuring a smooth experience by adding validation for the sign-in form both on the frontend and backend.

Additionally, I have managed users' favorite products, allowing them to add or remove items from their favorites section. I also set up the email notification system to alert users when one of their favorite products is either sold to another user or removed.

I have implemented the functionality to delete products, both from the seller's side and from the administrator’s side. I also implemented the whole account deletion process. This deletion process ensures that the sales and products already sold by the user remain intact, but other elements such as reviews, reports, and available products are removed when the account is deleted.

Furthermore, I have also managed the creation of sales/purchases in the application, ensuring that once a product is sold, it cannot be resold. Lastly, I have implemented the ability for users to view public profiles of other users, further enhancing the social interaction within the platform.

| Commit | Description |
| :----: | :---------: |
| [1º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/8000fbe1024c3ef2c1998e264d4f06ca126e766f) | User Registration with Validation in Frontend and Backend |
| [2º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/a9f2eec2a4221d44c160f8689acb170f866cf5dd) | Add and delete favorites from users who are not owners profile only |
| [3º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/6d88a7a1f76e2ea7d4adbe13d0b0021fbe70730d) | Send mail after favorite product delete |
| [4º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/62e0bb1a60143cb8cdf2e86b744b4258e489b064) | Sell products between users and some template fixes |
| [5º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/5d81e9681ed08f4b8c6e8e6d79a806f09d1514e4) | Public Profile and Account Delete |

| File | Description |
| :----: | :---------: |
| [1º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProductController.java) | Product Controller |
| [2º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/UserWebController.java) | User Web Controller |
| [3º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/PurchaseService.java) | Purchase Service |
| [4º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/ProductService.java) | Product Service |
| [5º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/UserService.java) | User Service |

### 👤 Adrián Muñoz Serrano
I participated in the initial creation of the classes and the definition of their dependencies, as well as developing their respective repositories and a DataBaseInitializer with test objects to facilitate website testing. I implemented the login functionality, profile display, and logout. I also unified all templates related to products to improve design consistency.

On the main screen, I added a "Load More" button with AJAX, allowing products to load progressively in blocks of eight. I developed the selling functionality, enabling users to add products. Additionally, I implemented a chat system between two users to facilitate the purchase of a specific product.

I incorporated the option to report purchased products, with their corresponding display in the administration panel. I also developed the rating system, allowing buyers to rate sellers, with their scores visible both on the user's profile and in a carousel within the product details screen.

I added the option for users to set a location through an interactive map that generates an iframe when clicked, making it visible on both their profile and the product details screen. Finally, I migrated the project from the H2 database to MySQL.

| Commit | Description |
| :----: | :---------: |
| [1º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/1f23678c6888d09f79262df1ac1f95f2bc0747fb) | newProduct (without images) |
| [2º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/b5f943ce92234835b806c3bcbf9ff58f8dc4b043) | create chat (without messages) |
| [3º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/6d6aa0c2d9203f27f8b3f7bd6419525687999daf) | Messages in Chats |
| [4º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/791561868aceb6cbdf681fa82047ccf95c5601a1) | NewReport and ShowReports (only for admin) |
| [5º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/1d078f84aad184e08f31af992b9e13cedbf56975) | add reviews and show reviews in productDetail and in Profile |

| File | Description |
| :----: | :---------: |
| [1º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ChatController.java) | Chat Controller |
| [2º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProductController.java) | Product Controller |
| [3º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/MessageService.java) | Message Service |
| [4º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/ReviewService.java) | Review Service |
| [5º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/ChatService.java) | Chat Service |

# ⚙️ Posible actions to perform in the application
## Anonymous User
Any user, whether logged in the application or registered or not in the application, will have access to the following actions:
### Login
Allows users to log in with their credentials (email and password) to access their account. If the accounts exists and the credentials are correct, they will log in and have access to additional features. 

### Register
Allows users to create a new account in the application to access personalized features explained later. If there is already an account linked to the mail, it is not allowed to create a second account.

### View the main page
Any user has access and can navigate through the main page of the application. It shows the products for sale from other users of the application. In addition, the products shown at the top are products of the top rated users. 

### Search for products
The application offers two alternative methods for searching products. One method uses a search bar, allowing users to type and search for specific products. The other method is category-based, enabling users to browse and find products within a specific category. The search bar is accessible from most pages of the application, while the category-based search is only available on the main page.

### View individual products
Users can view individual products, which includes detailed information such as descriptions, images, pricing, and availability. Additionally, they can read reviews left by previous buyers, offering insights into the quality of the product and the reliability of the seller.

### View other users profile
Users can view other registered users profiles, which include details such as the number of sales, purchases, and reviews they have received. They can also browse the products listed by the user and read the reviews left about them, providing a comprehensive view of the user's reputation and offerings.

## Registered User
### Logout
Registered users can log out of their account at any time by selecting the logout option. This will securely end their session and prevent unauthorized access to their account, ensuring privacy and security. The logout option is accessible from the users profile.

### Favorites
A registered user can mark products as favorites to save items they’re interested in. This allows them to easily access these products later. If a favorite product is sold or deleted, the user will receive an email notification to inform them about the change, keeping them updated on the status of their favorite items.These favorite products can be accessed at any time through the user's profile or the "Favoritos" button included in the header.

### Sell products
Registered users can sell products by filling out a form that allows them to provide detailed information about the product. The form includes fields for product name, description, price, category, and images. Once the product is submitted, it will be listed in the marketplace for other users to browse and purchase.

### Chats
Registered users can communicate with sellers and buyers through a chat feature. This allows them to ask questions, negotiate prices, and finalize transactions. The chat interface is simple to use and accessible from the product pages or the header.

### Edit profile
Users can update their profile to change personal information, such as their name, address, profile picture, or password. This allows for easy management of account details.

### Edit products
Registered users can edit the products they have listed for sale. This feature allows them to update product details such as price, description, images, and category. 

### Delete Products
Registered users can delete products they no longer want to sell.

### Delete Account
Registered users can delete their account. This will also delete all the unsold products, reviews and reports associated with the deleted user.

### Purchase and Sale History 
Logged-in users have access to separate histories for both their purchases and sales. Each history includes graphical representations of sold and purchased products, providing users with a visual overview of their activity. Users can also view the products they’ve purchased or sold. Users can also access detailed information about the products they’ve purchased or sold, allowing them to explore further details as needed.

### Leave a review
Once a user has purchased a product from another, they can leave reviews for the seller. This reviews will help other buyers assess the seller's reliability and product quality. The feedback left by buyers will impact the seller's reputation and overall rating.

### Report a user
Users can report a seller directly from the product they bought (in the purchase history) if the product does not meet expectations or if there are any issues with the transaction. These reports are submitted to the admins, who will review the situation and take appropriate action.

## Admin
### Logout
Registered users can log out of their account at any time by selecting the logout option. This will securely end their session and prevent unauthorized access to their account, ensuring privacy and security. The logout option is accessible from the users profile.

### Manage Reports
Admins can manage reports submitted by users regarding other users. They can review, investigate, and take appropriate actions based on the nature of the reports, ensuring the platform remains safe and compliant with its policies.

### Delete Account
Admins have the authority to delete other users' accounts when necessary. This action permanently removes the user's profile, including their products, reviews, and associated data, in accordance with platform guidelines and policies.

# 📈 Product Display Algorithm
The algorithm used to display recommended products on the main page works as follows:
1. **Seller Ratings Calculation**:  
   First, the algorithm calculates the average rating for each seller based on the reviews they’ve received. If a seller has no ratings, their average rating defaults to 0.

2. **Rank Products by Recency**:  
   For each seller, the products they own are ranked by their publication date. The algorithm assigns a rank to each product, starting from the most recently published, and limits the ranking to the top 3 products per seller.

3. **Filter Unsolved Products**:  
   The algorithm filters out products that have already been sold, ensuring that only unsold products are considered for display.

4. **Sort by Seller Rating and Publication Date**:  
   Once the products are ranked and filtered, they are sorted in descending order by the seller's average rating. If two products have the same rating, they are then sorted by the publication date, with the most recently published products appearing first.

5. **Final Product List**:  
   The result is a list of products where each seller’s top 3 most recent unsold products are shown, prioritized first by the seller's average rating and then by the publication date.

This approach ensures that users see highly rated products from active sellers while considering the recency of the product listings.



***   
# 🌀 Phase 3 - Integration of a REST API into the web application, deployment with Docker, and remote deployment.
***

## 📄 API REST Documentation (OpenAPI)
REST API documentation is automatically generated and is aviable in the following documents:

- **OpenAPI Specification (YAML):** [api-docs.yaml](https://github.com/CodeURJC-DAW-2024-25/webapp04/blob/main/ByteMarket/backend/api-docs/api-docs.yaml)
- **HTML Documentation (Viewable in browser):** [api-docs.html](https://raw.githack.com/CodeURJC-DAW-2024-25/webapp04/main/ByteMarket/backend/api-docs/api-docs.html)

## 📌 Class diagram updated 
![JavaClass](./Screenshots/Diagrams/Class%20Diagram%20Updated.jpg)


## 🐳 Docker execution instructions
1. Install [Docker](https://docs.docker.com/engine/install/)

2. Run Docker
   
3. Clone the Repository:
   ```
   git clone https://github.com/CodeURJC-DAW-2024-25/webapp04
   ```
   
4. Navigating to the Directory Containing docker-compose.yml:
   ```
   cd webapp04/Docker/
   ```

5. Deploying the Application with Docker Compose:
   ```
   docker compose up
   ``` 

6. The application will be accesible in the following URL: [https://localhost:8443](https://localhost:8443#)

7. To stop the deployment, run the following command:
   ```
   docker compose down
   ```

### 📦 Docker image contruction documentation
To build and publish the Docker image for the application, follow these instructions:
1. Install [Docker](https://docs.docker.com/engine/install/)

2. Run Docker 
   
3. Clone the Repository:
   ```
   git clone https://github.com/CodeURJC-DAW-2024-25/webapp04
   ```
   
4. Navigating to the Directory Containing create_image.sh:
   ```
   cd webapp04/Docker/
   ```

5. Give execution permission to create_image.sh command file:
   ```
   chmod +x create_image.sh
   ```
   
6. Execute the Build Script:
   ```
   ./create_image.sh
   ```

To access the Docker image for ByteMarket, visit the following URL: https://hub.docker.com/repository/docker/marcosgrc/webapp04/general. This image contains the latest stable version of ByteMarket application, ready for deployment in a Docker environment.


### 🖥️ Documentation for deploying on the virtual machine
To deploy the application on the virtual machine provided by the university, follow these steps:

### Prerequisites
- Ensure you have access to a machine with an internet connection and SSH (Secure Shell) installed. This is standard on most Linux and MacOS systems, and available on Windows through tools like PowerShell or Git Bash.
- You must be connected to the university's network directly or via MyApps to access the virtual machine.
- Make sure you have the private key (`appWeb04.key`) downloaded on your local machine and have set the appropriate permissions. Windows users may need to adjust permissions by following the guides provided in these links: [Microsoft vscode-remote release issue #1619](https://github.com/microsoft/vscode-remote-release/issues/1619#issuecomment-760990038), [Anuj Varma's SSH on Windows guide](https://www.anujvarma.com/ssh-on-windows-permissions-for-private-key-too-open/).

### Deployment Steps
1. Open a terminal on your system and use the following command to connect to the virtual machine.
   ```
   ssh -i ssh-keys/appWeb04.key vmuser@10.100.139.229
   ```
   Also it can be done with: 
   ```
   ssh -i ssh-keys/appWeb04.key vmuser@appWeb04.dawgis.etsii.urjc.es
   ```

2. Clone the repository in the virtual machine with the following command:
   ```
   git clone https://github.com/CodeURJC-DAW-2024-25/webapp04
   ```

3. Navigating to the Directory Containing docker-compose.yml:
   ```
   cd webapp04/Docker/
   ```

4. Deploying the Application with Docker Compose:
   ```
   docker compose up -d
   ```
5. The application will be accesible in the following URL: [https://appweb04.dawgis.etsii.urjc.es:443](https://appweb04.dawgis.etsii.urjc.es:443)

6. To stop the deployment, run the following command:
   ```
   docker compose down
   ```

### 🌐 URL App (Deployment on Virtual Machine)
The application is deployed and can be accessed at the following URL: https://appweb04.dawgis.etsii.urjc.es:443

### 🔐 Sample Users
#### 👑 Admin
- **Email:** sara@gmail.com
- **Password:** Password1234
  
#### 👤 User
- **Email:** pedro@gmail.com
- **Password:** 12341234

#### 👤 User
- **Email:** hugo@gmail.com
- **Password:** securePass456

#### 👤 User
- **Email:** alex@gmail.com
- **Password:** securePass1234


## ⚙️ Members participation

### 👤 Olga Chubinova Bortsova
During the development of the web application, I have worked on Review Service, ensuring its integration with the rest of the system, and implemented rest security configuration to strengthen access control and data protection adding. Additionally, I have documented the API using OpenAPI with SpringDoc, making it easier to understand and use the available endpoints.
To facilitate testing and validation, I have created an initial Postman collection for the user and product endpoints. I have also refactored the Review, Reports, and Purchase modules to use DTOs and mappers, ensuring a clear separation between entities and the presentation layer. Furthermore, I have initiated the implementation of the ProductRestController, laying its foundation, and have worked on the UserRestController, structuring its initial endpoints. I have also made minor adjustments to the ProfileController.

| Commit | Description |
| :----: | :---------: |
| [1º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/31bc7ae240244cf734fdf4151b6c0ba8793ccad5) | UserRestController and ProductRestController implemented |
| [2º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/a351c7b4b53748dacd2dad2c5260be0e0e685369) | Review, Reports and Purchase to DTOs and Mappers implemented |
| [3º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/f78b9f3142ef83e202907c4b6afca845fa3782da) | API DOCS |
| [4º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/3b137b4c1cd20c5cebe0959063223d4719082348) | Security |
| [5º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/dd067b40f6ee29feb660406a0466e12d056d3326) | Initial Postman collection of user and product |

| File | Name |
| :----: | :---------: |
| [1º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProductRestController.java) | Product Rest Controller |
| [2º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/ReviewService.java) | Review Service |
| [3º](ByteMarket/backend/src/main/java/es/grupo04/backend/security/WebSecurityConfig.java) | Security |
| [4º](ByteMarket/backend/api-docs/api-docs.yaml) | API DOCS |
| [5º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/UserRestController.java) | UserRestController |

### 👤 Marcos García García
During this phase I was responsible for creating the Dockerfile, docker-compose file and the scripts used to create the Docker image and publishing it at Docker Hub. I also set up the deployment of the application on the virtual machine provided by the university, installing the required programs on this machine, clonning this repository there and finally executing it using docker compose up.
I was also responsible for creating much of the DTOs needed for the API REST as well as the mappers needed to change from a Domain object to a DTO and vice versa. After this, I also refactored some of the alredy existing services and controllers to make then able to work correctly using the new DTOs, specially the ones related to Products and Images.
Finally, I implemented some of the rest controllers needed to make the API REST work correctly, I specially focused on the ones related to Products and Images making and refacotring part of them, as well as the ones related to Reviews and Reports which I completely implemented. I also added the login rest controller needed to authenticate users in the API REST as well as all the security configuration needed to make it work correctly.

| Commit | Description |
| :----: | :---------: |
| [1º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/48b9cf159c083bb39c56bc3f55d7b3233eaac929) | All ProductRestController fixed and RestLoginController added |
| [2º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/9e143dafcd2640ec20feab7693f9a7fdf5f78c13) | Finished Report and Review Rest Controller, changes and fixes in UserRestController and other refactoring changes |
| [3º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/1e775d50362d138a3254a019c38df321ddcc4b63) | Product service and controller refactoring |
| [4º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/1534603fc1183b86a2e10fc6d1a1fd54e3712bfd) | All working dockerfile and docker-compose, refactoring DataBaseInitializer to load images correctly, fixing native query |
| [5º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/72715279ba574bff1d6c1bff0a8ed4fb64927dba) | Finished all DTOs and adjusted existing ones |

| File | Name |
| :----: | :---------: |
| [1º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProductRestController.java) | Product Rest Controller |
| [2º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ReviewReportRestController.java) | Review and Report Rest Controller |
| [3º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/ProductService.java) | Product Service |
| [4º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProductController.java) | Product Controller |
| [5º](Docker/Dockerfile) | Dockerfile |

### 👤 Naroa Martín Simón
I have worked on updating the UserService, ProfileController and UserController, ensuring that all interactions within the presentation layer are done through DTOs instead of entities. I have also managed the users' profile images and improved the various user-related DTOs and mappers.
I have focused on the implementation of the UserRestController, which allows to perform operations to edit and delete users from the REST API, among other functionalities. In addition, I have worked on the ProductRestController, allowing to filter products through the request based on parameters such as name, category or if they are featured products.
Finally, I made sure that all the resources that can be obtained through the REST API return their corresponding Location header when created. In addition, I made specific fixes and improvements to the code and APIRest Urls to optimize its operation and ensure proper handling of API responses.
| Commit | Description |
| :----: | :---------: |
| [1º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/7676d05eb0be8cf87ed2ce7b89593557544e97cf) | UserRestController Merge |
| [2º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/61d2f86fe7c3bc84e9fcdcd311f1ea245adb2bb1) | Filter Products |
| [3º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/8b95bbe3d59dc30b2c7ed94c33e3977cfe26e8a0) | Json body management for editing users profile and minnor fixes |
| [4º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/04f5f5699cd1b3243313d66499e09ab0c771240d) | Profile Controller and Profile picture visibility |
| [5º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/cfa06782f0a21d065b7237532456738c23867331) | User Sevice update and ProfileController |

| File | Name |
| :----: | :---------: |
| [1º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/UserRestController.java) | User Rest Controller |
| [2º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProductRestController.java) | Product Rest Controller |
| [3º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/UserService.java) | User Service |
| [4º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ProfileController.java) | Profile Controller |
| [5º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/UserWebController.java) | User Web Controller |

### 👤 Adrián Muñoz Serrano
During this phase, my main focus was on everything related to the chat, in addition to resolving and fixing minor general issues on the website. I was responsible for modifying the ChatService, MessageService, and PurchaseService to use DTOs and mappers. Additionally, I updated the ChatController to properly integrate these services, ensuring that messaging and product purchases functioned correctly. I was also responsible for creating the ChatRestController to handle chat-related requests. Furthermore, I created and tested the Postman collection for the ChatRestController to ensure that all requests were functioning correctly, and later consolidated all collections of all the RestControllers into a single file, ensuring a uniform URL structure. Additionally, I updated the class diagram of the project.

| Commit | Description |
| :----: | :---------: |
| [1º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/af1f946e3ce7bfb7850f0c39b95c23f06f61d56c) | ChatService and PurchaseService with DTOs |
| [2º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/65b78eff4e7b73f08165f45d8b74760ffda7bbae) | Adapt ChatController to the DTO |
| [3º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/50fbe50016c390df11f42705a9ef4c143620054d) | Creation of ChatRestController |
| [4º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/5369b33a0a988610c442dee33c296bd23bba615d) | Fixed ChatRestController with JSON |
| [5º](https://github.com/CodeURJC-DAW-2024-25/webapp04/commit/956d32cb88e8a31a8fac4b9d496c33915f97d1a0) | Requests of purchases in UserRestController |

| File | Name |
| :----: | :---------: |
| [1º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ChatController.java) | ChatController |
| [2º](ByteMarket/backend/src/main/java/es/grupo04/backend/controller/ChatRestController.java) | ChatRestController |
| [3º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/ChatService.java) | ChatService |
| [4º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/MessageService.java) | MessageService |
| [5º](ByteMarket/backend/src/main/java/es/grupo04/backend/service/PurchaseService.java) | PurchaseService |




***   
# 🌀 Phase 4 - SPA Implementation with Angular.
***


## 🛠️ Development Environment Setup
To set up the development environment for the SPA application using Angular, follow these steps:

**Prerequisites**
1. Node.js: Ensure that Node.js is installed on your system. You can download it from Node.js official website.
2. Open a terminal on your system and execute the following commands:

```
npm install
```

```
cd webapp04
```

```
cd frontend
```

```
ng serve --proxy-config proxy.conf.json
```
3. Run the application with SpringBoot


### 🌐 Deployment on Virtual Machine
How to deploy:
```
ng build --configuration production --base-href="/new/"
```

```
cp dist/frontend/browser/* ../backend/src/main/resources/public/new/
```

### 🔗 URL App
The application is deployed and can be accessed at the following URL: https://appweb04.dawgis.etsii.urjc.es:8443/new 
Also it can be accessed by the following URL: https://localhost:8443/new/

## 📌 Class diagram and SPA templates
![JavaClass](./Screenshots/Diagrams/Class%20Diagram%20SPA.jpg)


## ⚙️ Members participation

### 👤 Olga Chubinova Bortsova
...

| Commit | Description |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |

| File | Name |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |


### 👤 Marcos García García
...

| Commit | Description |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |

| File | Name |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |


### 👤 Naroa Martín Simón
...

| Commit | Description |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |

| File | Name |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |



### 👤 Adrián Muñoz Serrano
...

| Commit | Description |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |

| File | Name |
| :----: | :---------: |
| [1º]() | |
| [2º]() | |
| [3º]() | |
| [4º]() | |
| [5º]() | |


## 🔗 Youtube link video 