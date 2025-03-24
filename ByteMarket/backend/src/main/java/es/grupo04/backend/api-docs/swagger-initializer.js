window.onload = function() {
    window.ui = SwaggerUIBundle({
        spec: {
            "openapi": "3.1.0",
  "info": {
    "title": "OpenAPI definition",
    "version": "v0"
  },
  "servers": [
    {
      "url": "https://appweb04.dawgis.etsii.urjc.es:443",
      "description": "Generated server url"
    }
  ],
  "paths": {
    "/api/v1/profile": {
      "put": {
        "summary": "Update user profile information and optionally upload a profile picture",
        "tags": [
          "profile-rest-controller"
        ],
        "operationId": "updateProfile",
        "parameters": [
          {
            "name": "name",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "address",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "newPass",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "repeatPass",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "iframe",
            "in": "query",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": {
                  "profilePicInput": {
                    "type": "string",
                    "format": "binary"
                  }
                }
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/products/{id}": {
      "get": {
        "summary": "Retrieve a product by its ID",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "getProductById",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ProductDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      },
      "put": {
        "summary": "Update an existing product by its ID",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "updateProduct",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          },
          {
            "name": "newProductDTO",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/NewProductDTO"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ProductDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      },
      "delete": {
        "summary": "Delete a product by its ID",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "deleteProduct",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ProductDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/users/{userId}/reviews": {
      "get": {
        "summary": "Fetch all reviews for a specific user by their ID",
        "tags": [
          "review-report-rest-controller"
        ],
        "operationId": "getReviews",
        "parameters": [
          {
            "name": "userId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/ReviewDTO"
                  }
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      },
      "post": {
        "summary": "Submit a new review for a user by their ID",
        "tags": [
          "review-report-rest-controller"
        ],
        "operationId": "postReview",
        "parameters": [
          {
            "name": "reviewDTO",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/NewReviewDTO"
            }
          },
          {
            "name": "userId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ReviewDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/users/signin": {
      "post": {
        "summary": "Register a new user",
        "tags": [
          "user-rest-controller"
        ],
        "operationId": "createUser",
        "parameters": [
          {
            "name": "user",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/NewUserDTO"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/reports/products/{productId}": {
      "post": {
        "summary": "Report a product by its ID",
        "tags": [
          "review-report-rest-controller"
        ],
        "operationId": "postReport",
        "parameters": [
          {
            "name": "reportDTO",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/NewReportDTO"
            }
          },
          {
            "name": "productId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ReportDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/products": {
      "get": {
        "summary": "Retrieve a paginated list of all products with optional filters",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "getAllProducts",
        "parameters": [
          {
            "name": "page",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 0
            }
          },
          {
            "name": "size",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 8
            }
          },
          {
            "name": "name",
            "in": "query",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "category",
            "in": "query",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "recommended",
            "in": "query",
            "required": false,
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageProductDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      },
      "post": {
        "summary": "Create a new product",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "createProduct",
        "parameters": [
          {
            "name": "productDTO",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/NewProductDTO"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ProductDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/products/{id}/images": {
      "post": {
        "summary": "Upload an image for a specific product by its ID",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "addImage",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": {
                  "image": {
                    "type": "string",
                    "format": "binary"
                  }
                },
                "required": [
                  "image"
                ]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "OK"
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/products/{id}/favorites": {
      "post": {
        "summary": "Toggle a product as a favorite by its ID",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "toggleFavoriteProduct",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          },
          {
            "name": "page",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 0
            }
          },
          {
            "name": "size",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 8
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageProductDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/chats/{productId}": {
      "post": {
        "summary": "Create a new chat for a product by its ID",
        "tags": [
          "chat-rest-controller"
        ],
        "operationId": "createChat",
        "parameters": [
          {
            "name": "productId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ChatDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/chats/{id}/send": {
      "post": {
        "summary": "Send a message in a chat by its ID",
        "tags": [
          "chat-rest-controller"
        ],
        "operationId": "sendMessage",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          },
          {
            "name": "message",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/chats/{id}/sell": {
      "post": {
        "summary": "Mark a product as sold in a chat by its ID",
        "tags": [
          "chat-rest-controller"
        ],
        "operationId": "sellProduct",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/auth/refresh": {
      "post": {
        "summary": "Refresh an authentication token",
        "tags": [
          "rest-login-controller"
        ],
        "operationId": "refreshToken",
        "parameters": [
          {
            "name": "RefreshToken",
            "in": "cookie",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/AuthResponse"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/auth/logout": {
      "post": {
        "summary": "Log out the current user",
        "tags": [
          "rest-login-controller"
        ],
        "operationId": "logOut",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/AuthResponse"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/auth/login": {
      "post": {
        "summary": "Authenticate a user and log in",
        "tags": [
          "rest-login-controller"
        ],
        "operationId": "login",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/LoginRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/AuthResponse"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/users": {
      "get": {
        "summary": "Retrieve a list of all users",
        "tags": [
          "user-rest-controller"
        ],
        "operationId": "getAllUsers",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/UserDTO"
                  }
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/users/{userId}": {
      "get": {
        "summary": "Fetch a user’s details by their ID",
        "tags": [
          "user-rest-controller"
        ],
        "operationId": "getUserById",
        "parameters": [
          {
            "name": "userId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/users/me": {
      "get": {
        "summary": "Retrieve details of the authenticated user",
        "tags": [
          "user-rest-controller"
        ],
        "operationId": "getAuthenticatedUser",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/reviews/{reviewId}": {
      "get": {
        "summary": "Retrieve a review by its ID",
        "tags": [
          "review-report-rest-controller"
        ],
        "operationId": "getReview",
        "parameters": [
          {
            "name": "reviewId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ReviewDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      },
      "delete": {
        "summary": "Delete a review by its ID",
        "tags": [
          "review-report-rest-controller"
        ],
        "operationId": "deleteReview",
        "parameters": [
          {
            "name": "reviewId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/reports": {
      "get": {
        "summary": "Fetch a list of all reports",
        "tags": [
          "review-report-rest-controller"
        ],
        "operationId": "getReports",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/ReportDTO"
                  }
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/profile/stats": {
      "get": {
        "summary": "Retrieve statistics for the user’s profile",
        "tags": [
          "profile-rest-controller"
        ],
        "operationId": "stats",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/profile/image/{id}": {
      "get": {
        "summary": "Fetch the profile image of a user by their ID",
        "tags": [
          "profile-rest-controller"
        ],
        "operationId": "getProfileImage",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/products/sales": {
      "get": {
        "summary": "Retrieve a list of the user’s recent sales",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "getLastSales",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/ProductDTO"
                  }
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/products/purchases": {
      "get": {
        "summary": "Retrieve a list of the user’s recent purchases",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "getLastPurchases",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/ProductDTO"
                  }
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/products/favorites": {
      "get": {
        "summary": "Fetch a paginated list of the user’s favorite products",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "getFavoriteProducts",
        "parameters": [
          {
            "name": "page",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 0
            }
          },
          {
            "name": "size",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 8
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageProductDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/chats": {
      "get": {
        "summary": "Retrieve a list of all chats for the authenticated user",
        "tags": [
          "chat-rest-controller"
        ],
        "operationId": "getUserChats",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/ChatDTO"
                  }
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/chats/{id}": {
      "get": {
        "summary": "Fetch a specific chat by its ID",
        "tags": [
          "chat-rest-controller"
        ],
        "operationId": "getChat",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ChatDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/reports/{reportId}": {
      "delete": {
        "summary": "Delete a report by its ID",
        "tags": [
          "review-report-rest-controller"
        ],
        "operationId": "deleteReport",
        "parameters": [
          {
            "name": "reportId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/profile/{id}": {
      "delete": {
        "summary": "Delete a user account by its ID",
        "tags": [
          "profile-rest-controller"
        ],
        "operationId": "deleteAccount",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    },
    "/api/v1/products/{productId}/images/{imageId}": {
      "delete": {
        "summary": "Remove an image from a product by product and image IDs",
        "tags": [
          "product-rest-controller"
        ],
        "operationId": "removeImage",
        "parameters": [
          {
            "name": "productId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          },
          {
            "name": "imageId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK"
          },
          "404": {
            "description": "Not Found"
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "NewProductDTO": {
        "type": "object",
        "properties": {
          "name": {
            "type": "string"
          },
          "price": {
            "type": "number",
            "format": "float"
          },
          "category": {
            "type": "string"
          },
          "description": {
            "type": "string"
          },
          "imageUpload": {
            "type": "array",
            "items": {
              "type": "string",
              "format": "binary"
            }
          }
        }
      },
      "ProductDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "name": {
            "type": "string"
          },
          "price": {
            "type": "number",
            "format": "float"
          },
          "category": {
            "type": "string"
          },
          "description": {
            "type": "string"
          },
          "owner": {
            "$ref": "#/components/schemas/UserBasicDTO"
          },
          "imageUrls": {
            "type": "array",
            "items": {
              "type": "string"
            }
          },
          "thumbnail": {
            "type": "string"
          },
          "sold": {
            "type": "boolean"
          },
          "publishDate": {
            "type": "string",
            "format": "date"
          }
        }
      },
      "UserBasicDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "name": {
            "type": "string"
          },
          "image": {
            "type": "string"
          },
          "hasImage": {
            "type": "boolean"
          }
        }
      },
      "NewReviewDTO": {
        "type": "object",
        "properties": {
          "rating": {
            "type": "integer",
            "format": "int32"
          },
          "description": {
            "type": "string"
          },
          "reviewedUserId": {
            "type": "integer",
            "format": "int64"
          }
        }
      },
      "ReviewDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "description": {
            "type": "string"
          },
          "reviewOwner": {
            "$ref": "#/components/schemas/UserBasicDTO"
          },
          "reviewedUser": {
            "$ref": "#/components/schemas/UserBasicDTO"
          },
          "rating": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "NewUserDTO": {
        "type": "object",
        "properties": {
          "mail": {
            "type": "string"
          },
          "name": {
            "type": "string"
          },
          "password": {
            "type": "string"
          },
          "repeatPassword": {
            "type": "string"
          }
        }
      },
      "NewReportDTO": {
        "type": "object",
        "properties": {
          "reason": {
            "type": "string"
          },
          "description": {
            "type": "string"
          },
          "productId": {
            "type": "integer",
            "format": "int64"
          },
          "reportCreatorId": {
            "type": "integer",
            "format": "int64"
          }
        }
      },
      "ProductBasicDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "name": {
            "type": "string"
          },
          "price": {
            "type": "number",
            "format": "float"
          },
          "category": {
            "type": "string"
          },
          "sold": {
            "type": "boolean"
          },
          "publishDate": {
            "type": "string",
            "format": "date"
          }
        }
      },
      "ReportDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "reason": {
            "type": "string"
          },
          "description": {
            "type": "string"
          },
          "product": {
            "$ref": "#/components/schemas/ProductBasicDTO"
          },
          "reportCreator": {
            "$ref": "#/components/schemas/UserBasicDTO"
          }
        }
      },
      "PageProductDTO": {
        "type": "object",
        "properties": {
          "totalPages": {
            "type": "integer",
            "format": "int32"
          },
          "totalElements": {
            "type": "integer",
            "format": "int64"
          },
          "size": {
            "type": "integer",
            "format": "int32"
          },
          "content": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ProductDTO"
            }
          },
          "number": {
            "type": "integer",
            "format": "int32"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "first": {
            "type": "boolean"
          },
          "last": {
            "type": "boolean"
          },
          "numberOfElements": {
            "type": "integer",
            "format": "int32"
          },
          "pageable": {
            "$ref": "#/components/schemas/PageableObject"
          },
          "empty": {
            "type": "boolean"
          }
        }
      },
      "PageableObject": {
        "type": "object",
        "properties": {
          "offset": {
            "type": "integer",
            "format": "int64"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "unpaged": {
            "type": "boolean"
          },
          "paged": {
            "type": "boolean"
          },
          "pageSize": {
            "type": "integer",
            "format": "int32"
          },
          "pageNumber": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "SortObject": {
        "type": "object",
        "properties": {
          "empty": {
            "type": "boolean"
          },
          "sorted": {
            "type": "boolean"
          },
          "unsorted": {
            "type": "boolean"
          }
        }
      },
      "ChatDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "product": {
            "$ref": "#/components/schemas/ProductBasicDTO"
          },
          "userBuyer": {
            "$ref": "#/components/schemas/UserBasicDTO"
          },
          "userSeller": {
            "$ref": "#/components/schemas/UserBasicDTO"
          },
          "messages": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/MessageDTO"
            }
          },
          "selling": {
            "type": "boolean"
          }
        }
      },
      "MessageDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "sender": {
            "$ref": "#/components/schemas/UserBasicDTO"
          },
          "message": {
            "type": "string"
          },
          "sentAt": {
            "type": "string"
          }
        }
      },
      "AuthResponse": {
        "type": "object",
        "properties": {
          "status": {
            "type": "string",
            "enum": [
              "SUCCESS",
              "FAILURE"
            ]
          },
          "message": {
            "type": "string"
          },
          "error": {
            "type": "string"
          }
        }
      },
      "LoginRequest": {
        "type": "object",
        "properties": {
          "username": {
            "type": "string"
          },
          "password": {
            "type": "string"
          }
        }
      },
      "UserDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "name": {
            "type": "string"
          },
          "creationYear": {
            "type": "integer",
            "format": "int32"
          },
          "roles": {
            "type": "array",
            "items": {
              "type": "string"
            }
          },
          "products": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ProductBasicDTO"
            }
          },
          "reviews": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ReviewDTO"
            }
          },
          "sales": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ProductBasicDTO"
            }
          },
          "purchases": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ProductBasicDTO"
            }
          },
          "iframe": {
            "type": "string"
          },
          "image": {
            "type": "string"
          },
          "hasImage": {
            "type": "boolean"
          }
        }
      }
    }
  }
        }, // PEGA AQUÍ TU JSON ENTERO
        dom_id: '#swagger-ui', 
    });
};
